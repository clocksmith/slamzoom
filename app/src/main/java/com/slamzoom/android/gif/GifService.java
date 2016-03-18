package com.slamzoom.android.gif;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import com.google.common.base.Predicate;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.slamzoom.android.common.BusProvider;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.effect.EffectModel;
import com.slamzoom.android.effect.EffectStep;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifDrawable;

/**
 * Created by clocksmith on 2/27/16.
 *
 * TODO(clocksmith): make this a real service.
 */
public class GifService {
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

  public class ProgressUpdateEvent{
    public final String effectName;
    public final int progress;
    public ProgressUpdateEvent(String effectName, int progress) {
      this.effectName = effectName;
      this.progress = progress;
    }
  }

  private static final int PREVIEW_CACHE_SIZE = 200;
  private static final int CACHE_SIZE = 20;

  private static GifService mGifService = new GifService();

  private Deque<Runnable> mCreateGifPreviewQueue;
  private Deque<Runnable> mCreateGifQueue;
  private boolean mIsCreatingGif;
  private Cache<EffectModel, byte[]> mGifPreviewCache;
  private Cache<EffectModel, byte[]> mGifCache;
  private Map<EffectModel, Double> mGifProgresses;
  private List<EffectModel> mEffectModels;
  private Bitmap mSelectedBitmap;
  private Rect mCropRect;

  private GifService() {
    mCreateGifPreviewQueue = Queues.newLinkedBlockingDeque();
    mCreateGifQueue = Queues.newLinkedBlockingDeque();;
    mGifPreviewCache = CacheBuilder.newBuilder()
        .maximumSize(PREVIEW_CACHE_SIZE)
        .build();
    mGifCache = CacheBuilder.newBuilder()
        .maximumSize(CACHE_SIZE)
        .build();
    mGifProgresses = Maps.newHashMap();
    mEffectModels = Lists.newArrayList();

    BusProvider.getInstance().register(this);
  }

  public static GifService getInstance() {
    return mGifService;
  }

  public void setEffectModels(List<EffectModel> effectModels) {
    mEffectModels = effectModels;
    updateGifPreviewsIfPossible();
  }

  public void setSelectedBitmap(Bitmap bitmap) {
    mSelectedBitmap = bitmap;
    updateGifPreviewsIfPossible();
  }

  public void setCropRect(Rect cropRect, String selectedEffectName) {
    // TODO(clocksmith): temp, change this.
    for (EffectModel effectModel : mEffectModels) {
      for (EffectStep step : effectModel.getEffectSteps()) {
        step.setHotspot(cropRect);
      }
    }

    mCropRect = cropRect;
    updateGifPreviewsIfPossible();
//    updateGifIfPossible(selectedEffectName);
  }

  // TODO(clocksmith) refactor this to share for updateGIfIfPossible
  private void updateGifPreviewsIfPossible() {
    if (mEffectModels != null && mSelectedBitmap != null && mCropRect != null) {
      mGifCache.invalidateAll();
      mCreateGifQueue.clear();
      mCreateGifPreviewQueue.clear();
      for (final EffectModel effectModel : mEffectModels) {
        mCreateGifPreviewQueue.addLast(new Runnable() {
          @Override
          public void run() {
            if (mGifPreviewCache.asMap().containsKey(effectModel)) {
              fireGifPreviewReadyEvent(effectModel);
            } else {
              new GifCreator(
                  mSelectedBitmap,
                  effectModel,
                  Constants.DEFAULT_GIF_PREVIEW_SIZE_PX,
                  false,
                  new GifCreator.CreateGifCallback() {
                    @Override
                    public void onCreateGif(byte[] gifBytes) {
                      if (gifBytes != null) {
                        mGifPreviewCache.asMap().put(effectModel, gifBytes);
                        fireGifPreviewReadyEvent(effectModel);
                      }
                      if (mCreateGifPreviewQueue.peek() != null) {
                        mCreateGifPreviewQueue.pollFirst().run();
                      } else {
                        mIsCreatingGif = false;
                      }
                    }
                  }).createAsync();
            }
          }
        });
      }
      if (mCreateGifPreviewQueue.peek() != null) {
        mCreateGifPreviewQueue.pollFirst().run();
        mIsCreatingGif = true;
      }
    }
  }


  public void updateGifIfPossible(final String effectName) {
    if (mEffectModels != null && mSelectedBitmap != null && mCropRect != null) {
      Iterator<EffectModel> iterator = Iterables.filter(mEffectModels, new Predicate<EffectModel>() {
        @Override
        public boolean apply(EffectModel input) {
          return input.getName().equals(effectName);
        }
      }).iterator();

      if (iterator.hasNext()) {
        final EffectModel effectModel = iterator.next();

        if (mGifCache.asMap().containsKey(effectModel)) {
          fireGifReadyEvent(effectModel);
        } else {
          final long start = System.currentTimeMillis();
          new GifCreator(
              mSelectedBitmap,
              effectModel,
              Constants.DEFAULT_GIF_SIZE_PX,
              true,
              new GifCreator.CreateGifCallback() {
                @Override
                public void onCreateGif(byte[] gifBytes) {
                  if (gifBytes != null) {
                    Log.wtf(TAG, "gif took " + (System.currentTimeMillis() - start) + "ms to make");
                    mGifCache.asMap().put(effectModel, gifBytes);
                    fireGifReadyEvent(effectModel);
                  }
                }
              }).createAsync();
        }
      }
    }
  }

  private void fireGifPreviewReadyEvent(EffectModel effectModel) {
    BusProvider.getInstance().post(
        new GifPreviewReadyEvent(effectModel.getName(), mGifPreviewCache.asMap().get(effectModel)));
  }

  private void fireGifReadyEvent(EffectModel effectModel) {
    BusProvider.getInstance().post(new GifReadyEvent(effectModel.getName(), mGifCache.asMap().get(effectModel)));
  }

  @Subscribe
  public void on(GifCreator.ProgressUpdateEvent event) throws IOException {
    if (!mGifProgresses.containsKey(event.effectModel)) {
      mGifProgresses.put(event.effectModel, 0d);
    }

    mGifProgresses.put(event.effectModel, mGifProgresses.get(event.effectModel) + event.amountToUpdate);
    BusProvider.getInstance().post(new ProgressUpdateEvent(event.effectModel.getName(),
        (int) Math.round(100 * mGifProgresses.get(event.effectModel))));
  }
}
