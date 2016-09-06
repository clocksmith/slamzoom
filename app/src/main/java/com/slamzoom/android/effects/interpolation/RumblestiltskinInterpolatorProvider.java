package com.slamzoom.android.effects.interpolation;

import com.slamzoom.android.effects.interpolation.EffectInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.TransformInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.TranslateInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.BaseShakeInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;

/**
 * Created by clocksmith on 6/6/16.
 */
public class RumblestiltskinInterpolatorProvider extends EffectInterpolatorProvider {
  private static final float PERCENT_START = 0.5f;

  private static final Interpolator START_INTENSITY_INTERPOLATOR = LinearSplineInterpolator.newBuilder()
      .withPoint(0, 0)
      .withPoint(PERCENT_START, 3)
      .build();

  private static final TranslateInterpolatorProvider START_SHAKE_IP =
      getStartShakeInterpolationProvider(START_INTENSITY_INTERPOLATOR, 12);
  private static final TranslateInterpolatorProvider END_SHAKE_IP = new BaseShakeInterpolatorProvider(0.5f, 24);

  @Override
  public Interpolator getScaleInterpolator() {
    return LinearSplineInterpolator.newBuilder()
        .withPoint(0, 0)
        .withPoint(PERCENT_START, 0)
        .withPoint(PERCENT_START + 0.0001f, 0.5f)
        .withPoint(1f, 1f)
        .build();
  }

  @Override
  public Interpolator getXInterpolator() {
    return new Interpolator() {
      @Override
      public float getValue(float t) {
        if (t < PERCENT_START) {
          return START_SHAKE_IP.getXInterpolator().getValue(t);
        } else {
          return END_SHAKE_IP.getXInterpolator().getValue(t);
        }
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new Interpolator() {
      @Override
      public float getValue(float t) {
        if (t < PERCENT_START) {
          return START_SHAKE_IP.getYInterpolator().getValue(t);
        } else {
          return END_SHAKE_IP.getYInterpolator().getValue(t);
        }
      }
    };
  }

  private static TranslateInterpolatorProvider getStartShakeInterpolationProvider(
      final Interpolator intensityInterpolator, final int frequency) {
    return new TranslateInterpolatorProvider() {
      @Override
      public Interpolator getXInterpolator() {
        return new Interpolator() {
          @Override
          public float getValue(float t) {
            return new BaseShakeInterpolatorProvider(intensityInterpolator.getValue(t), frequency)
                .getXInterpolator().getValue(t);
          }
        };
      }

      @Override
      public Interpolator getYInterpolator() {
        return new Interpolator() {
          @Override
          public float getValue(float t) {
            return new BaseShakeInterpolatorProvider(intensityInterpolator.getValue(t), frequency)
                .getYInterpolator().getValue(t);
          }
        };
      }
    };
  }
}
