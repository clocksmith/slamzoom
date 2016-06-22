package com.slamzoom.android.effects.interpolation.filter;

import com.slamzoom.android.effects.interpolation.filter.calculators.ExposureCalculator;
import com.slamzoom.android.effects.interpolation.filter.parameters.HasExposure;
import com.slamzoom.android.interpolators.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 6/4/16.
 */
public class BaseExposureFilterInterpolator extends FilterInterpolator implements HasExposure {
  protected static final float BASE_EXPOSURE = 0;

  protected ExposureCalculator mExposureCalcualtor;

  public BaseExposureFilterInterpolator() {
    this(null);
  }

  public BaseExposureFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
    mExposureCalcualtor = new ExposureCalculator(this);
  }

  @Override
  protected GPUImageFilter getFilter() {
    return new GPUImageExposureFilter(getExposure());
  }

  @Override
  public float getExposure() {
    return mExposureCalcualtor.getBaseExposure();
  }
}
