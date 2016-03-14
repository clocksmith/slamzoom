package com.slamzoom.android.interpolate.base;

/**
 * Created by clocksmith on 3/14/16.
 */
public class BaseShakeInterpolator extends MultiOutputInterpolator {
  private static final float PHASE_SHIFT_MULTIPLIER = 1.3f;

  private float mIntensity;
  private int mFrequency;
  private int mShiftedFrequency;

  // intesnity and frequency should be 1-100
  public BaseShakeInterpolator(int intensity, int frequency) {
    mIntensity = (float) intensity / 100;
    mFrequency = frequency;
    mShiftedFrequency = Math.round(mFrequency * PHASE_SHIFT_MULTIPLIER);
  }

  @Override
  protected double getX1(double input) {
    return  mIntensity * Math.sin(mFrequency * Math.PI * input) +
        mIntensity * Math.cos(mShiftedFrequency * Math.PI * input);
  }

  @Override
  protected double getX2(double input) {
    return  mIntensity * Math.sin(mShiftedFrequency * Math.PI * input) +
        mIntensity * Math.cos(mFrequency * Math.PI * input);
  }
}
