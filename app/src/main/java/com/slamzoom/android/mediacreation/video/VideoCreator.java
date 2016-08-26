package com.slamzoom.android.mediacreation.video;

import android.content.Context;
import android.graphics.Bitmap;

import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.mediacreation.MediaCreator;
import com.slamzoom.android.mediacreation.MediaEncoder;
import com.slamzoom.android.mediacreation.MediaFrame;

/**
 * Created by clocksmith on 3/25/16.
 */
public class VideoCreator extends MediaCreator {
  public VideoCreator(
      Context context,
      Bitmap bitmap,
      EffectTemplate effectTemplate,
      int size,
      int fps,
      VideoCreatorCallback callback) {
    super(context, bitmap, effectTemplate, size, fps, callback, null);
  }

  @Override
  public MediaFrame createFrame(Bitmap bitmap, int delayMillis) {
    return new VideoFrame(bitmap, delayMillis);
  }

  @Override
  public MediaEncoder createEncoder() {
    return new VideoEncoder();
  }
}
