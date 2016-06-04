package com.slamzoom.android.effects.interpolation.filter.single;

import com.slamzoom.android.effects.interpolation.filter.base.BaseExposureFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 3/31/16.
 */
public class UnderExposeFilterInterpolator extends BaseExposureFilterInterpolator {
  protected static final float BASE_MIN_EXPSURE = -10;

  public UnderExposeFilterInterpolator() {
    this(null);
  }

  public UnderExposeFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  public float getExposure() {
    return BASE_MIN_EXPSURE * getInterpolationValue();
  }
}
