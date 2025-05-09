package com.slamzoom.android.effects.interpolation.filter.calculators;

import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;

/**
 * Created by clocksmith on 6/4/16.
 */
public class ExposureCalculator extends BaseCalculator {
  // TODO(clocksmtih): Add max and base if needed.
  private float mBaseExposure = 0;
  private float mBaseMinExposure = -10;
  private float mBaseMaxExposure = 10;

  public ExposureCalculator(FilterInterpolator filterInterpolator) {
    super(filterInterpolator);
  }

  public float getBaseExposure() {
    return mBaseExposure;
  }

  public float getUnderExposureFromInterpolation() {
    return mBaseMinExposure * getInterpolationValue();
  }

  public float getOverExposureFromInterpolation() {
    return mBaseMaxExposure * getInterpolationValue();
  }
}