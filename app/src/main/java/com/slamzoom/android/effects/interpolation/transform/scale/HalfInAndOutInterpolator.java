package com.slamzoom.android.effects.interpolation.transform.scale;

import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 3/12/16.
 */
public class HalfInAndOutInterpolator extends Interpolator {
  @Override
  public float getValue(float t) {
    if (t < 0.5) {
      return t;
    } else {
      return 1 - t;
    }
  }
}
