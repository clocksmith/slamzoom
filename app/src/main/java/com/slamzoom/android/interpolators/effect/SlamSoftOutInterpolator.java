package com.slamzoom.android.interpolators.effect;

import com.slamzoom.android.interpolators.base.Interpolator;

/**
 * Created by clocksmith on 3/22/16.
 */
public class SlamSoftOutInterpolator extends Interpolator {
  @Override
  protected float getValue(float input) {
    return (float) (1 - Math.pow(input, 10));
  }
}
