package com.slamzoom.android.effects.interpolation.transform.scaletranslate;

import com.slamzoom.android.interpolators.effect.IdentityInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;
import com.slamzoom.android.effects.interpolation.transform.base.ScaleAndTranslateInterpolatorProvider;

/**
 * Created by clocksmith on 3/16/16.
 */
public class SpiralInterpolatorProvider implements ScaleAndTranslateInterpolatorProvider {
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
        return (float) (newInput * Math.cos(18 * Math.PI * newInput));
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new Interpolator() {
      @Override
      protected float getValue(float input) {
        float newInput = 1 - input;
        return (float) (newInput * Math.sin(18 * Math.PI * newInput));
      }
    };
  }
}
