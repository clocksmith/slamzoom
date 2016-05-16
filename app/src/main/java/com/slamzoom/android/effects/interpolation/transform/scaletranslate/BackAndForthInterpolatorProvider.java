package com.slamzoom.android.effects.interpolation.transform.scaletranslate;

import com.slamzoom.android.interpolators.base.ConstantInterpolator;
import com.slamzoom.android.interpolators.effect.IdentityInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;
import com.slamzoom.android.effects.interpolation.transform.base.ScaleAndTranslateInterpolatorProvider;

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
      protected float getValue(float percent) {
        float newInput = 1 - percent;
        return (float) (newInput * Math.cos(6 * Math.PI * newInput));
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new ConstantInterpolator(0);
  }
}
