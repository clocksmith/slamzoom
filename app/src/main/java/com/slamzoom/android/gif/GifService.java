package com.slamzoom.android.gif;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import com.google.common.base.Predicate;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.slamzoom.android.common.BusProvider;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.effect.EffectModel;
import com.slamzoom.android.effect.EffectStep;

import java.util.List;

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

  private static final int PREVIEW_CACHE_SIZE = 200;
  private static final int CACHE_SIZE = 50;

  private static GifService mGifService = new GifService();

  private Cache<EffectModel, byte[]> mGifPreviewCache;
  private Cache<EffectModel, byte[]> mGifCache;
  private List<EffectModel> mEffectModels;
  private Bitmap mSelectedBitmap;
  private Rect mCropRect;

  private GifService() {
    mGifPreviewCache = CacheBuilder.newBuilder()
        .maximumSize(PREVIEW_CACHE_SIZE)
        .build();
    mGifCache = CacheBuilder.newBuilder()
        .maximumSize(CACHE_SIZE)
        .build();
    mEffectModels = Lists.newArrayList();
  }

  public static GifService getInstance() {
    return mGifService;
  }

  public void setEffectModels(List<EffectModel> effectModels) {
    mEffectModels = effectModels;
    updateGifPreviewsIfPossible();
  }

  public void setSelectedBitmap(Bitmap bitmap) {
    mGifCache.asMap().clear();
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
    updateGif(selectedEffectName);
    updateGifPreviewsIfPossible();
  }

  // TODO(clocksmith) refactor this to share for updateGIfIfPossible
  private void updateGifPreviewsIfPossible() {
    if (mEffectModels != null && mSelectedBitmap != null && mCropRect != null) {
      for (final EffectModel effectModel : mEffectModels) {
        if (mGifPreviewCache.asMap().containsKey(effectModel)) {
          fireGifPreviewReadyEvent(effectModel);
        } else {
          GifUtils.createGif(
              mSelectedBitmap,
              effectModel,
              Constants.DEFAULT_GIF_PREVIEW_SIZE_PX,
              false,
              new GifUtils.CreateGifCallback() {
                @Override
                public void onCreateGif(byte[] gifBytes) {
                  if (gifBytes != null) {
                    mGifPreviewCache.asMap().put(effectModel, gifBytes);
                    fireGifPreviewReadyEvent(effectModel);
                  }
                }
              });
        }
      }
    }
  }

  public void updateGif(final String effectName) {
    if (mEffectModels != null && mSelectedBitmap != null && mCropRect != null) {
      final EffectModel effectModel = Iterables.filter(mEffectModels, new Predicate<EffectModel>() {
        @Override
        public boolean apply(EffectModel input) {
          return input.getName().equals(effectName);
        }
      }).iterator().next();

      if (mGifCache.asMap().containsKey(effectModel)) {
        fireGifReadyEvent(effectModel);
      } else {
        GifUtils.createGif(
            mSelectedBitmap,
            effectModel,
            Constants.DEFAULT_GIF_SIZE_PX,
            true,
            new GifUtils.CreateGifCallback() {
              @Override
              public void onCreateGif(byte[] gifBytes) {
                if (gifBytes != null) {
                  mGifCache.asMap().put(effectModel, gifBytes);
                  fireGifReadyEvent(effectModel);
                }
              }
            });
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
}
