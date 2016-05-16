package com.slamzoom.android.effects.interpolation.transform.scaletranslate;

import com.slamzoom.android.interpolators.base.ConstantInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;
import com.slamzoom.android.interpolators.effect.HalfInAndOutInterpolator;
import com.slamzoom.android.effects.interpolation.transform.base.ScaleAndTranslateInterpolatorProvider;

/**
 * Created by clocksmith on 3/17/16.
 */
public class WizzyWazzleInterpolatorProvider implements ScaleAndTranslateInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new HalfInAndOutInterpolator();
  }

  @Override
  public Interpolator getXInterpolator() {
    return new Interpolator() {
      @Override
      protected float getValue(float percent) {
//        float newInput = input < 0.5 ? 1 - input : input;
        return (float) Math.cos(8 * Math.PI * percent);
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new ConstantInterpolator(0);
  }
}
