package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.RectF;

import com.slamzoom.android.effects.interpolation.filter.base.BaseSaturationFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;

/**
 * Created by clocksmith on 3/21/16.
 */
public class UnsaturateFilterInterpolator extends BaseSaturationFilterInterpolator {
  public UnsaturateFilterInterpolator() {
    this(null);
  }

  public UnsaturateFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  public float getSaturation() {
    return BASE_SATURATION * getInterpolationValueCompliment();
  }
}
