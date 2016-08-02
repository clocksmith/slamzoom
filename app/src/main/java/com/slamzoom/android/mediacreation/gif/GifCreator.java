package com.slamzoom.android.mediacreation.gif;

import android.content.Context;
import android.graphics.Bitmap;

import com.slamzoom.android.common.bus.BusProvider;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.mediacreation.CreateMediaCallback;
import com.slamzoom.android.mediacreation.MediaCreator;
import com.slamzoom.android.mediacreation.MultiPhaseStopwatch;

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
    BusProvider.getInstance().post(new ProgressUpdateEvent(mEffectTemplate.getName(), amountToUpdate));
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
    super(context, gifConfig.bitmap, getAdjustedEffectTemplate(gifConfig), gifSize, gifConfig.fps, callback, tracker);
  }

  private static EffectTemplate getAdjustedEffectTemplate(GifConfig gifConfig) {
    // This is weird. Not yet sure how else to do this.
    EffectTemplate effectTemplate = gifConfig.effectTemplate;
    for (EffectStep step : effectTemplate.getEffectSteps()) {
      step.setHotspot(gifConfig.hotspot);
      if (gifConfig.endText != null) {
        step.setEndText(gifConfig.endText);
      }
    }
    return effectTemplate;
  }

  @Override
  public GifFrame createFrame(Bitmap bitmap, int delayMillis) {
    mTracker.start(STOPWATCH_PIXELIZING);
    GifFrame frame =  new GifFrame(bitmap, delayMillis, mTracker);
    mTracker.stop(STOPWATCH_PIXELIZING);
    if (!mIsPreview) {
      BusProvider.getInstance().post(
          new ProgressUpdateEvent(mEffectTemplate.getName(), 1.0 / 3 / mTotalNumFrames));
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
