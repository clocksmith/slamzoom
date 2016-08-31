package com.slamzoom.android.effects.interpolation.transform.scale;

import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 6/14/16.
 */
public class OutAndInInterpolator extends Interpolator {
  @Override
  public float getValue(float t) {
    if (t < 0.5) {
      return 1 - 2 * t;
    } else {
      return 2 * t - 1;
    }
  }
}