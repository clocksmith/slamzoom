package com.slamzoom.android.effects.interpolation.transform.scaletranslate;

import com.slamzoom.android.effects.interpolation.transform.TransformInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.LinearInterpolator;

/**
 * Created by clocksmith on 3/16/16.
 */
public class ZigZagInterpolatorProvider implements TransformInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new LinearInterpolator();
  }

  @Override
  public Interpolator getXInterpolator() {
    return new Interpolator() {
      @Override
      public float getValue(float t) {
        float newInput = 1 - t;
        return (float) (newInput * Math.pow(Math.cos(12 * Math.PI * newInput), 2));
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new Interpolator() {
      @Override
      public float getValue(float t) {
        float newInput = 1 - t;
        return (float) (newInput * Math.pow(Math.sin(12 * Math.PI * newInput), 2));
      }
    };
  }
}
