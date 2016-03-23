package com.slamzoom.android.interpolate.filter;

import com.slamzoom.android.interpolate.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;

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