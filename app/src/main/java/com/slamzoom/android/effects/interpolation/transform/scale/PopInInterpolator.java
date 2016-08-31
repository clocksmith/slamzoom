package com.slamzoom.android.effects.interpolation.transform.scale;

import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 8/30/16.
 */
public class PopInInterpolator extends Interpolator {
  @Override
  public float getInterpolation(float t) {
    return getValue(t);
  }

  @Override
  public float getValue(float t) {
    return 1 - t;
  }
}
