package com.slamzoom.android.interpolators.effect;

import com.slamzoom.android.interpolators.base.Interpolator;

/**
 * Created by clocksmith on 3/22/16.
 */
public class SlamSoftOutInterpolator extends Interpolator {
  @Override
  protected float getRangePercent(float t) {
    return (float) (1 - Math.pow(t, 10));
  }
}
