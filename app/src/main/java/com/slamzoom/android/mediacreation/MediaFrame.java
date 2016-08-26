package com.slamzoom.android.mediacreation;

import android.graphics.Bitmap;

/**
 * Created by clocksmith on 3/25/16.
 */
public abstract class MediaFrame {
  public Bitmap bitmap;
  public int delayMillis;
  public int width;
  public int height;

  public MediaFrame() {}
  public MediaFrame(Bitmap bitmap, int delayMillis) {
    this.bitmap = bitmap;
    this.delayMillis = delayMillis;
    this.width = bitmap.getWidth();
    this.height = bitmap.getHeight();
  }
}
