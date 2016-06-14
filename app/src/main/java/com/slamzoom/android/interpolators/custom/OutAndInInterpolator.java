package com.slamzoom.android.interpolators.custom;

import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 6/14/16.
 */
public class OutAndInInterpolator extends Interpolator {
  @Override
  protected float getRangePercent(float t) {
    if (t < 0.5) {
      return 2 * (1 - t);
    } else {
      return 2 * t;
    }
  }
}