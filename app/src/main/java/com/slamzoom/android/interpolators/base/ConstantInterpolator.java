package com.slamzoom.android.interpolators.base;

/**
 * Created by clocksmith on 3/17/16.
 */
public class ConstantInterpolator extends Interpolator {
  private float mConstant;
  public ConstantInterpolator(float constant) {
    super();
    mConstant = constant;
  }

  @Override
  protected float getValue(float percent) {
    return mConstant;
  }
}
