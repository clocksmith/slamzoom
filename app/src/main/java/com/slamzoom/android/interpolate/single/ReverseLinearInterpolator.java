package com.slamzoom.android.interpolate.single;

import com.slamzoom.android.interpolate.base.SingleOutputInterpolator;

/**
 * Created by clocksmith on 3/12/16.
 */
public class ReverseLinearInterpolator extends SingleOutputInterpolator {
  @Override
  protected double getX(double input) {
    if (input < 0.5) {
      return input;
    } else {
      return 1 - input;
    }
  }
}
