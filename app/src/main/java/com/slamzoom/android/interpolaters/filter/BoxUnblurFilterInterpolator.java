package com.slamzoom.android.interpolaters.filter;

import jp.co.cyberagent.android.gpuimage.GPUImageBoxBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 3/21/16.
 */
public class BoxUnblurFilterInterpolator extends FilterInterpolator {
  @Override
  protected GPUImageFilter getFilter(float interpolationValue) {
    return new GPUImageBoxBlurFilter(1 - interpolationValue);
  }
}
