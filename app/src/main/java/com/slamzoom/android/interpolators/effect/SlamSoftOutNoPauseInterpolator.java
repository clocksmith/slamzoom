package com.slamzoom.android.interpolators.effect;

import com.slamzoom.android.interpolators.base.Interpolator;

/**
 * Created by clocksmith on 3/26/16.
 */
public class SlamSoftOutNoPauseInterpolator extends Interpolator {
  @Override
  public float getValue(float percent) {
    return (float) (1 - Math.pow(0.6 * (percent + 0.6666), 5));
  }
}