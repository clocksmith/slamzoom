package com.slamzoom.android.effects.interpolation.filter.base;

import com.slamzoom.android.interpolators.base.Interpolator;
import com.slamzoom.android.interpolators.base.InterpolatorHolder;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 4/22/16.
 */
public abstract class RegularFilterInterpolator extends FilterInterpolator {
  public RegularFilterInterpolator() {
    super();
  }

  public RegularFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  public GPUImageFilter getInterpolationFilter(float input) {
    return getFilter(mInterpolator.getInterpolation(input));
  }

  protected abstract GPUImageFilter getFilter(float interpolationValue);
}
