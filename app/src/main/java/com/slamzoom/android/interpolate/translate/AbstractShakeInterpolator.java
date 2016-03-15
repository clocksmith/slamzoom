package com.slamzoom.android.interpolate.translate;

import com.slamzoom.android.interpolate.base.BaseInterpolator;

/**
 * Created by clocksmith on 3/14/16.
 */
public abstract class AbstractShakeInterpolator extends AbstractTranslateInterpolator {
  private static final float PHASE_SHIFT_MULTIPLIER = 1.3f;

  private float mIntensity;
  private int mFrequency;
  private int mShiftedFrequency;

  // intesnity and frequency should be 1-100
  public AbstractShakeInterpolator(int intensity, int frequency) {
    mIntensity = (float) intensity / 100;
    mFrequency = frequency;
    mShiftedFrequency = Math.round(mFrequency * PHASE_SHIFT_MULTIPLIER);
  }

  @Override
  protected BaseInterpolator getXInterpolator() {
    return new BaseInterpolator() {
      @Override
      protected float getValue(float input) {
        return (float) (mIntensity * Math.sin(mFrequency * Math.PI * input) +
            mIntensity * Math.cos(mShiftedFrequency * Math.PI * input));
      }
    };
  }

  @Override
  protected BaseInterpolator getYInterpolator() {
    return new BaseInterpolator() {
      @Override
      protected float getValue(float input) {
        return (float) (mIntensity * Math.sin(mShiftedFrequency * Math.PI * input) +
            mIntensity * Math.cos(mFrequency * Math.PI * input));
      }
    };
  }
}
