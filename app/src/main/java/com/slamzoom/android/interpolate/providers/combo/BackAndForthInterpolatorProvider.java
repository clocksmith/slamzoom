package com.slamzoom.android.interpolate.providers.combo;

import com.slamzoom.android.interpolate.ConstantInterpolator;
import com.slamzoom.android.interpolate.IdentityInterpolator;
import com.slamzoom.android.interpolate.Interpolator;

/**
 * Created by clocksmith on 3/17/16.
 */
public class BackAndForthInterpolatorProvider implements ScaleAndTranslateInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new IdentityInterpolator();
  }

  @Override
  public Interpolator getXInterpolator() {
    return new Interpolator() {
      @Override
      protected float getValue(float input) {
        float newInput = 1 - input;
        return (float) (newInput * Math.cos(6 * Math.PI * newInput));
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new ConstantInterpolator(0);
  }
}
