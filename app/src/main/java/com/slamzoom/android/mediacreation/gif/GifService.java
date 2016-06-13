package com.slamzoom.android.mediacreation.gif;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.common.base.Function;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.slamzoom.android.common.utils.DebugUtils;
import com.slamzoom.android.effects.EffectTemplateProvider;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.singletons.BusProvider;
import com.slamzoom.android.ui.create.effectchooser.EffectThumbnailViewHolder;
import com.squareup.otto.Subscribe;

import java.util.List;
import java.util.Map;

/**
 * Created by clocksmith on 4/14/16.
 */
public class GifService extends Service {
  private static final String TAG = GifService.class.getSimpleName();

  public abstract class BaseGifReadyEvent {
    public final String effectName;
    public final byte[] gifBytes;
    public BaseGifReadyEvent(String effectName, byte[] gifBytes) {
      this.effectName = effectName;
      this.gifBytes = gifBytes;
    }
  }

  public class GifReadyEvent extends BaseGifReadyEvent {
    public GifReadyEvent(String effectName, byte[] gifBytes) {
      super(effectName, gifBytes);
    }
  }

  public class GifPreviewReadyEvent extends BaseGifReadyEvent {
    public GifPreviewReadyEvent(String effectName, byte[] gifBytes) {
      super(effectName, gifBytes);
    }
  }

  public class GifServiceBinder extends Binder {
    public GifService getService() {
      return GifService.this;
    }
  }

  private final IBinder mBinder = new GifServiceBinder();

  private Cache<String, byte[]> mGifCache;
  private Cache<String, byte[]> mGifPreviewCache;
  private Map<String, GifCreatorManager> mGifCreatorManagers;
  private Map<String, GifCreatorManager> mGifPreviewCreatorManagers;
  private List<GifCreatorManager> mGifPreviewCreatorQueue;

  @Override
  public void onCreate() {
    mGifCache = CacheBuilder.newBuilder()
        .maximumSize(DebugUtils.DEBUG_USE_CACHE ? EffectTemplateProvider.getTemplates().size() : 0)
        .build();
    mGifPreviewCache = CacheBuilder.newBuilder()
        .maximumSize(DebugUtils.DEBUG_USE_CACHE ? EffectTemplateProvider.getTemplates().size() : 0)
        .build();

    mGifCreatorManagers = Maps.newHashMap();
    mGifPreviewCreatorManagers = Maps.newHashMap();
    mGifPreviewCreatorQueue = Lists.newArrayList();

    BusProvider.getInstance().register(this);
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    super.onStartCommand(intent, flags, startId);
    return START_STICKY;
  }

  public void resetWithConfigs(Map<String, GifConfig> configs) {
    for (GifCreatorManager manager : mGifCreatorManagers.values()) {
      manager.cancel();
    }
    for (GifCreatorManager manager : mGifPreviewCreatorManagers.values()) {
      manager.cancel();
    }

    mGifCache.asMap().clear();
    mGifPreviewCache.asMap().clear();
    mGifCreatorManagers.clear();
    mGifPreviewCreatorManagers.clear();
    mGifPreviewCreatorQueue.clear();

    mGifCreatorManagers.clear();
    mGifPreviewCreatorManagers.clear();

    mGifCreatorManagers.putAll(Maps.transformValues(configs, new Function<GifConfig, GifCreatorManager>() {
      @Override
      public GifCreatorManager apply(final GifConfig gifConfig) {
        final String name = gifConfig.effectModel.getEffectTemplate().getName();
        return new GifCreatorManager(
            getApplicationContext(),
            gifConfig,
            Constants.DEFAULT_GIF_SIZE_PX,
            new GifCreator.CreateGifCallback() {
              @Override
              public void onCreateGif(byte[] gifBytes) {
                if (gifBytes != null) {
                  mGifCache.put(name, gifBytes);
                  fireGifReadyEvent(name);
                  continueGifPreviewGeneration();
                }
              }
            });
      }
    }));

    mGifPreviewCreatorManagers.putAll(Maps.transformValues(configs, new Function<GifConfig, GifCreatorManager>() {
      @Override
      public GifCreatorManager apply(final GifConfig gifConfig) {
        final String name = gifConfig.effectModel.getEffectTemplate().getName();
        return new GifCreatorManager(
            getApplicationContext(),
            gifConfig,
            Constants.DEFAULT_GIF_PREVIEW_SIZE_PX,
            new GifCreator.CreateGifCallback() {
              @Override
              public void onCreateGif(byte[] gifBytes) {
                if (gifBytes != null) {
                  mGifPreviewCache.put(name, gifBytes);
                  fireGifPreviewReadyEvent(name);
                  for (int i = 0; i < mGifPreviewCreatorQueue.size(); i++) {
                    if (mGifPreviewCreatorQueue.get(i).getName().equals(name)) {
                      mGifPreviewCreatorQueue.remove(i);
                    }
                  }
                  continueGifPreviewGeneration();
                }
              }
            });
      }
    }));
  }

  public void requestGif(final GifConfig gifConfig) {
    final String name = gifConfig.effectModel.getEffectTemplate().getName();
    if (mGifCache.asMap().containsKey(name)) {
      fireGifReadyEvent(name);
    } else {
      for (Map.Entry<String, GifCreatorManager> entry : mGifCreatorManagers.entrySet()) {
        if (entry.getValue().isRunning() && !entry.getKey().equals(name)) {
          entry.getValue().stop();
        }
        if (!entry.getValue().isRunning() && entry.getKey().equals(name)) {
          entry.getValue().start();
        }
      }
    }
  }

  @Subscribe
  public void on(EffectThumbnailViewHolder.RequestGifPreviewEvent event) {
    final String name = event.effectName;
    mGifPreviewCreatorQueue.add(0, mGifPreviewCreatorManagers.get(name));
    continueGifPreviewGeneration();
  }

  @Subscribe
  public void on(EffectThumbnailViewHolder.RequestCancelGifPreviewEvent event) {
    mGifPreviewCreatorManagers.get(event.effectName).stop();
  }

  private void continueGifPreviewGeneration() {
    if (!mGifPreviewCreatorQueue.isEmpty()) {
      boolean isRunning = false;
      for (GifCreatorManager manager : mGifPreviewCreatorQueue) {
        if (manager.isRunning()) {
          isRunning = true;
          break;
        }
      }
      if (!isRunning) {
        mGifPreviewCreatorQueue.get(0).start();
      }
    }
  }


  private void fireGifReadyEvent(String name) {
    Log.wtf(TAG, "name: " + name + "\n" + mGifCreatorManagers.get(name).getTracker().getReport());
    BusProvider.getInstance().post(new GifReadyEvent(name, mGifCache.asMap().get(name)));
  }

  private void fireGifPreviewReadyEvent(String name) {
    BusProvider.getInstance().post(new GifPreviewReadyEvent(name, mGifPreviewCache.asMap().get(name)));
  }

}
