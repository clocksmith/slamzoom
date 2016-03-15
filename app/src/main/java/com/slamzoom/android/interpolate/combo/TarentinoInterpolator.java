package com.slamzoom.android.interpolate.combo;

import com.slamzoom.android.interpolate.base.CubicSplineInterpolator;

/**
 * Created by clocksmith on 3/12/16.
 */
public class TarentinoInterpolator extends CubicSplineInterpolator {

  @Override
  protected float getX1(float input) {
    if (input < 0.2308) {
      return 0.3f * input;
    } else if (input < 0.3){
      return (float) (0.1 / input * Math.cos(Math.PI * 40 * input));
    } else {
      return 0;
    }
  }

  @Override
  protected float getX2(float input) {
    if (input < 0.2308) {
      return 0.3f * input;
    } else if (input < 0.3){
      return (float) (0.2 / input * Math.cos(Math.PI * 40 * input));
    } else {
      return 0;
    }
  }
}
