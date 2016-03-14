package com.slamzoom.android.interpolate.multi;

import com.slamzoom.android.interpolate.base.MultiOutputInterpolator;

/**
 * Created by clocksmith on 3/12/16.
 */
public class TarentinoTranslateInterpolator extends MultiOutputInterpolator {

  @Override
  protected double getX1(double input) {
    if (input < 0.2308) {
      return 0.3 * input;
    } else if (input < 0.3){
      return 0.1 / input * Math.cos(Math.PI * 40 * input);
    } else {
      return 0;
    }
  }

  @Override
  protected double getX2(double input) {
    if (input < 0.2308) {
      return 0.3 * input;
    } else if (input < 0.3){
      return 0.2 / input * Math.cos(Math.PI * 40 * input);
    } else {
      return 0;
    }
  }
}
