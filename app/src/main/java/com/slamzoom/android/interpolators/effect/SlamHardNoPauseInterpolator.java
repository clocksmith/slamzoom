package com.slamzoom.android.interpolators.effect;

import com.slamzoom.android.interpolators.base.Interpolator;

/**
 * Created by clocksmith on 3/26/16.
 */
public class SlamHardNoPauseInterpolator extends Interpolator {
  @Override
  protected float getValue(float input) {
    return (float) Math.pow(0.25 * (input + 3), 20);
  }
}
