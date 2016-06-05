package com.slamzoom.android.effects.interpolation.filter.base;

import com.slamzoom.android.effects.interpolation.filter.base.parameters.HasSaturation;
import com.slamzoom.android.effects.interpolation.filter.base.parameters.calculators.FloatCalculator;
import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;

/**
 * Created by clocksmith on 6/4/16.
 */
public abstract class BaseSaturationFilterInterpolator extends FilterInterpolator implements HasSaturation {
  protected static final float BASE_SATURATION = 1f;

  protected FloatCalculator mSaturationCalculator;

  public BaseSaturationFilterInterpolator() {
    this(null);
  }

  public BaseSaturationFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
    mSaturationCalculator = new FloatCalculator(this, BASE_SATURATION);
  }

  @Override
  protected GPUImageFilter getFilter() {
    return new GPUImageSaturationFilter(getSaturation());
  }

  @Override
  public float getSaturation() {
    return mSaturationCalculator.getBaseValue();
  }
}
