package com.slamzoom.android.interpolate.base;

/**
 * Created by antrob on 2/24/16.
 */
public class BinomialInterpolator extends BaseInterpolator {
  private float c1;
  private float c2;
  private float e1;
  private float e2;

  public BinomialInterpolator(
      float startExponent,
      float endExponent,
      float coefficientRatio) {
    super();
    this.c1 = coefficientRatio / (coefficientRatio + 1);
    this.c2 = 1 - this.c1;
    this.e1 = startExponent;
    this.e2 = endExponent;
  }

  @Override
  protected float getValue(float input) {
    return (float) (c1 * Math.pow(input, e1) + c2 * Math.pow(input, e2));
  }
}
