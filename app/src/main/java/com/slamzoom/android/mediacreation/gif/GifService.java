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
import com.slamzoom.android.effects.EffectTemplateProvider;
import com.slamzoom.android.global.Constants;
import com.slamzoom.android.global.singletons.BusProvider;
import com.slamzoom.android.ui.create.effectchooser.EffectModel;
import com.squareup.otto.Subscribe;

import java.io.IOException;
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

  public class ProgressUpdateEvent{
    public final String effectName;
    public final int progress;
    public ProgressUpdateEvent(String effectName, int progress) {
      this.effectName = effectName;
      this.progress = progress;
    }
  }

  public class GifServiceBinder extends Binder {
    public GifService getService() {
      return GifService.this;
    }
  }

  private static final int CACHE_SIZE = EffectTemplateProvider.getTemplates().size();

  private final IBinder mBinder = new GifServiceBinder();
  private Cache<GifConfig, byte[]> mGifCache;
  private Map<EffectModel, Double> mGifProgresses;

  @Override
  public void onCreate() {
    mGifCache = CacheBuilder.newBuilder()
        .maximumSize(CACHE_SIZE)
        .build();
    mGifProgresses = Maps.newHashMap();

    BusProvider.getInstance().register(this);
  }

  public void update(final GifConfig gifConfig) {
    if (mGifCache.asMap().containsKey(gifConfig)) {
      fireGifReadyEvent(gifConfig);
    } else {
      final long start = System.currentTimeMillis();
      new GifCreator(
          getApplicationContext(),
          gifConfig,
          Constants.DEFAULT_GIF_SIZE_PX,
          new GifCreator.CreateGifCallback() {
            @Override
            public void onCreateGif(byte[] gifBytes) {
              if (gifBytes != null) {
                Log.wtf(TAG, "gif took " + (System.currentTimeMillis() - start) + "ms to make");
                mGifCache.put(gifConfig, gifBytes);
                fireGifReadyEvent(gifConfig);
              }
            }
          }).createAsync();
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

  @Subscribe
  public void on(GifCreator.ProgressUpdateEvent event) throws IOException {
    if (!mGifProgresses.containsKey(event.effectModel)) {
      mGifProgresses.put(event.effectModel, 0d);
    }

    mGifProgresses.put(event.effectModel, mGifProgresses.get(event.effectModel) + event.amountToUpdate);
    BusProvider.getInstance().post(new ProgressUpdateEvent(event.effectModel.getEffectTemplate().getName(),
        (int) Math.round(100 * mGifProgresses.get(event.effectModel))));
  }
}
