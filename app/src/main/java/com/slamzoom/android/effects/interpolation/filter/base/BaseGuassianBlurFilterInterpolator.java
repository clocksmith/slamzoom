package com.slamzoom.android.effects.interpolation.filter.base;

import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;

/**
 * Created by clocksmith on 6/4/16.
 */
public abstract class BaseGuassianBlurFilterInterpolator extends FilterInterpolator implements HasBlurSize {
  protected static final float BASE_BLUR_SIZE = 5;

  public BaseGuassianBlurFilterInterpolator() {
    this(null);
  }

  public BaseGuassianBlurFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  protected GPUImageFilter getFilter() {
    return new GPUImageGaussianBlurFilter(getBlurSize());
  }

  @Override
  public float getBlurSize() {
    return BASE_BLUR_SIZE;
  }
}
