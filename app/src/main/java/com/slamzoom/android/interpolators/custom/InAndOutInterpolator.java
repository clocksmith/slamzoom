package com.slamzoom.android.interpolators.custom;

import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 3/22/16.
 */
public class InAndOutInterpolator extends Interpolator {
  @Override
  protected float getValue(float t) {
    if (t < 0.5) {
      return 2 * t;
    } else {
      return 2 * (1 - t);
    }
  }
}