package com.slamzoom.android.interpolators.custom;

import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 6/7/16.
 */
public class SlamSoftOutInterpolator extends Interpolator {
  @Override
  protected float getValue(float t) {
    return (float) (1 - Math.pow(t, 4));
  }
}
