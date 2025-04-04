package com.slamzoom.android.interpolators;

import com.slamzoom.android.effects.interpolation.transform.scale.EaseInSlamHardInterpolator;

/**
 * Created by clocksmith on 2/24/16.
 *
 * Interpolator in the form c1 * t^e1 + c2 * t^e2 where c1 + c2 = 1.
 * This is useful for effects that need to have different beginning and end behavior.
 *
 * {@link EaseInSlamHardInterpolator}
 */
public class BinomialInterpolator extends Interpolator {
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
  public float getValue(float t) {
    return (float) (c1 * Math.pow(t, e1) + c2 * Math.pow(t, e2));
  }
}
