package com.slamzoom.android.interpolators;

/**
 * Created by clocksmith on 3/17/16.
 *
 * Interpolator that returns the same value for all values of t. It "stands still".
 * This is useful when applying multiple effects on partial durations of the effect.
 */
public class ConstantInterpolator extends Interpolator {
  private float mConstant;

  public ConstantInterpolator(float constant) {
    super();
    mConstant = constant;
  }

  @Override
  public float getValue(float t) {
    return mConstant;
  }
}
