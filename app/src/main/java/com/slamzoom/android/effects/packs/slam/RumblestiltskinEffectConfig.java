package com.slamzoom.android.effects.packs.slam;

import com.slamzoom.android.effects.EffectConfig;
import com.slamzoom.android.effects.interpolation.transform.TranslateInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.BaseShakeInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;

/**
 * Created by clocksmith on 6/6/16.
 */
public class RumblestiltskinEffectConfig extends EffectConfig {
  private static final float PERCENT_START = 0.5f;
  private static final Interpolator START_INTENSITY_INTERPOLATOR = LinearSplineInterpolator.newBuilder()
      .withPoint(0, 0)
      .withPoint(PERCENT_START, 3)
      .build();
  private static final TranslateInterpolatorProvider START_SHAKE_IP = getStartShakeIP(START_INTENSITY_INTERPOLATOR, 12);
  private static final TranslateInterpolatorProvider END_SHAKE_IP = new BaseShakeInterpolatorProvider(0.5f, 24);

  @Override
  public String getName() {
    return "rumblestiltskin";
  }

  @Override
  public float getDurationSeconds() {
    return 3;
  }

  @Override
  public float getEndPauseSeconds() {
    return 0;
  }

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

  private static TranslateInterpolatorProvider getStartShakeIP(
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
