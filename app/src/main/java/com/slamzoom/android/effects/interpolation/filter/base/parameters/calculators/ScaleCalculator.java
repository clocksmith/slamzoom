package com.slamzoom.android.effects.interpolation.filter.base.parameters.calculators;

import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;

/**
 * Created by clocksmith on 6/4/16.
 */
public class ScaleCalculator extends BaseCalculator {
  private float mBaseScale;

  public ScaleCalculator(FilterInterpolator filterInterpolator, float baseScale) {
    super(filterInterpolator);
    mBaseScale = baseScale;
  }

  public float getBaseScale() {
    return mBaseScale;
  }

  public float getScaleFromInterpolation() {
    return mBaseScale * getInterpolationValue();
  }
}
