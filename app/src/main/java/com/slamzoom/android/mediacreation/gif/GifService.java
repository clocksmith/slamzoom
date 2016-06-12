package com.slamzoom.android.mediacreation.gif;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.slamzoom.android.common.utils.DebugUtils;
import com.slamzoom.android.effects.EffectTemplateProvider;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.singletons.BusProvider;
import com.slamzoom.android.ui.create.effectchooser.EffectThumbnailViewHolder;
import com.squareup.otto.Subscribe;

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
  private Cache<GifConfig, byte[]> mGifCache;
  private GifCreator mGifCreator;

  private Cache<GifConfig, byte[]> mGifPreviewCache;
  private Map<String, GifCreator> mGifPreviewCreators;
  private Map<String, GifConfig> mPreviewConfigs;

  @Override
  public void onCreate() {
    mGifCache = CacheBuilder.newBuilder()
        .maximumSize(DebugUtils.DEBUG_USE_CACHE ? EffectTemplateProvider.getTemplates().size() : 0)
        .build();

    mGifPreviewCache = CacheBuilder.newBuilder()
        .maximumSize(DebugUtils.DEBUG_USE_CACHE ? EffectTemplateProvider.getTemplates().size() : 0)
        .build();
    mGifPreviewCreators = Maps.newHashMap();

    BusProvider.getInstance().register(this);
  }

  public void updatePreviewConfigs(Map<String, GifConfig> configs) {
    mGifPreviewCache.asMap().clear();

    for (GifCreator creator : mGifPreviewCreators.values()) {
      creator.cancel();
    }
    mGifPreviewCreators.clear();

    mPreviewConfigs = Maps.newHashMap(configs);
  }

  public void generate(final GifConfig gifConfig) {
    if (mGifCache.asMap().containsKey(gifConfig)) {
      fireGifReadyEvent(gifConfig);
    } else {
      final long start = System.currentTimeMillis();
      if (mGifCreator != null) {
        mGifCreator.cancel();
      }
      mGifCreator = new GifCreator(
          getApplicationContext(),
          gifConfig,
          Constants.DEFAULT_GIF_SIZE_PX,
          new GifCreator.CreateGifCallback() {
            @Override
            public void onCreateGif(byte[] gifBytes) {
              if (gifBytes != null) {
                Log.wtf(TAG, gifConfig.effectModel.getEffectTemplate().getName() +
                    " gif took a total of " + (System.currentTimeMillis() - start) + "ms to create");
                mGifCache.put(gifConfig, gifBytes);
                fireGifReadyEvent(gifConfig);
              }
            }
          });
      mGifCreator.createAsync();
    }
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

  private void fireGifReadyEvent(GifConfig gifConfig) {
    String effectName = gifConfig.effectModel.getEffectTemplate().getName();
    BusProvider.getInstance().post(new GifReadyEvent(effectName, mGifCache.asMap().get(gifConfig)));
  }

  private void fireGifPreviewReadyEvent(GifConfig gifConfig) {
    String effectName = gifConfig.effectModel.getEffectTemplate().getName();
    BusProvider.getInstance().post(new GifPreviewReadyEvent(effectName, mGifPreviewCache.asMap().get(gifConfig)));
  }

//  @Subscribe
//  public void on(GifCreator.ProgressUpdateEvent event) throws IOException {
//    if (!mGifProgresses.containsKey(event.effectModel)) {
//      mGifProgresses.put(event.effectModel, 0d);
//    }
//
//    mGifProgresses.put(event.effectModel, mGifProgresses.get(event.effectModel) + event.amountToUpdate);
//    BusProvider.getInstance().post(new ProgressUpdateEvent(event.effectModel.getEffectTemplate().getName(),
//        (int) Math.round(100 * mGifProgresses.get(event.effectModel))));
//  }

  @Subscribe
  public void on(EffectThumbnailViewHolder.RequestGifPreviewEvent event) {
    final String name = event.effectName;
    final GifConfig gifConfig = mPreviewConfigs.get(event.effectName);
    final long start = System.currentTimeMillis();
    final GifCreator gifPreviewCreator = new GifCreator(
        getApplicationContext(),
        gifConfig,
        Constants.DEFAULT_GIF_PREVIEW_SIZE_PX,
        new GifCreator.CreateGifCallback() {
          @Override
          public void onCreateGif(byte[] gifBytes) {
            if (gifBytes != null) {
              Log.wtf(
                  TAG, name + " gif preview took a total of " + (System.currentTimeMillis() - start) + "ms to create");
              mGifPreviewCache.put(gifConfig, gifBytes);
              fireGifPreviewReadyEvent(gifConfig);
            }
          }
        });
    mGifPreviewCreators.put(event.effectName, gifPreviewCreator);
    gifPreviewCreator.createAsync();
  }

  @Subscribe
  public void on(EffectThumbnailViewHolder.RequestCancelGifPreviewEvent event) {
    if (mGifPreviewCreators.containsKey(event.effectName)) {
      GifCreator creator = mGifPreviewCreators.get(event.effectName);
      creator.cancel();
    }
  }
}
