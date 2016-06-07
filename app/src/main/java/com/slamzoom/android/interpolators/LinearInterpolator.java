package com.slamzoom.android.interpolators;

/**
 * Created by antrob on 2/24/16.
 */
public class LinearInterpolator extends MonomialInterpolator {
  public LinearInterpolator(float start, float end) {
    this();
    super.setRange(start, end);
  }

  public LinearInterpolator() {
    super(1);
  }
}
