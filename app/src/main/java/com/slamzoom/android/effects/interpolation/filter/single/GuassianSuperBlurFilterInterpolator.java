package com.slamzoom.android.effects.interpolation.filter.single;

import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 6/16/16.
 */
public class GuassianSuperBlurFilterInterpolator extends GaussianBlurFilterInterpolator {
  public GuassianSuperBlurFilterInterpolator() {
    this(null);
  }

  public GuassianSuperBlurFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
    mBlurCalculator.setBaseValue(10);
  }
}
