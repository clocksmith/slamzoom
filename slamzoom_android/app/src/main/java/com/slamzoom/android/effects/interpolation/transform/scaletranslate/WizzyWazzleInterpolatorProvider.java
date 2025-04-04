package com.slamzoom.android.effects.interpolation.transform.scaletranslate;

import com.slamzoom.android.effects.interpolation.transform.TransformInterpolatorProvider;
import com.slamzoom.android.interpolators.ConstantInterpolator;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.effects.interpolation.transform.scale.HalfInAndOutInterpolator;

/**
 * Created by clocksmith on 3/17/16.
 */
public class WizzyWazzleInterpolatorProvider implements TransformInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new HalfInAndOutInterpolator();
  }

  @Override
  public Interpolator getXInterpolator() {
    return new Interpolator() {
      @Override
      public float getValue(float t) {
//        float newInput = input < 0.5 ? 1 - input : input;
        return (float) Math.cos(8 * Math.PI * t);
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new ConstantInterpolator(0);
  }
}
