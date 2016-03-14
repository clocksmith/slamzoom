package com.slamzoom.android.interpolate.single;

import com.slamzoom.android.interpolate.base.SingleOutputInterpolator;

/**
 * Created by clocksmith on 3/12/16.
 */
public class TarantinoScaleInterpolator extends SingleOutputInterpolator {
  @Override
  protected double getX(double input) {
    if (input < 0.2308) {
      return 5 * input;
    } else {
      return 0.1 * input + 0.9;
    }
  }
}
