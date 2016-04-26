package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.PointF;
import android.graphics.RectF;

import com.slamzoom.android.effects.interpolation.filter.base.AdjustedFilterInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 4/2/16.
 */
public class BulgeFilterInterpolator extends AdjustedFilterInterpolator {
  public BulgeFilterInterpolator() {
    super();
  }

  public BulgeFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  protected GPUImageFilter getFilter(float interpolationValue, PointF focus, RectF relativeHotspot) {
    return new GPUImageBulgeDistortionFilter(1, 0.6f * interpolationValue, focus);
  }
}
