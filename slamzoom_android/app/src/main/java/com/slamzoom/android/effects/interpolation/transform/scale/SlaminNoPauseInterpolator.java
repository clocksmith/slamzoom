package com.slamzoom.android.effects.interpolation.transform.scale;

import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 8/31/16.
 */
public class SlaminNoPauseInterpolator extends Interpolator {
  public float getValue(float t) {
    return (float) Math.pow(0.25 * (t + 3), 20);
  }
}
