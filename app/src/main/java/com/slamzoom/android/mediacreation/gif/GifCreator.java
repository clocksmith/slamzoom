package com.slamzoom.android.mediacreation.gif;

import android.content.Context;
import android.graphics.Bitmap;

import com.slamzoom.android.common.BusProvider;
import com.slamzoom.android.mediacreation.MediaConfig;
import com.slamzoom.android.mediacreation.MediaCreator;
import com.slamzoom.android.mediacreation.StopwatchTracker;

/**
 * Created by clocksmith on 3/25/16.
 */
public class GifCreator extends MediaCreator implements GifEncoder.ProgressUpdateListener {
  public static final String TAG = GifCreator.class.getSimpleName();

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
      MediaConfig mediaConfig,
      StopwatchTracker tracker) {
    super(context, mediaConfig, tracker);
  }

  @Override
  public GifFrame createFrame(Bitmap bitmap, int delayMillis, int frameIndex) {
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
