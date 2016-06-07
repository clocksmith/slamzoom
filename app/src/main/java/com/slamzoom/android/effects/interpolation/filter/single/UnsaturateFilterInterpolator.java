package com.slamzoom.android.effects.interpolation.filter.single;

import com.slamzoom.android.effects.interpolation.filter.BaseSaturationFilterInterpolator;
import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 3/21/16.
 */
public class UnsaturateFilterInterpolator extends BaseSaturationFilterInterpolator {
  public UnsaturateFilterInterpolator() {
    this(null);
  }

  public UnsaturateFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  public float getSaturation() {
    return mSaturationCalculator.getValueFromInterpolationCompliment();
  }
}
