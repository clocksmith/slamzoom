package com.slamzoom.android.interpolate.base;

/**
 * Created by clocksmith on 3/12/16.
 */
public class ReverseLinearInterpolator extends Interpolator {
  @Override
  protected float getValue(float input) {
    if (input < 0.5) {
      return input;
    } else {
      return 1 - input;
    }
  }
}
