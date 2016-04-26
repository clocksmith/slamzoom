package com.slamzoom.android.effects.interpolation.filter.single;

import com.slamzoom.android.effects.interpolation.filter.base.RegularFilterInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 3/31/16.
 */
public class ExposureFilterInterpolator extends RegularFilterInterpolator {
  public ExposureFilterInterpolator() {
    super();
  }

  public ExposureFilterInterpolator(Interpolator interpolator) {
    super((interpolator));
  }

  @Override
  protected GPUImageFilter getFilter(float interpolationValue) {
    return new GPUImageExposureFilter(-10 * interpolationValue);
  }
}
