package com.slamzoom.android.interpolate.single;

import com.slamzoom.android.interpolate.base.Interpolator;

/**
 * Created by clocksmith on 3/22/16.
 */
public class SlamHardInAndOutInterpolator extends Interpolator {
  @Override
  protected float getValue(float input) {
    if (input < 0.5) {
      return (float) Math.pow(0.5 * (input + 1.5), 20);
    } else {
      return (float) (1 - Math.pow(0.5 * (input + 1), 20));
    }
  }
}
