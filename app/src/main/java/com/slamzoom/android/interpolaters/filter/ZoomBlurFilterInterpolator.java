package com.slamzoom.android.interpolaters.filter;

import com.slamzoom.android.interpolaters.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 3/21/16.
 */
public class ZoomBlurFilterInterpolator extends FilterInterpolator {
  public ZoomBlurFilterInterpolator(Interpolator interpolator) {
    super((interpolator));
  }

  @Override
  protected GPUImageFilter getFilter(float interpolationValue) {
    return new GPUImageZoomBlurFilter(5 * interpolationValue);
  }
}