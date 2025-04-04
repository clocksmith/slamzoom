package com.slamzoom.android.effects.interpolation.filter.single;

import com.slamzoom.android.effects.interpolation.filter.BaseExposureFilterInterpolator;
import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 6/14/16.
 */
public class OverExposeFilterInterpolator extends BaseExposureFilterInterpolator {
  public OverExposeFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  public float getExposure() {
    return mExposureCalcualtor.getOverExposureFromInterpolation();
  }
}
