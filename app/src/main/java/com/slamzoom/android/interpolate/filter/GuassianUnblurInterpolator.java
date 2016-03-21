package com.slamzoom.android.interpolate.filter;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;

/**
 * Created by clocksmith on 3/21/16.
 */
public class GuassianUnblurInterpolator extends FilterInterpolator {
  @Override
  public GPUImageFilter getFilter(float interpolationValue) {
    return new GPUImageGaussianBlurFilter(1 - interpolationValue);
  }
}
