package com.slamzoom.android.mediacreation.gif;

import android.content.Context;
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
import com.slamzoom.android.effects.EffectTemplateProvider;
import com.slamzoom.android.global.singletons.BusProvider;
import com.slamzoom.android.global.Constants;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.ui.main.effectchooser.EffectModel;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

  private static final int PREVIEW_CACHE_SIZE = EffectTemplateProvider.getTemplates().size();
  private static final int CACHE_SIZE = PREVIEW_CACHE_SIZE;

  private static GifService mGifService = new GifService();

  private Context mContext;
  private Deque<Runnable> mCreateGifQueue;
  private boolean mIsCreatingGif;
  private Cache<EffectModel, byte[]> mGifPreviewCache;
  private Cache<EffectModel, byte[]> mGifCache;
  private Map<EffectModel, Double> mGifProgresses;
  private List<EffectModel> mEffectModels;
  private Bitmap mSelectedBitmap;
  private Rect mHotspot;
  private String mEndText;

  private GifService() {
    mCreateGifQueue = Queues.newLinkedBlockingDeque();
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

  public void setContext(Context context) {
    mContext = context;
  }

  public void setEffectModels(List<EffectModel> effectModels) {
    mEffectModels = effectModels;
  }

  public void setSelectedBitmap(Bitmap bitmap) {
    mSelectedBitmap = bitmap;
  }

  public void setHotspot(Rect hotspot) {
    // TODO(clocksmith): temp, change this.
    for (EffectModel effectTemplate : mEffectModels) {
      for (EffectStep step : effectTemplate.getEffectTemplate().getEffectSteps()) {
        step.setHotspot(hotspot);
      }
    }

    mHotspot = hotspot;
    updateGifPreviewsIfPossible();
  }

  public void setEndText(String endText) {
    // TODO(clocksmith): temp, change this.
    for (EffectModel effectTemplate : mEffectModels) {
      for (EffectStep step : effectTemplate.getEffectTemplate().getEffectSteps()) {
        step.setEndText(endText);
      }
    }

    if (!endText.equals(mEndText)) {
      mEndText = endText;
      updateGifPreviewsIfPossible();
    }
  }

  // TODO(clocksmith) refactor this to share for updateGIfIfPossible
  private void updateGifPreviewsIfPossible() {
    if (mEffectModels != null && mSelectedBitmap != null && mHotspot != null) {
      mGifCache.invalidateAll();
      mGifPreviewCache.invalidateAll();
      mCreateGifQueue.clear();
      for (final EffectModel effectModel : mEffectModels) {
        final long start = System.currentTimeMillis();
        final String effectName = effectModel.getEffectTemplate().getName();
        mCreateGifQueue.addLast(new Runnable() {
          @Override
          public void run() {
            mIsCreatingGif = true;
            new GifCreator(
                mContext,
                mSelectedBitmap,
                effectModel,
                Constants.DEFAULT_GIF_PREVIEW_SIZE_PX,
                false,
                new GifCreator.CreateGifCallback() {
                  @Override
                  public void onCreateGif(byte[] gifBytes) {
                    mIsCreatingGif = false;
                    if (gifBytes != null) {
                      Log.wtf(TAG, "gif preview took " + (System.currentTimeMillis() - start) + "ms to make");
                      mGifPreviewCache.put(effectModel, gifBytes);
                      fireGifPreviewReadyEvent(effectModel);
                    }
                    resumeQueue();
                  }
                }).createAsync();
          }
        });
      }
      resumeQueue();
    }
  }

  public void updateGifIfPossible(final String effectName) {
    if (mEffectModels != null && mSelectedBitmap != null && mHotspot != null) {
      Iterator<EffectModel> iterator = Iterables.filter(mEffectModels, new Predicate<EffectModel>() {
        @Override
        public boolean apply(EffectModel input) {
          return input.getEffectTemplate().getName().equals(effectName);
        }
      }).iterator();

      if (iterator.hasNext()) {
        final EffectModel effectModel = iterator.next();

        if (mGifCache.asMap().containsKey(effectModel)) {
          fireGifReadyEvent(effectModel);
        } else {
          final long start = System.currentTimeMillis();
          mCreateGifQueue.addFirst(new Runnable() {
            @Override
            public void run() {
              mIsCreatingGif = true;
              new GifCreator(
                  mContext,
                  mSelectedBitmap,
                  effectModel,
                  Constants.DEFAULT_GIF_SIZE_PX,
                  true,
                  new GifCreator.CreateGifCallback() {
                    @Override
                    public void onCreateGif(byte[] gifBytes) {
                      mIsCreatingGif = false;
                      if (gifBytes != null) {
                        Log.wtf(TAG, "gif took " + (System.currentTimeMillis() - start) + "ms to make");
                        mGifCache.put(effectModel, gifBytes);
                        fireGifReadyEvent(effectModel);
                        resumeQueue();
                      }
                    }
                  }).createAsync();
            }
          });
        }
      }
      resumeQueue();
    }
  }

  private void resumeQueue() {
    if (!mIsCreatingGif && mCreateGifQueue.peek() != null) {
      mCreateGifQueue.removeFirst().run();
      mIsCreatingGif = true;
    } else {
      mIsCreatingGif = false;
    }
  }

  private void fireGifPreviewReadyEvent(EffectModel effectModel) {
    String effectName = effectModel.getEffectTemplate().getName();
    BusProvider.getInstance().post(new GifPreviewReadyEvent(effectName, mGifPreviewCache.asMap().get(effectModel)));
  }

  private void fireGifReadyEvent(EffectModel effectModel) {
    String effectName = effectModel.getEffectTemplate().getName();
    BusProvider.getInstance().post(new GifReadyEvent(effectName, mGifCache.asMap().get(effectModel)));
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
