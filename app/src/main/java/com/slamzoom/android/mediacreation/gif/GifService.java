package com.slamzoom.android.mediacreation.gif;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.slamzoom.android.common.bus.BusProvider;
import com.slamzoom.android.common.utils.DebugUtils;
import com.slamzoom.android.common.utils.SzAnalytics;
import com.slamzoom.android.common.utils.SzLog;
import com.slamzoom.android.effects.Effects;
import com.slamzoom.android.ui.create.effectchooser.EffectThumbnailViewHolder;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Created by clocksmith on 4/14/16.
 */
public class GifService extends Service {
  private static final String TAG = GifService.class.getSimpleName();

  private static final int MAIN_CACHE_SIZE = 10;
  private static final int THUMBNAIL_CACHE_SIZE = Effects.listEffects().size();;

  public class GifReadyEvent {
    public final String effectName;
    public final byte[] gifBytes;
    public boolean thumbnail;
    public GifReadyEvent(String effectName, byte[] gifBytes, boolean thumbnail) {
      this.effectName = effectName;
      this.gifBytes = gifBytes;
      this.thumbnail = thumbnail;
    }
  }

  public class GifGenerationSartEvent {}

  public class GifServiceBinder extends Binder {
    public GifService getService() {
      return GifService.this;
    }
  }

  private final IBinder mBinder = new GifServiceBinder();

  private Cache<String, byte[]> mMainGifCache;
  private Cache<String, byte[]> mThumbnailGifCache;
  private GifCreatorManager mGifCreatorManager;
  private List<GifCreatorManager> mThumbnailGifCreatorBackQueue;
  private List<GifCreatorManager> mThumbnailGifCreatorPriorityQueue;

  @Override
  public void onCreate() {
    super.onCreate();
    SzLog.f(TAG, "onCreate()");

    mMainGifCache = CacheBuilder.newBuilder()
        .maximumSize(DebugUtils.DEBUG_USE_CACHE ? MAIN_CACHE_SIZE : 0)
        .build();
    mThumbnailGifCache = CacheBuilder.newBuilder()
        .maximumSize(DebugUtils.DEBUG_USE_CACHE ? THUMBNAIL_CACHE_SIZE : 0)
        .build();

    mThumbnailGifCreatorBackQueue = Lists.newArrayList();
    mThumbnailGifCreatorPriorityQueue = Lists.newArrayList();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    SzLog.f(TAG, "onDestroy()");
    stopCreatorsAndClearCaches();
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    SzLog.f(TAG, "onBind()");
    BusProvider.getInstance().register(this);
    return mBinder;
  }

  @Override
  public boolean onUnbind(Intent intent) {
    SzLog.f(TAG, "onUnbind()");
    BusProvider.getInstance().unregister(this);
    return super.onUnbind(intent);
  }

  public void requestThumbnailGifs(List<GifConfig> configs) {
    stopCreatorsAndClearCaches();

    if (DebugUtils.DEBUG_GENERATE_THUMBNAIL_GIFS) {
      mThumbnailGifCreatorBackQueue = Lists.newArrayList(Lists.transform(configs, new Function<GifConfig, GifCreatorManager>() {
        @Override
        public GifCreatorManager apply(GifConfig input) {
          return getManager(input, true, mThumbnailGifCache);
        }
      }));
    } else {
      mThumbnailGifCreatorBackQueue = Lists.newArrayList();
    }
  }

  public void requestMainGif(final GifConfig config) {
    SzLog.f(TAG, "endText: " + config.endText);
    final String name = config.effectModel.getEffectTemplate().getName();
    if (mMainGifCache.asMap().containsKey(name)) {
      fireGifReadyEvent(name, false);
    } else {
      if (mGifCreatorManager != null && !mGifCreatorManager.getName().equals(name)) {
        mGifCreatorManager.stop();
        mGifCreatorManager = getManager(config, false, mMainGifCache);
      } else if (mGifCreatorManager == null) {
        mGifCreatorManager = getManager(config, false, mMainGifCache);
      } else {
        return;
      }

      stopThumbnailCreators();
      BusProvider.getInstance().post(new GifGenerationSartEvent());
      mGifCreatorManager.start();
    }
  }

  private void stopCreatorsAndClearCaches() {
    stopAndDeleteMainCreator();
    stopThumbnailCreators();
    clearThumbnailCreators();

    mMainGifCache.invalidateAll();
    mThumbnailGifCache.invalidateAll();
  }

