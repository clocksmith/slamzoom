package com.slamzoom.android.interpolate.combo.scaletranslate;

import com.slamzoom.android.interpolate.base.ConstantInterpolator;
import com.slamzoom.android.interpolate.base.Interpolator;
import com.slamzoom.android.interpolate.single.InAndOutInterpolator;
import com.slamzoom.android.interpolate.combo.ScaleAndTranslateInterpolatorProvider;

/**
 * Created by clocksmith on 3/17/16.
 */
public class WizzyWazzleInterpolatorProvider implements ScaleAndTranslateInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new InAndOutInterpolator();
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
