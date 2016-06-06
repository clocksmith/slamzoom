package com.slamzoom.android.effects.interpolation.transform.scaletranslate;

import com.slamzoom.android.interpolators.base.Interpolator;
import com.slamzoom.android.effects.interpolation.transform.base.ScaleAndTranslateInterpolatorProvider;
import com.slamzoom.android.interpolators.effect.EaseInSlamHardInterpolator;

/**
 * Created by clocksmith on 3/16/16.
 */
public class FlushInterpolatorProvider implements ScaleAndTranslateInterpolatorProvider {
  private static final float RADIUS = 0.5f;
  private static final float FREQUENCY = 16;
  private static final float EXPONENT_0 = 2f;
  private static final float EXPONENT_1 = 0.2f;
  private static final float EXPONENT_2 = 3;

  @Override
  public Interpolator getScaleInterpolator() {
    return new EaseInSlamHardInterpolator();
  }

  @Override
  public Interpolator getXInterpolator() {
    return new Interpolator() {
      @Override
      protected float getRangePercent(float t) {
        double tt = 1 - Math.pow(t, EXPONENT_0);
        return (float) (RADIUS * Math.pow(tt, EXPONENT_1) *
            Math.cos(FREQUENCY * 2 * Math.PI * Math.pow(tt, EXPONENT_2)));
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new Interpolator() {
      @Override
      protected float getRangePercent(float t) {
        double tt = 1 - Math.pow(t, EXPONENT_0);
        return (float) (RADIUS * Math.pow(tt, EXPONENT_1) *
            Math.sin(FREQUENCY * 2 * Math.PI * Math.pow(tt, EXPONENT_2)));
      }
    };
  }
}
