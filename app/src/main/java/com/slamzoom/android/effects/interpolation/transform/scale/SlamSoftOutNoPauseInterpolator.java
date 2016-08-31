package com.slamzoom.android.effects.interpolation.transform.scale;

import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 3/26/16.
 */
public class SlamSoftOutNoPauseInterpolator extends Interpolator {
  @Override
  public float getValue(float t) {
    return (float) (1 - Math.pow(0.6 * (t + 0.6666), 5));
  }
}