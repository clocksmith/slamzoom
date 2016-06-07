package com.slamzoom.android.effects.interpolation.filter;

import android.graphics.PointF;

import com.slamzoom.android.effects.imagefilters.GPUImageZoomBlurFilter;
import com.slamzoom.android.effects.interpolation.filter.parameters.HasBlurSize;
import com.slamzoom.android.effects.interpolation.filter.parameters.HasCenter;
import com.slamzoom.android.effects.interpolation.filter.calculators.CenterCalculator;
import com.slamzoom.android.effects.interpolation.filter.calculators.FloatCalculator;
import com.slamzoom.android.interpolators.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 6/4/16.
 */
public class BaseZoomBlurFilterInterpolator extends FilterInterpolator implements HasBlurSize, HasCenter {
  protected static final float BASE_BLUR_SIZE = 5;

  protected FloatCalculator mBlurCalculator;
  protected CenterCalculator mCenterCalculator;

  public BaseZoomBlurFilterInterpolator() {
    this(null);
  }

  public BaseZoomBlurFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
    mBlurCalculator = new FloatCalculator(this, BASE_BLUR_SIZE);
    mCenterCalculator = new CenterCalculator(this);
  }

  @Override
  protected GPUImageFilter getFilter() {
    return new GPUImageZoomBlurFilter(getBlurSize(), getCenter());
  }

  @Override
  public float getBlurSize() {
    return mBlurCalculator.getBaseValue();
  }

  @Override
  public PointF getCenter() {
    // Center is very custom for BlurZoom. Currently no need to put in calculator for reuse.
    return mCenterCalculator.getBaseValue();
  }
}
