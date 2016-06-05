package com.slamzoom.android.effects.interpolation.filter.base.parameters.calculators;

import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;

/**
 * Created by clocksmith on 6/4/16.
 */
public class ExposureCalculator extends BaseCalculator {
  // TODO(clocksmtih): Add max and base if needed.
  private float mBaseExposure = 0;
  private float mBaseMinExposure = -10;

  public ExposureCalculator(FilterInterpolator filterInterpolator) {
    super(filterInterpolator);
  }

  public float getBaseExposure() {
    return mBaseExposure;
  }


  public float getBaseMinExposure() {
    return mBaseMinExposure;
  }


  public float getUnderExposureFromInterpolation() {
    return mBaseMinExposure * getInterpolationValue();
  }
}