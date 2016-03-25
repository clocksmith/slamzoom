package com.slamzoom.android.interpolaters.single;

import com.slamzoom.android.interpolaters.base.Interpolator;

/**
 * Created by clocksmith on 3/22/16.
 */
public class SlamSoftOutInterpolator extends Interpolator {
  @Override
  protected float getValue(float input) {
    return (float) (1 - Math.pow(input, 10));
  }
}
