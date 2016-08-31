package com.slamzoom.android.effects.interpolation.transform.scale;

import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 3/22/16.
 */
public class SlamHardInAndOutInterpolator extends Interpolator {
  @Override
  public float getValue(float t) {
    if (t < 0.5) {
      return (float) Math.pow(0.5 * (t + 1.5), 20);
    } else {
      return (float) (1 - Math.pow(0.5 * (t + 1), 3));
    }
  }
}
