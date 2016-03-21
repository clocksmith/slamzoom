package com.slamzoom.android.interpolate.combo.translate;

import com.slamzoom.android.interpolate.base.Interpolator;
import com.slamzoom.android.interpolate.combo.TranslateInterpolatorProvider;

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
      protected float getValue(float input) {
        return (float) (mIntensity * Math.sin(mFrequency * Math.PI * input) +
            mIntensity * Math.cos(mShiftedFrequency * Math.PI * input));
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new Interpolator() {
      @Override
      protected float getValue(float input) {
        return (float) (mIntensity * Math.sin(mShiftedFrequency * Math.PI * input) +
            mIntensity * Math.cos(mFrequency * Math.PI * input));
      }
    };
  }
}
