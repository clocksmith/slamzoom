package com.slamzoom.android.interpolators.effect;

import com.slamzoom.android.interpolators.base.Interpolator;

/**
 * Created by clocksmith on 3/22/16.
 */
public class InAndOutInterpolator extends Interpolator {
  @Override
  protected float getValue(float percent) {
    if (percent < 0.5) {
      return 2 * percent;
    } else {
      return 2 * (1 - percent);
    }
  }
}