package com.slamzoom.android.interpolators.effect;

import com.slamzoom.android.interpolators.base.Interpolator;

/**
 * Created by clocksmith on 3/20/16.
 */
public class ReverseIdentityInterpoaltor extends Interpolator {
  @Override
  protected float getValue(float input) {
    return 1 - input;
  }
}