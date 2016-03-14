package com.slamzoom.android.gif.encoder;

import android.graphics.Bitmap;

/**
 * Created by clocksmith on 3/11/16.
 */
public class Frame {
  public Bitmap bitmap;
  public int delay;

  public Frame(Bitmap bitmap, int delay) {
    this.bitmap = bitmap;
    this.delay = delay;
  }
}