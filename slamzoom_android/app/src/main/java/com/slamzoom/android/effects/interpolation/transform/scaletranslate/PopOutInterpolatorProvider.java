package com.slamzoom.android.effects.interpolation.transform.scaletranslate;

import com.slamzoom.android.effects.interpolation.transform.TransformInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.LinearInterpolator;

/**
 * Created by clocksmith on 8/31/16.
 */
public class PopOutInterpolatorProvider implements TransformInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new Interpolator() {
      @Override
      public float getInterpolation(float t) {
        return getValue(t);
      }

      @Override
      public float getValue(float t) {
        return 1 - t;
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
//      return 0.25f;
      return new LinearInterpolator(0, (1 - t) / 2).getInterpolation(t);
    }
  }
}

