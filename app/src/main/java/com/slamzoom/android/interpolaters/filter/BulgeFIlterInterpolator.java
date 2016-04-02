package com.slamzoom.android.interpolaters.filter;

import android.graphics.PointF;

import com.slamzoom.android.interpolaters.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 4/2/16.
 */
public class BulgeFilterInterpolator extends FilterInterpolator {
  public BulgeFilterInterpolator() {
    super();
  }

  public BulgeFilterInterpolator(Interpolator interpolator) {
    super((interpolator));
  }

  @Override
  protected GPUImageFilter getFilter(float interpolationValue) {
    return new GPUImageBulgeDistortionFilter(0.67f, 0.67f * interpolationValue, new PointF(0.5f, 0.5f));
  }
}
