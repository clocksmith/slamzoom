package com.slamzoom.android.effects.interpolation.filter.base;

import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;

/**
 * Created by clocksmith on 6/4/16.
 */
public abstract class BaseSaturationFilterInterpolator extends FilterInterpolator implements HasSaturation {
  protected static final float BASE_SATURATION = 0;

  public BaseSaturationFilterInterpolator() {
    this(null);
  }

  public BaseSaturationFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  protected GPUImageFilter getFilter() {
    return new GPUImageSaturationFilter(getSaturation());
  }

  @Override
  public float getSaturation() {
    return BASE_SATURATION;
  }
}
