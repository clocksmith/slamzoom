package com.slamzoom.android.interpolators.effect;

import com.slamzoom.android.interpolators.base.Interpolator;

/**
 * Created by clocksmith on 3/12/16.
 */
public class HalfInAndOutInterpolator extends Interpolator {
  @Override
  protected float getRangePercent(float t) {
    if (t < 0.5) {
      return t;
    } else {
      return 1 - t;
    }
  }
}
