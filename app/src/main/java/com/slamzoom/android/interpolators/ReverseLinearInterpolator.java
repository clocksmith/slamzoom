package com.slamzoom.android.interpolators;

/**
 * Created by clocksmith on 6/7/16.
 */
public class ReverseLinearInterpolator extends LinearInterpolator {
  public float getInterpolation(float t) {
    return super.getInterpolation(1 - t);
  }
}
