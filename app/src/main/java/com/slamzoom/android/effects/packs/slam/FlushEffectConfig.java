package com.slamzoom.android.effects.packs.slam;

import com.slamzoom.android.effects.EffectConfig;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.effects.interpolation.transform.scale.EaseInSlamHardInterpolator;

/**
 * Created by clocksmith on 3/16/16.
 */
public class FlushEffectConfig extends EffectConfig {
  private static final float RADIUS = 0.15f;
  private static final float FREQUENCY = 4;
  private static final float EXPONENT_0 = 2f;
  private static final float EXPONENT_1 = 0.2f;
  private static final float EXPONENT_2 = 3;

  @Override
  public String getName() {
    return "flush";
  }

  @Override
  public float getEndPauseSeconds() {
    return 1;
  }

  @Override
  public Interpolator getScaleInterpolator() {
    return new EaseInSlamHardInterpolator();
  }

  @Override
  public Interpolator getXInterpolator() {
    return new Interpolator() {
      @Override
      public float getValue(float t) {
        if (t == 0) {
          return 0;
        }

        double newT = 1 - Math.pow(t, EXPONENT_0);
        return (float) (RADIUS * Math.pow(newT, EXPONENT_1) *
            Math.cos(FREQUENCY * 2 * Math.PI * Math.pow(newT, EXPONENT_2)));
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new Interpolator() {
      @Override
      public float getValue(float t) {
        double newT = 1 - Math.pow(t, EXPONENT_0);
        return (float) (RADIUS * Math.pow(newT, EXPONENT_1) *
            Math.sin(FREQUENCY * 2 * Math.PI * Math.pow(newT, EXPONENT_2)));
      }
    };
  }
}
