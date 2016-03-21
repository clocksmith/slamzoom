package com.slamzoom.android.interpolate.single;

import com.slamzoom.android.interpolate.Interpolator;

/**
 * Created by clocksmith on 3/12/16.
 */
public class InAndOutInterpolator extends Interpolator {
  @Override
  protected float getValue(float input) {
    if (input < 0.5) {
      return input;
    } else {
      return 1 - input;
    }
  }
}
