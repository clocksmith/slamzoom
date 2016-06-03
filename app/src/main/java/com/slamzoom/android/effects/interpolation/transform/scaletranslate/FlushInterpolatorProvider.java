package com.slamzoom.android.effects.interpolation.transform.scaletranslate;

import com.slamzoom.android.interpolators.base.Interpolator;
import com.slamzoom.android.effects.interpolation.transform.base.ScaleAndTranslateInterpolatorProvider;
import com.slamzoom.android.interpolators.effect.EaseInSlamHardInterpolator;

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
      protected float getRangePercent(float t) {
        float newInput = (float) Math.pow(1 - t, 0.5);
        return  (float) (4 * newInput * Math.cos(32 * Math.PI * newInput));
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new Interpolator() {
      @Override
      protected float getRangePercent(float t) {
        float newInput = (float) Math.pow(1 - t, 0.5);
        return (float) (4 * newInput * Math.sin(32 * Math.PI * newInput));
      }
    };
  }
}
