package com.slamzoom.android.interpolate.filter;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;

/**
 * Created by clocksmith on 3/21/16.
 */
public class UnsaturateFilterInterpolator extends FilterInterpolator {
  @Override
  protected GPUImageFilter getFilter(float interpolationValue) {
    return new GPUImageSaturationFilter(1 - interpolationValue);
  }
}
