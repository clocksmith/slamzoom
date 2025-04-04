package com.slamzoom.android.effects.interpolation.transform.scale;

import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 3/26/16.
 */
public class SlamHardNoPauseInterpolator extends Interpolator {
  @Override
  public float getValue(float t) {
    // TODO(clocksmith): This, and probably some other interpolators, should return 0 when input is 0.
    return (float) Math.pow(0.25 * (t + 3), 20);
  }
}
