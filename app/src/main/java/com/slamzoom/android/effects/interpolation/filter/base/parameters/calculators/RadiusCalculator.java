package com.slamzoom.android.effects.interpolation.filter.base.parameters.calculators;

import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;

/**
 * Created by clocksmith on 6/4/16.
 */
public class RadiusCalculator extends BaseCalculator {
  private float mBaseRadius;

  public RadiusCalculator(FilterInterpolator filterInterpolator, float baseRadius, ) {
    super(filterInterpolator);
    mBaseRadius = baseRadius;
  }

  public float getBaseRadius() {
    return mBaseRadius;
  }

  public float getRadiusFromInterpolation() {
    return mBaseRadius * getInterpolationValue();
  }
}
