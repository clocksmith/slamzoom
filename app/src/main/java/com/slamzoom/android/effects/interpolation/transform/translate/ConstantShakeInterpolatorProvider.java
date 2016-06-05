package com.slamzoom.android.effects.interpolation.transform.translate;

import com.slamzoom.android.effects.interpolation.transform.base.TranslateInterpolatorProvider;
import com.slamzoom.android.interpolators.base.Interpolator;

/**
 * Created by clocksmith on 6/5/16.
 */
public class ConstantShakeInterpolatorProvider implements TranslateInterpolatorProvider {
  private static final float PHASE_SHIFT_MULTIPLIER = 1.3f;

  private float mIntensity;
  private int mFrequency;
  private int mShiftedFrequency;

  // intesnity and frequency should be 1-100
  public ConstantShakeInterpolatorProvider(int intensity, int frequency) {
    mIntensity = (float) intensity / 100;
    mFrequency = frequency;
    mShiftedFrequency = Math.round(mFrequency * PHASE_SHIFT_MULTIPLIER);
  }

  @Override
  public Interpolator getXInterpolator() {
    return new Interpolator() {
      @Override
      protected float getRangePercent(float t) {
        return (float) (mIntensity * Math.sin(mFrequency * Math.PI * t) +
            mIntensity * Math.cos(mShiftedFrequency * Math.PI * t));
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new Interpolator() {
      @Override
      protected float getRangePercent(float t) {
        return (float) (mIntensity * Math.sin(mShiftedFrequency * Math.PI * t) +
            mIntensity * Math.cos(mFrequency * Math.PI * t));
      }
    };
  }
}
