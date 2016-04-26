package com.slamzoom.android.effects.interpolation.filter.base;

import android.graphics.PointF;
import android.graphics.RectF;

import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 4/22/16.
 */
public abstract class AdjustedFilterInterpolator extends FilterInterpolator {
  public AdjustedFilterInterpolator() {
    super();
  }

  public AdjustedFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  public GPUImageFilter getInterpolationFilter(float input, PointF relativeFocus, RectF relativeHotspot) {
    return getFilter(mInterpolator.getInterpolation(input), relativeFocus, relativeHotspot);
  }

  protected abstract GPUImageFilter getFilter(float interpolationValue, PointF focus, RectF relativeHotspot);
}
