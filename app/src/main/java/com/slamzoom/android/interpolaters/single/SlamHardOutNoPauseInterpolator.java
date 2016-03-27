package com.slamzoom.android.interpolaters.single;

import com.slamzoom.android.interpolaters.base.Interpolator;

/**
 * Created by clocksmith on 3/26/16.
 */
public class SlamHardOutNoPauseInterpolator extends Interpolator {
  @Override
  public float getValue(float input) {
    return (float) (1 - Math.pow(0.25 * (input + 3), 20));
  }
}
