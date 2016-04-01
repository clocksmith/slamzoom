package com.slamzoom.android.interpolaters.filter;

import com.slamzoom.android.interpolaters.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 3/31/16.
 */
public class ExposureFilterInterpolator extends FilterInterpolator {
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
