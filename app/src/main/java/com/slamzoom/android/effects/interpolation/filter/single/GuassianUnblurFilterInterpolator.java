package com.slamzoom.android.effects.interpolation.filter.single;

import com.slamzoom.android.effects.interpolation.filter.base.RegularFilterInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;

/**
 * Created by clocksmith on 3/21/16.
 */
public class GuassianUnblurFilterInterpolator extends RegularFilterInterpolator {
  public GuassianUnblurFilterInterpolator() {
    super();
  }

  public GuassianUnblurFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  public GPUImageFilter getFilter(float interpolationValue) {
    return new GPUImageGaussianBlurFilter(5 * (1 - interpolationValue));
  }
}
