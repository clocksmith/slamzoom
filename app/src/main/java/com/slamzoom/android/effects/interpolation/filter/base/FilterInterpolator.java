package com.slamzoom.android.effects.interpolation.filter.base;

import android.graphics.RectF;

import com.slamzoom.android.interpolators.base.Interpolator;
import com.slamzoom.android.interpolators.base.InterpolatorHolder;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 4/25/16.
 *
 * marker abstract class
 */
public abstract class FilterInterpolator extends InterpolatorHolder {
  public FilterInterpolator() {}

  public FilterInterpolator(Interpolator interpolator) {
   super(interpolator);
  }

  public GPUImageFilter getInterpolationFilter(float percent, RectF normalizedHotspot) {
    return getFilter(mInterpolator.getInterpolation(percent), normalizedHotspot);
  }

  protected abstract GPUImageFilter getFilter(float interpolationValue, RectF normalizedHotspot);
}
