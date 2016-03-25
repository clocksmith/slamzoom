package com.slamzoom.android.interpolaters.combo.scaletranslate;

import com.slamzoom.android.interpolaters.single.IdentityInterpolator;
import com.slamzoom.android.interpolaters.base.Interpolator;
import com.slamzoom.android.interpolaters.combo.ScaleAndTranslateInterpolatorProvider;

/**
 * Created by clocksmith on 3/16/16.
 */
public class ZigZagInterpolatorProvider implements ScaleAndTranslateInterpolatorProvider {
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
        return (float) (newInput * Math.pow(Math.cos(12 * Math.PI * newInput), 2));
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new Interpolator() {
      @Override
      protected float getValue(float input) {
        float newInput = 1 - input;
        return (float) (newInput * Math.pow(Math.sin(12 * Math.PI * newInput), 2));
      }
    };
  }
}
