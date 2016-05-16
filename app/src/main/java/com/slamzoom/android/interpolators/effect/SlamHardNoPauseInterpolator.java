package com.slamzoom.android.interpolators.effect;

import com.slamzoom.android.interpolators.base.Interpolator;

/**
 * Created by clocksmith on 3/26/16.
 */
public class SlamHardNoPauseInterpolator extends Interpolator {
  @Override
  protected float getValue(float percent) {
    // TODO(clocksmith): This, and probably some other interpolators, should return 0 when input is 0.
    return (float) Math.pow(0.25 * (percent + 3), 20);
  }
}