  private GifCreatorManager getManager(GifConfig config, final boolean thumbnail, final Cache<String, byte[]> cache) {
    final String name = config.effectModel.getEffectTemplate().getName();
    return new GifCreatorManager(
        getApplicationContext(),
        config,
        thumbnail,
        new GifCreator.CreateGifCallback() {
          @Override
          public void onCreateGif(byte[] gifBytes) {
            if (gifBytes != null) {
              cache.put(name, gifBytes);
              onGifReadyEvent(name, thumbnail);
              fireGifReadyEvent(name, thumbnail);
              if (thumbnail) {
                int i = 0;
                for (GifCreatorManager manager : mThumbnailGifCreatorPriorityQueue) {
                  if (manager.getName().equals(name)) {
                    mThumbnailGifCreatorPriorityQueue.remove(i);
                    break;
                  }
                  i++;
                }
              }
              continueThumbnailGifGeneration();
            }
          }
        });
  }

  private void stopAndDeleteMainCreator() {
    if (mGifCreatorManager != null) {
      if (mGifCreatorManager.isRunning()) {
        mGifCreatorManager.stop();
      }
      mGifCreatorManager = null;
    }
  }

  private void stopThumbnailCreators() {
    stopCreatorQueue(mThumbnailGifCreatorBackQueue);
    stopCreatorQueue(mThumbnailGifCreatorPriorityQueue);
  }

  private void clearThumbnailCreators() {
    mThumbnailGifCreatorBackQueue.clear();
    mThumbnailGifCreatorPriorityQueue.clear();
  }

  private void stopCreatorQueue(List<GifCreatorManager> queue) {
    for (GifCreatorManager thumbnailManager : queue) {
      if (thumbnailManager.isRunning()) {
        thumbnailManager.stop();
      }
    }
  }

  @Subscribe
  public void on(EffectThumbnailViewHolder.RequestThumbnailGifEvent event) {
    final String name = event.effectName;
    if (mThumbnailGifCache.asMap().containsKey(name)) {
      fireGifReadyEvent(name, true);
    } else {
      int i = 0;
      for (GifCreatorManager thumbnailManager : mThumbnailGifCreatorBackQueue) {
        if (thumbnailManager.getName().equals(name)) {
          GifCreatorManager managerWithPriority = mThumbnailGifCreatorBackQueue.remove(i);
          mThumbnailGifCreatorPriorityQueue.add(managerWithPriority);
          continueThumbnailGifGeneration();
          break;
        }
        i++;
      }
    }
  }

  @Subscribe
  public void on(EffectThumbnailViewHolder.RequestStopThumbnailGifGenerationEvent event) {
    final String name = event.effectName;
    int i = 0;
    for (GifCreatorManager thumbnailManager : mThumbnailGifCreatorPriorityQueue) {
      if (thumbnailManager.getName().equals(name)) {
        GifCreatorManager managerToCancel = mThumbnailGifCreatorPriorityQueue.remove(i);
        if (managerToCancel.isRunning()) {
          managerToCancel.stop();
        }
        mThumbnailGifCreatorBackQueue.add(managerToCancel);
        break;
      }
      i++;
    }
    continueThumbnailGifGeneration();
  }

  private void continueThumbnailGifGeneration() {
    SzLog.f(TAG, "continueThumbnailGifGeneration()");

    if (mThumbnailGifCreatorPriorityQueue.isEmpty() && !mThumbnailGifCreatorBackQueue.isEmpty()) {
      mThumbnailGifCreatorPriorityQueue.add(mThumbnailGifCreatorBackQueue.remove(0));
    }

    if (!mThumbnailGifCreatorPriorityQueue.isEmpty() && !mThumbnailGifCreatorPriorityQueue.get(0).isRunning()) {
      mThumbnailGifCreatorPriorityQueue.get(0).start();
    }
  }

  private void onGifReadyEvent(String name, boolean thumbnail) {
    if (!thumbnail) {
//      SzLog.f(TAG, "name: " + name + "\n" + mGifCreatorManager.getTracker().getReport());
      SzLog.f(TAG, "name: " + name + "\n" + mGifCreatorManager.getTracker().getTotalString());
    }

    GifCreatorManager currentThumbnailManager = null;
    for (GifCreatorManager thumbnailManager : mThumbnailGifCreatorPriorityQueue) {
      if (thumbnailManager.getName().equals(name)) {
        currentThumbnailManager = thumbnailManager;
        break;
      }
    }
    GifCreatorManager currentManager = thumbnail ? currentThumbnailManager : mGifCreatorManager;
    if (currentManager != null) {
      SzAnalytics.newGifGeneratedEvent()
          .withItemId(name)
          .withDurationMs(currentManager.getTracker().getTotal())
          .withGifSize(currentManager.getGifSize())
          .withEndScale(currentManager.getEndScale())
          .withFps(currentManager.getFps())
          .log(this);
    } else {
      SzLog.e(TAG, "Current GifCreatorManager for onGifReadyEvent is null!");
    }
  }

  private void fireGifReadyEvent(String name, boolean thumbnail) {
    BusProvider.getInstance().post(
        new GifReadyEvent(
            name,
            thumbnail ? mThumbnailGifCache.asMap().get(name) : mMainGifCache.asMap().get(name),
            thumbnail));
  }
}
