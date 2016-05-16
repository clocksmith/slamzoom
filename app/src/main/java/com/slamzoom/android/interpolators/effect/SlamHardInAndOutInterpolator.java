package com.slamzoom.android.interpolators.effect;

import com.slamzoom.android.interpolators.base.Interpolator;

/**
 * Created by clocksmith on 3/22/16.
 */
public class SlamHardInAndOutInterpolator extends Interpolator {
  @Override
  protected float getValue(float percent) {
    if (percent < 0.5) {
      return (float) Math.pow(0.5 * (percent + 1.5), 20);
    } else {
      return (float) (1 - Math.pow(0.5 * (percent + 1), 10));
    }
  }
}
