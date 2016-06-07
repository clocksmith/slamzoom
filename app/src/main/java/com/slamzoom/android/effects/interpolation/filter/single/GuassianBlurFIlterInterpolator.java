package com.slamzoom.android.effects.interpolation.filter.single;

import com.slamzoom.android.effects.interpolation.filter.BaseGuassianBlurFilterInterpolator;
import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 6/5/16.
 */
public class GuassianBlurFilterInterpolator extends BaseGuassianBlurFilterInterpolator {
  public GuassianBlurFilterInterpolator() {
    this(null);
  }

  public GuassianBlurFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  public float getBlurSize() {
    return mBlurCalculator.getValueFromInterpolation();
  }
}
