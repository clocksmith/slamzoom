package com.slamzoom.android.interpolate.filter;

import com.slamzoom.android.interpolate.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 3/21/16.
 */
public abstract class FilterInterpolator {
  private Interpolator mInterpolator;

  public FilterInterpolator() {}

  public FilterInterpolator(Interpolator interpolator) {
    mInterpolator = interpolator;
  }

  protected abstract GPUImageFilter getFilter(float interpolationValue);

  public GPUImageFilter getInterpolationFilter(float input) {
    return getFilter(mInterpolator.getInterpolation(input));
  }

  public boolean hasInterpoolator() {
    return mInterpolator != null;
  }

  public void setInterpolator(Interpolator interpolator) {
    mInterpolator = interpolator;
  }
}
