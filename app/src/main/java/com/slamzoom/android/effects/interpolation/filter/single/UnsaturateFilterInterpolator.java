package com.slamzoom.android.effects.interpolation.filter.single;

import com.slamzoom.android.effects.interpolation.filter.base.RegularFilterInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;

/**
 * Created by clocksmith on 3/21/16.
 */
public class UnsaturateFilterInterpolator extends RegularFilterInterpolator {
  public UnsaturateFilterInterpolator() {
    super();
  }

  public UnsaturateFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  protected GPUImageFilter getFilter(float interpolationValue) {
    return new GPUImageSaturationFilter(1 - interpolationValue);
  }
}
