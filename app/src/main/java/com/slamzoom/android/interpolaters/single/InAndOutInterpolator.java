package com.slamzoom.android.interpolaters.single;

import com.slamzoom.android.interpolaters.base.Interpolator;

/**
 * Created by clocksmith on 3/22/16.
 */
public class InAndOutInterpolator extends Interpolator {
  @Override
  protected float getValue(float input) {
    if (input < 0.5) {
      return 2 * input;
    } else {
      return 2 * (1 - input);
    }
  }
}