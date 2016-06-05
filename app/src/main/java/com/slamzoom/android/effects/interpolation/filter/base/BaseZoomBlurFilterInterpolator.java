package com.slamzoom.android.effects.interpolation.filter.base;

import android.graphics.PointF;

import com.slamzoom.android.common.Constants;
import com.slamzoom.android.effects.imagefilters.GPUImageZoomBlurFilter;
import com.slamzoom.android.effects.interpolation.filter.base.parameters.HasBlurSize;
import com.slamzoom.android.effects.interpolation.filter.base.parameters.HasCenter;
import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 6/4/16.
 */
public class BaseZoomBlurFilterInterpolator extends FilterInterpolator implements HasBlurSize, HasCenter {
  protected static final float BASE_BLUR_SIZE = 5;
  protected static final PointF BASE_CENTER = Constants.NORMAL_CENTER_POINT;

  public BaseZoomBlurFilterInterpolator() {
    this(null);
  }

  public BaseZoomBlurFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  protected GPUImageFilter getFilter() {
    return new GPUImageZoomBlurFilter(getBlurSize(), getCenter());
  }

  @Override
  public float getBlurSize() {
    return BASE_BLUR_SIZE;
  }

  @Override
  public PointF getCenter() {
    return BASE_CENTER;
  }
}
