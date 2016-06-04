package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.PointF;
import android.graphics.RectF;

import com.slamzoom.android.effects.imagefilters.GPUImageZoomBlurFilter;
import com.slamzoom.android.effects.interpolation.filter.base.BaseZoomBlurFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.base.HasBlurSize;
import com.slamzoom.android.effects.interpolation.filter.base.HasCenter;
import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 3/21/16.
 */
public class ZoomBlurFilterInterpolator extends BaseZoomBlurFilterInterpolator {
  public ZoomBlurFilterInterpolator() {
    this(null);
  }

  public ZoomBlurFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  public float getBlurSize() {
    return BASE_BLUR_SIZE * getInterpolationValue();
  }

  @Override
  public PointF getCenter() {
    return getCenterOfHotspot();
  }
}