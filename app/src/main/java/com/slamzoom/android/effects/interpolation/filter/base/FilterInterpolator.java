package com.slamzoom.android.effects.interpolation.filter.base;

import com.slamzoom.android.interpolators.base.Interpolator;
import com.slamzoom.android.interpolators.base.InterpolatorHolder;

/**
 * Created by clocksmith on 4/25/16.
 *
 * marker abstract class
 */
public abstract class FilterInterpolator extends InterpolatorHolder {
  public FilterInterpolator() {}

  public FilterInterpolator(Interpolator interpolator) {
   super(interpolator);
  }
}
