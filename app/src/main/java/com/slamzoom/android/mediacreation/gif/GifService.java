package com.slamzoom.android.mediacreation.gif;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Queues;
import com.slamzoom.android.common.bus.BusProvider;
import com.slamzoom.android.common.utils.DebugUtils;
import com.slamzoom.android.common.SzAnalytics;
import com.slamzoom.android.common.SzLog;
import com.slamzoom.android.effects.Effects;
import com.slamzoom.android.mediacreation.MediaConfig;
import com.slamzoom.android.ui.create.effectchooser.EffectThumbnailViewHolder;
import com.squareup.otto.Subscribe;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by clocksmith on 4/14/16.
 */
public class GifService extends Service {
  private static final String TAG = GifService.class.getSimpleName();

  private static final int THUMBNAIL_CACHE_SIZE = Effects.listEffectTemplatesByPack().size();
  private static final int MAIN_CACHE_SIZE = THUMBNAIL_CACHE_SIZE;

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

  public class GifGenerationStartEvent {}

  public class GifServiceBinder extends Binder {
    public GifService getService() {
      return GifService.this;
    }
  }

  private final IBinder mBinder = new GifServiceBinder();

  private Cache<String, byte[]> mMainGifCache;
  private Cache<String, byte[]> mThumbnailGifCache;
  private GifCreatorManager mGifCreatorManager;
  private PriorityBlockingQueue<GifCreatorManager> mThumbnailGifCreatorsBackQueue;
  private Queue<GifCreatorManager> mThumbnailGifCreatorRunQueue;

