package com.slamzoom.android.effects.interpolation.transform.scaletranslate;

import com.slamzoom.android.effects.interpolation.transform.ScaleAndTranslateInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.TranslateInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.BaseShakeInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 6/6/16.
 */
public class ShakeSwitchInterpolatorProvider implements ScaleAndTranslateInterpolatorProvider {
  private static final float PERCENT_IN_CIRCLE = 0.5f;
  private static final TranslateInterpolatorProvider CIRCLE_SHAKE_IP = new BaseShakeInterpolatorProvider(6, 12);
  private static final TranslateInterpolatorProvider END_SHAKE_IP = new BaseShakeInterpolatorProvider(3, 24);

  @Override
  public Interpolator getScaleInterpolator() {
    return new Interpolator() {
      @Override
      protected float getValue(float t) {
        if (t < PERCENT_IN_CIRCLE) {
          return 0;
        } else {
          return 1;
        }
      }
    };
  }

  @Override
  public Interpolator getXInterpolator() {
    return new Interpolator() {
      @Override
      protected float getValue(float t) {
        if (t < PERCENT_IN_CIRCLE) {
          return CIRCLE_SHAKE_IP.getXInterpolator().getInterpolation(t);
        } else {
          return END_SHAKE_IP.getXInterpolator().getInterpolation(t);
        }
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new Interpolator() {
      @Override
      protected float getValue(float t) {
        if (t < PERCENT_IN_CIRCLE) {
          return CIRCLE_SHAKE_IP.getYInterpolator().getInterpolation(t);
        } else {
          return END_SHAKE_IP.getYInterpolator().getInterpolation(t);
        }
      }
    };
  }
}
