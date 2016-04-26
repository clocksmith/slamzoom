package com.slamzoom.android.effects.interpolation.filter.single;

import com.slamzoom.android.effects.interpolation.filter.base.RegularFilterInterpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageBoxBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 3/21/16.
 */
public class BoxUnblurFilterInterpolator extends RegularFilterInterpolator {
  @Override
  protected GPUImageFilter getFilter(float interpolationValue) {
    return new GPUImageBoxBlurFilter(1 - interpolationValue);
  }
}
