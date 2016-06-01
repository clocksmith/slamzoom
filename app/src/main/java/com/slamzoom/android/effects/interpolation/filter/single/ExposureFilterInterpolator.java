package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.RectF;

import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 3/31/16.
 */
public class ExposureFilterInterpolator extends FilterInterpolator {
  public ExposureFilterInterpolator() {
    super();
  }

  public ExposureFilterInterpolator(Interpolator interpolator) {
    super((interpolator));
  }

  @Override
  public GPUImageFilter getFilter(float interpolationValue, RectF normalizedHotspot) {
    return new GPUImageExposureFilter(-10 * interpolationValue);
  }
}
