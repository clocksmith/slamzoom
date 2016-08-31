package com.slamzoom.android.effects.interpolation.transform.translate;

import com.slamzoom.android.effects.interpolation.transform.TranslateInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 3/14/16.
 */
public class BaseShakeInterpolatorProvider implements TranslateInterpolatorProvider {
  private static final float PHASE_SHIFT_MULTIPLIER = 1.3f;

  private float mIntensity;
  private int mFrequency;
  private int mShiftedFrequency;

  // intesnity and frequency should be 1-100
  public BaseShakeInterpolatorProvider(int intensity, int frequency) {
    mIntensity = (float) intensity / 100;
    mFrequency = frequency;
    mShiftedFrequency = Math.round(mFrequency * PHASE_SHIFT_MULTIPLIER);
  }

  @Override
  public Interpolator getXInterpolator() {
    return new Interpolator() {
      @Override
      public float getValue(float t) {
        return (float) (
            mIntensity * Math.sin(mFrequency * 2 * Math.PI * t) +
            mIntensity * Math.cos(mShiftedFrequency * 2 * Math.PI * t));
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new Interpolator() {
      @Override
      public float getValue(float t) {
        return (float) (
            mIntensity * Math.sin(mShiftedFrequency * 2 * Math.PI * t) +
            mIntensity * Math.cos(mFrequency * 2 * Math.PI * t));
      }
    };
  }
}
