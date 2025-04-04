package com.slamzoom.android.mediacreation.video;

import android.content.Context;
import android.graphics.Bitmap;

import com.slamzoom.android.mediacreation.MediaConfig;
import com.slamzoom.android.mediacreation.MediaCreator;
import com.slamzoom.android.mediacreation.MediaEncoder;
import com.slamzoom.android.mediacreation.MediaFrame;
import com.slamzoom.android.mediacreation.StopwatchTracker;

/**
 * Created by clocksmith on 3/25/16.
 */
public class VideoCreator extends MediaCreator {
  private static final String SAVING_FRAME = "saving_frames";

  public VideoCreator(
      Context context,
      MediaConfig config,
      StopwatchTracker tracker) {
    super(context, config,tracker);
  }

  @Override
  public MediaFrame createFrame(Bitmap bitmap, int delayMillis, int frameIndex) {
    mTracker.start(SAVING_FRAME);
    VideoFrame videoFrame = new VideoFrame(bitmap, delayMillis, frameIndex);
    mTracker.stop(SAVING_FRAME);
    return videoFrame;
  }

  @Override
  public MediaEncoder createEncoder() {
    VideoEncoder encoder = new VideoEncoder();
    encoder.setTracker(mTracker);
    return encoder;
  }
}
