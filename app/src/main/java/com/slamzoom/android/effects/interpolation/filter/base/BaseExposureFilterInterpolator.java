package com.slamzoom.android.effects.interpolation.filter.base;

import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;

/**
 * Created by clocksmith on 6/4/16.
 */
public class BaseExposureFilterInterpolator extends FilterInterpolator implements HasExposure {
  protected static final float BASE_EXPOSURE = 0;

  public BaseExposureFilterInterpolator() {
    this(null);
  }

  public BaseExposureFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  protected GPUImageFilter getFilter() {
    return new GPUImageExposureFilter(getExposure());
  }

  @Override
  public float getExposure() {
    return BASE_EXPOSURE;
  }
}
