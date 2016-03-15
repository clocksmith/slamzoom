package com.slamzoom.android.interpolate.scale;

/**
 * Created by clocksmith on 3/12/16.
 */
public class SlamHardOvershootInterpolator extends AbstractScaleInterpolator {
  @Override
  protected double getX(double input) {
    if (input < 0.95) {
      return Math.pow((input + 0.05), 20);
    } else {
      return Math.pow((input - 2), 20);
    }
  }
}
