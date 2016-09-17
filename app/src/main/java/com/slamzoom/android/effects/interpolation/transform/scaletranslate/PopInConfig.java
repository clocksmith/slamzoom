package com.slamzoom.android.effects.interpolation.transform.scaletranslate;

import com.slamzoom.android.effects.EffectConfig;
import com.slamzoom.android.effects.interpolation.transform.TransformInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.LinearInterpolator;

/**
 * Created by clocksmith on 8/31/16.
 */
public class PopInConfig extends EffectConfig {
  @Override
  public Interpolator getScaleInterpolator() {
    return new Interpolator() {
      @Override
      public float getInterpolation(float t) {
        return getValue(t);
      }

      @Override
      public float getValue(float t) {
        return t;
      }
    };
  }

  @Override
  public Interpolator getXInterpolator() {
    return new XAndYInterpolator();
  }

  @Override
  public Interpolator getYInterpolator() {
    return new XAndYInterpolator();
  }

  private class XAndYInterpolator extends Interpolator {
    @Override
    public float getInterpolation(float t) {
      return getValue(t);
    }

    @Override
    public float getValue(float t) {
      return new LinearInterpolator(t / 2, 0).getInterpolation(t);
    }
  }
}
