package com.slamzoom.android.effects.interpolation.transform.scale;

import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 7/8/16.
 */
public class ThreeEaseInHardOutInterpolator extends Interpolator {
  @Override
  public float getValue(float t) {
    return (float) Math.pow(Math.sin(3 * Math.PI * t), 50);
  }
}
