package com.slamzoom.android.mediacreation.video;

import android.content.Context;
import android.graphics.Bitmap;

import com.slamzoom.android.mediacreation.MediaConfig;
import com.slamzoom.android.mediacreation.MediaCreator;
import com.slamzoom.android.mediacreation.MediaEncoder;
import com.slamzoom.android.mediacreation.MediaFrame;
import com.slamzoom.android.mediacreation.MultiPhaseStopwatch;

/**
 * Created by clocksmith on 3/25/16.
 */
public class VideoCreator extends MediaCreator {
  public VideoCreator(
      Context context,
      MediaConfig config) {
    super(context, config, new MultiPhaseStopwatch());
  }

  @Override
  public MediaFrame createFrame(Bitmap bitmap, int delayMillis, int frameIndex) {
    return new VideoFrame(bitmap, delayMillis, frameIndex);
  }

  @Override
  public MediaEncoder createEncoder() {
    return new VideoEncoder();
  }
}
