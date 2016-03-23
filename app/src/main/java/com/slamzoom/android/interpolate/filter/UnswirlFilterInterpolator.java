package com.slamzoom.android.interpolate.filter;

import android.graphics.PointF;

import com.slamzoom.android.interpolate.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSwirlFilter;

/**
 * Created by clocksmith on 3/22/16.
 */
public class UnswirlFilterInterpolator extends FilterInterpolator {
  public UnswirlFilterInterpolator() {
    super();
  }

  public UnswirlFilterInterpolator(Interpolator interpolator) {
    super((interpolator));
  }

  @Override
  protected GPUImageFilter getFilter(float interpolationValue) {
    return new GPUImageSwirlFilter(0.5f, 1 - interpolationValue, new PointF(0.5f, 0.5f));
  }
}
