package com.slamzoom.android.interpolaters.combo.scaletranslate;

import com.slamzoom.android.interpolaters.base.Interpolator;
import com.slamzoom.android.interpolaters.combo.ScaleAndTranslateInterpolatorProvider;
import com.slamzoom.android.interpolaters.single.EaseInSlamHardInterpolator;

/**
 * Created by clocksmith on 3/16/16.
 */
public class FlushInterpolatorProvider implements ScaleAndTranslateInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new EaseInSlamHardInterpolator();
  }

  @Override
  public Interpolator getXInterpolator() {
    return new Interpolator() {
      @Override
      protected float getValue(float input) {
        float newInput = (float) Math.pow(1 - input, 0.5);
        return  (float) (4 * newInput * Math.cos(32 * Math.PI * newInput));
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new Interpolator() {
      @Override
      protected float getValue(float input) {
        float newInput = (float) Math.pow(1 - input, 0.5);
        return (float) (4 * newInput * Math.sin(32 * Math.PI * newInput));
      }
    };
  }
}
