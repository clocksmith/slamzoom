package com.slamzoom.android.interpolators.base;

/**
 * Created by antrob on 2/24/16.
 */
public class MonomialInterpolator extends Interpolator {
  private float exponent;

  public MonomialInterpolator(float exponent) {
    super();
    this.exponent = exponent;
  }

  @Override
  protected float getValue(float percent) {
    return (float) Math.pow(percent, exponent);
  }
}
