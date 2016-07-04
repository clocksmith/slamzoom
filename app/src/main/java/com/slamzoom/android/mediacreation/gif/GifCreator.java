package com.slamzoom.android.mediacreation.gif;

import android.content.Context;
import android.graphics.Bitmap;

import com.slamzoom.android.common.singletons.BusProvider;
import com.slamzoom.android.common.utils.SzLog;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.mediacreation.CreateMediaCallback;
import com.slamzoom.android.mediacreation.MediaCreator;
import com.slamzoom.android.mediacreation.MultiPhaseStopwatch;
import com.slamzoom.android.ui.create.effectchooser.EffectModel;

/**
 * Created by clocksmith on 3/25/16.
 */
public class GifCreator extends MediaCreator implements GifEncoder.ProgressUpdateListener {
  public static final String TAG = GifCreator.class.getSimpleName();

  public interface CreateGifCallback extends CreateMediaCallback {
    void onCreateGif(byte[] gifBytes);
  }

  public static final String STOPWATCH_PIXELIZING = "pixelizing";

  @Override
  public void onProgressUpdate(double amountToUpdate) {
    BusProvider.getInstance().post(new ProgressUpdateEvent(mEffectModel.getEffectTemplate().getName(), amountToUpdate));
  }

  public class ProgressUpdateEvent {
    public final String effectName;
    public final double amountToUpdate;
    public ProgressUpdateEvent(String effectName, double amountToUpdate) {
      this.effectName = effectName;
      this.amountToUpdate = amountToUpdate;
    }
  }

  public GifCreator(
      Context context,
      GifConfig gifConfig,
      int gifSize,
      CreateGifCallback callback,
      MultiPhaseStopwatch tracker) {
    super(context, gifConfig.bitmap, getAdjustedEffectModel(gifConfig), gifSize, callback, tracker);
  }

  private static EffectModel getAdjustedEffectModel(GifConfig gifConfig) {
    // This is weird. Not yet sure how else to do this.
    EffectModel effectModel = gifConfig.effectModel;
    for (EffectStep step :  effectModel.getEffectTemplate().getEffectSteps()) {
      step.setHotspot(gifConfig.hotspot);
      if (gifConfig.endText != null) {
        step.setEndText(gifConfig.endText);
      }
    }
    return effectModel;
  }

  @Override
  public GifFrame createFrame(Bitmap bitmap, int delayMillis) {
    mTracker.start(STOPWATCH_PIXELIZING);
    GifFrame frame =  new GifFrame(bitmap, delayMillis, mTracker);
    mTracker.stop(STOPWATCH_PIXELIZING);
    if (!mIsPreview) {
      BusProvider.getInstance().post(
          new ProgressUpdateEvent(mEffectModel.getEffectTemplate().getName(), 1.0 / 3 / mTotalNumFrames));
    }
    return frame;
  }

  @Override
  public GifEncoder createEncoder() {
    GifEncoder gifEncoder = new GifEncoder();
    if (!mIsPreview) {
      gifEncoder.setProgressUpdateListener(this);
    }
    gifEncoder.setTracker(mTracker);
    return gifEncoder;
  }
}
