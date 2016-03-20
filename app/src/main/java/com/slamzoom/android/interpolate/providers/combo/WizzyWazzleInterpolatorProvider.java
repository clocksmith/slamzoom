package com.slamzoom.android.interpolate.providers.combo;

import com.slamzoom.android.interpolate.ConstantInterpolator;
import com.slamzoom.android.interpolate.Interpolator;
import com.slamzoom.android.interpolate.ReverseIdentityInterpolator;

/**
 * Created by clocksmith on 3/17/16.
 */
public class WizzyWazzleInterpolatorProvider implements ScaleAndTranslateInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new ReverseIdentityInterpolator();
  }

  @Override
  public Interpolator getXInterpolator() {
    return new Interpolator() {
      @Override
      protected float getValue(float input) {
//        float newInput = input < 0.5 ? 1 - input : input;
        return (float) Math.cos(8 * Math.PI * input);
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new ConstantInterpolator(0);
  }
}
