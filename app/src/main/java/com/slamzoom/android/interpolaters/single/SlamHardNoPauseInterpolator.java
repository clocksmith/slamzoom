package com.slamzoom.android.interpolaters.single;

import com.slamzoom.android.interpolaters.base.Interpolator;

/**
 * Created by clocksmith on 3/26/16.
 */
public class SlamHardNoPauseInterpolator extends Interpolator {
  @Override
  protected float getValue(float input) {
    return (float) Math.pow(0.25 * (input + 3), 20);
  }
}
