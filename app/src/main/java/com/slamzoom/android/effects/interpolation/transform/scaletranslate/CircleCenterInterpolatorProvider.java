package com.slamzoom.android.effects.interpolation.transform.scaletranslate;

import com.slamzoom.android.effects.interpolation.transform.ScaleAndTranslateInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.LinearInterpolator;

/**
 * Created by clocksmith on 3/16/16.
 */
public class CircleCenterInterpolatorProvider implements ScaleAndTranslateInterpolatorProvider {
  private static final float RADIUS = 0.6f;
  private static final float PERCENT_IN_CIRCLE = 0.7f;

  @Override
  public Interpolator getScaleInterpolator() {
    return new LinearInterpolator();
  }

  @Override
  public Interpolator getXInterpolator() {
    return new Interpolator() {
      @Override
      protected float getRangePercent(float t) {
        if (t < PERCENT_IN_CIRCLE) {
          return (float) (RADIUS * Math.sin(2 * Math.PI * t / PERCENT_IN_CIRCLE));
        } else {
          return 0;
        }
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new Interpolator() {
      @Override
      protected float getRangePercent(float t) {
        if (t < PERCENT_IN_CIRCLE) {
          return (float) (RADIUS * Math.cos(2 * Math.PI * t / PERCENT_IN_CIRCLE));
        } else {
          return 0;
        }
      }
    };
  }
}
