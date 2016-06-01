package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.RectF;

import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;

/**
 * Created by clocksmith on 3/21/16.
 */
public class UnsaturateFilterInterpolator extends FilterInterpolator {
  public UnsaturateFilterInterpolator() {
    super();
  }

  public UnsaturateFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  public GPUImageFilter getFilter(float interpolationValue, RectF normalizedHotspot) {
    return new GPUImageSaturationFilter(1 - interpolationValue);
  }
}
