package com.slamzoom.android.effects.interpolation.filter.single;

import com.slamzoom.android.effects.interpolation.filter.BaseGuassianBlurFilterInterpolator;
import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 3/21/16.
 */
public class GaussianUnblurFilterInterpolator extends BaseGuassianBlurFilterInterpolator {
  public GaussianUnblurFilterInterpolator() {
    this(null);
  }

  public GaussianUnblurFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  public float getBlurSize() {
    return mBlurCalculator.getValueFromInterpolationCompliment();
  }
}
