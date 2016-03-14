package com.slamzoom.android.interpolate.base;

/**
 * Created by antrob on 2/24/16.
 */
public class BinomialSingleOutputInterpolator extends SingleOutputInterpolator {
  private double c1;
  private double c2;
  private double e1;
  private double e2;

  public BinomialSingleOutputInterpolator(
      double startExponent,
      double endExponent,
      double coefficientRatio) {
    super();
    this.c1 = coefficientRatio / (coefficientRatio + 1);
    this.c2 = 1 - this.c1;
    this.e1 = startExponent;
    this.e2 = endExponent;
  }

  @Override
  protected double getX(double input) {
    return c1 * Math.pow(input, e1) + c2 * Math.pow(input, e2);
  }
}
