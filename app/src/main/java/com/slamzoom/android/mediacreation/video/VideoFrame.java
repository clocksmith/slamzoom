package com.slamzoom.android.mediacreation.video;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Log;

import com.slamzoom.android.common.SzLog;
import com.slamzoom.android.common.utils.BitmapUtils;
import com.slamzoom.android.mediacreation.MediaFrame;

import java.io.File;

/**
 * Created by clocksmith on 3/25/16.
 */
public class VideoFrame extends MediaFrame {
  private static final String TAG = VideoFrame.class.getSimpleName();

  File path;

  @SuppressLint("DefaultLocale")
  public VideoFrame(Bitmap bitmap, int delayMillis, int index) {
    super(bitmap, delayMillis);

    path = BitmapUtils.saveBitmapToDiskPrivatelyAsJpeg(bitmap, "video" + (index + 1));
    Log.wtf(TAG, "saved " + index);
  }
}
