package com.slamzoom.android.interpolators.effect;

import com.slamzoom.android.interpolators.base.Interpolator;

/**
 * Created by clocksmith on 3/26/16.
 */
public class SlamHardOutNoPauseInterpolator extends Interpolator {
  @Override
  public float getRangePercent(float t) {
    return (float) (1 - Math.pow(0.25 * (t + 3), 20));
  }
}
