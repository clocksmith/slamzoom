package com.slamzoom.android.interpolate.multi;

import com.slamzoom.android.interpolate.base.MultiOutputInterpolator;

/**
 * Created by clocksmith on 3/11/16.
 */
public class ShakeInterpolator extends MultiOutputInterpolator {
  @Override
  protected double getX1(double input) {
    return Math.sin(25 * Math.PI * input);
  }

  @Override
  protected double getX2(double input) {
    return Math.sin(15 * Math.PI * input);
  }
}
