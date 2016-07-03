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
import com.slamzoom.android.common.singletons.BusProvider;
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

  private static int MAIN_CACHE_SIZE = 10;
  private static int PREVIEW_CACHE_SIZE = Effects.listEffects().size();;

  public class GifReadyEvent {
    public final String effectName;
    public final byte[] gifBytes;
    public boolean preview;
    public GifReadyEvent(String effectName, byte[] gifBytes, boolean preview) {
      this.effectName = effectName;
      this.gifBytes = gifBytes;
      this.preview = preview;
    }
  }

  public class GifGenerationSartEvent {}

  public class GifServiceBinder extends Binder {
    public GifService getService() {
      return GifService.this;
    }
  }

  private final IBinder mBinder = new GifServiceBinder();

  private Cache<String, byte[]> mGifCache;
  private Cache<String, byte[]> mGifPreviewCache;
  private GifCreatorManager mGifCreatorManager;
  private List<GifCreatorManager> mGifPreviewCreatorBackQueue;
  private List<GifCreatorManager> mGifPreviewCreatorPriorityQueue;

  @Override
  public void onCreate() {
    super.onCreate();
    SzLog.f(TAG, "onCreate()");

    mGifCache = CacheBuilder.newBuilder()
        .maximumSize(DebugUtils.DEBUG_USE_CACHE ? MAIN_CACHE_SIZE : 0)
        .build();
    mGifPreviewCache = CacheBuilder.newBuilder()
        .maximumSize(DebugUtils.DEBUG_USE_CACHE ? PREVIEW_CACHE_SIZE : 0)
        .build();

    mGifPreviewCreatorBackQueue = Lists.newArrayList();
    mGifPreviewCreatorPriorityQueue = Lists.newArrayList();
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

  public void requestPreviewGifs(List<GifConfig> configs) {
    stopCreatorsAndClearCaches();

    if (DebugUtils.DEBUG_GENERATE_PREVIEWS) {
      mGifPreviewCreatorBackQueue = Lists.newArrayList(Lists.transform(configs, new Function<GifConfig, GifCreatorManager>() {
        @Override
        public GifCreatorManager apply(GifConfig input) {
          return getManager(input, true, mGifPreviewCache);
        }
      }));
    } else {
      mGifPreviewCreatorBackQueue = Lists.newArrayList();
    }
  }

  public void requestMainGif(final GifConfig config) {
    final String name = config.effectModel.getEffectTemplate().getName();
    if (mGifCache.asMap().containsKey(name)) {
      fireGifReadyEvent(name, false);
    } else {
      if (mGifCreatorManager != null && !mGifCreatorManager.getName().equals(name)) {
        mGifCreatorManager.stop();
        mGifCreatorManager = getManager(config, false, mGifCache);
      } else if (mGifCreatorManager == null) {
        mGifCreatorManager = getManager(config, false, mGifCache);
      } else {
        return;
      }

      stopPreviewCreators();
      BusProvider.getInstance().post(new GifGenerationSartEvent());
      mGifCreatorManager.start();
    }
  }

  private void stopCreatorsAndClearCaches() {
    stopMainCreator();
    stopPreviewCreators();

    mGifCache.invalidateAll();
    mGifPreviewCache.invalidateAll();
  }

  private GifCreatorManager getManager(GifConfig config, final boolean preview, final Cache<String, byte[]> cache) {
    final String name = config.effectModel.getEffectTemplate().getName();
    return new GifCreatorManager(
        getApplicationContext(),
        config,
        preview,
        new GifCreator.CreateGifCallback() {
          @Override
          public void onCreateGif(byte[] gifBytes) {
            if (gifBytes != null) {
              cache.put(name, gifBytes);
              onGifReadyEvent(name, preview);
              fireGifReadyEvent(name, preview);
              if (preview) {
                int i = 0;
                for (GifCreatorManager manager : mGifPreviewCreatorPriorityQueue) {
                  if (manager.getName().equals(name)) {
//                    SzLog.f(TAG, "finished " + name + " in " + manager.getTracker().getTotalString());
                    mGifPreviewCreatorPriorityQueue.remove(i);
                    break;
                  }
                  i++;
                }
              }
              continueGifPreviewGeneration();
            }
          }
        });
  }

  private void stopMainCreator() {
    if (mGifCreatorManager != null) {
      if (mGifCreatorManager.isRunning()) {
        mGifCreatorManager.stop();
      }
      mGifCreatorManager = null;
    }
  }

  private void stopPreviewCreators() {
    stopCreatorQueue(mGifPreviewCreatorBackQueue);
    stopCreatorQueue(mGifPreviewCreatorPriorityQueue);
  }

  private void stopCreatorQueue(List<GifCreatorManager> queue) {
    for (GifCreatorManager previewManager : queue) {
      if (previewManager.isRunning()) {
        previewManager.stop();
      }
    }
    queue.clear();
  }
  @Subscribe
  public void on(EffectThumbnailViewHolder.RequestGifPreviewEvent event) {
    final String name = event.effectName;
    if (mGifPreviewCache.asMap().containsKey(name)) {
      fireGifReadyEvent(name, true);
    } else {
      int i = 0;
      for (GifCreatorManager previewManager : mGifPreviewCreatorBackQueue) {
        if (previewManager.getName().equals(name)) {
          GifCreatorManager managerWithPriority = mGifPreviewCreatorBackQueue.remove(i);
          mGifPreviewCreatorPriorityQueue.add(managerWithPriority);
          continueGifPreviewGeneration();
          break;
        }
        i++;
      }
    }
  }

  @Subscribe
  public void on(EffectThumbnailViewHolder.RequestGifPreviewStopEvent event) {
    final String name = event.effectName;
    int i = 0;
    for (GifCreatorManager previewManager : mGifPreviewCreatorPriorityQueue) {
      if (previewManager.getName().equals(name)) {
        GifCreatorManager managerToCancel = mGifPreviewCreatorPriorityQueue.remove(i);
        if (managerToCancel.isRunning()) {
          managerToCancel.stop();
        }
        mGifPreviewCreatorBackQueue.add(managerToCancel);
        break;
      }
      i++;
    }
    continueGifPreviewGeneration();
  }

  private void continueGifPreviewGeneration() {
    if (!mGifPreviewCreatorPriorityQueue.isEmpty() && !mGifPreviewCreatorPriorityQueue.get(0).isRunning()) {
      mGifPreviewCreatorPriorityQueue.get(0).start();
    } else if (mGifPreviewCreatorPriorityQueue.isEmpty() && !mGifPreviewCreatorBackQueue.isEmpty()) {
      mGifPreviewCreatorPriorityQueue.add(mGifPreviewCreatorBackQueue.remove(0));
      continueGifPreviewGeneration();
    }
  }

  private void onGifReadyEvent(String name, boolean preview) {
    if (!preview) {
      SzLog.f(TAG, "name: " + name + "\n" + mGifCreatorManager.getTracker().getReport());
    }

    GifCreatorManager currentPreviewManager = null;
    for (GifCreatorManager previewManager : mGifPreviewCreatorPriorityQueue) {
      if (previewManager.getName().equals(name)) {
        currentPreviewManager = previewManager;
        break;
      }
    }
    GifCreatorManager currentManager = preview ? currentPreviewManager : mGifCreatorManager;
    if (currentManager != null) {
      SzAnalytics.newGifGeneratedEvent()
          .withItemId(name)
          .withDurationMs(currentManager.getTracker().getTotal())
          .withGifSize(currentManager.getGifSize())
          .withEndScale(currentManager.getEndScale())
          .log(this);
    } else {
      SzLog.e(TAG, "Current GifCreatorManager for onGifReadyEvent is null!");
    }
  }

  private void fireGifReadyEvent(String name, boolean preview) {
    BusProvider.getInstance().post(
        new GifReadyEvent(
            name,
            preview ? mGifPreviewCache.asMap().get(name) : mGifCache.asMap().get(name),
            preview));
  }
}
