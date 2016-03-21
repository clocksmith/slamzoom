package com.slamzoom.android.interpolate;

/**
 * Created by clocksmith on 3/20/16.
 */
public class ReverseIdentityInterpoaltor extends Interpolator {
  @Override
  protected float getValue(float input) {
    return 1 - input;
  }
}