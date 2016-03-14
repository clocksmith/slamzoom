package com.slamzoom.android.interpolate.base;

/**
 * Created by antrob on 2/24/16.
 */
public class MonomialSingleOutputInterpolator extends SingleOutputInterpolator {
  private double exponent;

  public MonomialSingleOutputInterpolator(double exponent) {
    super();
    this.exponent = exponent;
  }

  @Override
  protected double getX(double input) {
    return Math.pow(input, exponent);
  }
}