  @Override
  public void onCreate() {
    super.onCreate();
    SzLog.f(TAG, "onCreate()");

    mMainGifCache = CacheBuilder.newBuilder()
        .maximumSize(DebugUtils.SKIP_GIF_CACHE ? 0 : MAIN_CACHE_SIZE)
        .build();
    mThumbnailGifCache = CacheBuilder.newBuilder()
        .maximumSize(DebugUtils.SKIP_GIF_CACHE ? 0 : THUMBNAIL_CACHE_SIZE)
        .build();

    mThumbnailGifCreatorsBackQueue = Queues.newPriorityBlockingQueue();
    mThumbnailGifCreatorRunQueue = Queues.newConcurrentLinkedQueue();
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

  public void clear() {
    stopCreatorsAndClearCaches();
    mThumbnailGifCreatorsBackQueue.clear();
    mThumbnailGifCreatorRunQueue.clear();
  }

  public void requestThumbnailGifs(List<MediaConfig> configs) {
    clear();

    if (!DebugUtils.SKIP_GENERATE_THUMBNAIL_GIFS) {
      for (int i = 0; i < configs.size(); i++) {
        mThumbnailGifCreatorsBackQueue.add(getManager(configs.get(i), mThumbnailGifCache, true, i));
      }
    }

    continueThumbnailGifGeneration();
  }

  public void requestMainGif(final MediaConfig config) {
    SzLog.f(TAG, "endText: " + config.endText);
    final String name = config.effectTemplate.getName();
    if (mMainGifCache.asMap().containsKey(name)) {
      fireGifReadyEvent(name, false);
    } else {
      if (mGifCreatorManager != null && !mGifCreatorManager.getName().equals(name)) {
        mGifCreatorManager.stop();
        mGifCreatorManager = getManager(config, mMainGifCache, false, 0);
      } else if (mGifCreatorManager == null) {
        mGifCreatorManager = getManager(config, mMainGifCache, false, 0);
      } else {
        return;
      }

      stopThumbnailCreators();
      BusProvider.getInstance().post(new GifGenerationStartEvent());
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

  private GifCreatorManager getManager(
      MediaConfig config, final Cache<String, byte[]> cache, final boolean thumbnail, int index) {
    final String name = config.effectTemplate.getName();
    return new GifCreatorManager(
        getApplicationContext(),
        config,
        thumbnail,
        index,
        new GifCreatorCallback() {
          @Override
          public void onCreateGif(byte[] gifBytes) {
            if (gifBytes != null) {
              cache.put(name, gifBytes);
              fireGifReadyEvent(name, thumbnail);
              if (thumbnail) {
                for (GifCreatorManager manager : mThumbnailGifCreatorRunQueue) {
                  if (manager.getName().equals(name)) {
                    mThumbnailGifCreatorRunQueue.remove(manager);
                    break;
                  }
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
    for (GifCreatorManager thumbnailManager : mThumbnailGifCreatorRunQueue) {
      if (thumbnailManager.isRunning()) {
        thumbnailManager.stop();
      }
    }
  }

  private void clearThumbnailCreators() {
    mThumbnailGifCreatorRunQueue.clear();
    mThumbnailGifCreatorsBackQueue.clear();
  }

  @Subscribe
  public synchronized void on(EffectThumbnailViewHolder.RequestThumbnailGifEvent event) {
    final String name = event.effectName;
    if (mThumbnailGifCache.asMap().containsKey(name)) {
      fireGifReadyEvent(name, true);
    } else {
      for (GifCreatorManager thumbnailManager : mThumbnailGifCreatorsBackQueue) {
        if (thumbnailManager.getName().equals(name)) {
          mThumbnailGifCreatorsBackQueue.remove(thumbnailManager);
          mThumbnailGifCreatorRunQueue.add(thumbnailManager);
          continueThumbnailGifGeneration();
          break;
        }
      }
    }
  }

  @Subscribe
  public synchronized void on(EffectThumbnailViewHolder.RequestStopThumbnailGifGenerationEvent event) {
    final String name = event.effectName;
    for (GifCreatorManager thumbnailManager : mThumbnailGifCreatorRunQueue) {
      if (thumbnailManager.getName().equals(name)) {
        mThumbnailGifCreatorRunQueue.remove(thumbnailManager);
        if (thumbnailManager.isRunning()) {
          thumbnailManager.stop();
        }
        mThumbnailGifCreatorsBackQueue.add(thumbnailManager);
        break;
      }
    }
    continueThumbnailGifGeneration();
  }

  private void continueThumbnailGifGeneration() {
    SzLog.f(TAG, "continueThumbnailGifGeneration()");

    if (mThumbnailGifCreatorRunQueue.isEmpty() && !mThumbnailGifCreatorsBackQueue.isEmpty()) {
      mThumbnailGifCreatorRunQueue.add(mThumbnailGifCreatorsBackQueue.remove());
    }

    if (!mThumbnailGifCreatorRunQueue.isEmpty() && !mThumbnailGifCreatorRunQueue.peek().isRunning()) {
      mThumbnailGifCreatorRunQueue.peek().start();
    }
  }

  private void logGifReadyEvent(String name, boolean thumbnail) {
    if (!thumbnail) {
      SzLog.f(TAG, "name: " + name + "\n" + mGifCreatorManager.getTracker().getTotalString());
    }

    GifCreatorManager currentThumbnailManager = null;
    for (GifCreatorManager thumbnailManager : mThumbnailGifCreatorRunQueue) {
      if (thumbnailManager.getName().equals(name)) {
        currentThumbnailManager = thumbnailManager;
        break;
      }
    }
    GifCreatorManager currentManager = thumbnail ? currentThumbnailManager : mGifCreatorManager;
    if (currentManager != null) {
      SzAnalytics.newGifGeneratedEvent()
          .withItemId(name)
          .withGifSize(currentManager.getGifSize())
          .withFps(currentManager.getFps())
          .withEndScale(currentManager.getEndScale())
          .withEndTextLength(currentManager.getEndText() == null ? 0 : currentManager.getEndText().length())
          .withDurationMs(currentManager.getTracker().getTotal())
          .withHasStopped(currentManager.hasStopped())
          .log(this);
    } else {
      SzLog.e(TAG, "Current GifCreatorManager for logGifReadyEvent is null!");
    }
  }

  private void fireGifReadyEvent(String name, boolean thumbnail) {
    logGifReadyEvent(name, thumbnail);

    BusProvider.getInstance().post(
        new GifReadyEvent(
            name,
            thumbnail ? mThumbnailGifCache.asMap().get(name) : mMainGifCache.asMap().get(name),
            thumbnail));
  }
}
