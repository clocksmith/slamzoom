package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.PointF;
import android.graphics.RectF;

import com.slamzoom.android.effects.interpolation.filter.base.AdjustedFilterInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSwirlFilter;

/**
 * Created by clocksmith on 3/22/16.
 */
public class UnswirlFilterInterpolator extends AdjustedFilterInterpolator {
  public UnswirlFilterInterpolator() {
    super();
  }

  public UnswirlFilterInterpolator(Interpolator interpolator) {
    super((interpolator));
  }

  @Override
  protected GPUImageFilter getFilter(float interpolationValue, PointF focus, RectF relativeHotspot) {
    return new GPUImageSwirlFilter(0.5f, 1 - interpolationValue, focus);
  }
}
