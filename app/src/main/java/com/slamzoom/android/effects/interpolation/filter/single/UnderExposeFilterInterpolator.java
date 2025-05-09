package com.slamzoom.android.effects.interpolation.filter.single;

import com.slamzoom.android.effects.interpolation.filter.BaseExposureFilterInterpolator;
import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 3/31/16.
 */
public class UnderExposeFilterInterpolator extends BaseExposureFilterInterpolator {
  public UnderExposeFilterInterpolator() {
    this(null);
  }

  public UnderExposeFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  public float getExposure() {
    return mExposureCalcualtor.getUnderExposureFromInterpolation();
  }
}
