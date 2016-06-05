package com.slamzoom.android.effects.interpolation.filter.base;

import com.slamzoom.android.effects.interpolation.filter.base.parameters.HasBlurSize;
import com.slamzoom.android.effects.interpolation.filter.base.parameters.calculators.FloatCalculator;
import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;

/**
 * Created by clocksmith on 6/4/16.
 */
public abstract class BaseGuassianBlurFilterInterpolator extends FilterInterpolator implements HasBlurSize {
  protected static final float BASE_BLUR_SIZE = 5;

  protected FloatCalculator mBlurCalculator;

  public BaseGuassianBlurFilterInterpolator() {
    this(null);
  }

  public BaseGuassianBlurFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
    mBlurCalculator = new FloatCalculator(this, BASE_BLUR_SIZE);
  }

  @Override
  protected GPUImageFilter getFilter() {
    return new GPUImageGaussianBlurFilter(getBlurSize());
  }

  @Override
  public float getBlurSize() {
    return mBlurCalculator.getBaseValue();
  }
}
