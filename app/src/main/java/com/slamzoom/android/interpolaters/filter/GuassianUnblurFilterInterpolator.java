package com.slamzoom.android.interpolaters.filter;

import com.slamzoom.android.interpolaters.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;

/**
 * Created by clocksmith on 3/21/16.
 */
public class GuassianUnblurFilterInterpolator extends FilterInterpolator {
  public GuassianUnblurFilterInterpolator() {
    super();
  }

  public GuassianUnblurFilterInterpolator(Interpolator interpolator) {
    super((interpolator));
  }

  @Override
  public GPUImageFilter getFilter(float interpolationValue) {
    return new GPUImageGaussianBlurFilter(5 * (1 - interpolationValue));
  }
}
