package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.RectF;

import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageBoxBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 3/21/16.
 */
public class BoxUnblurFilterInterpolator extends FilterInterpolator {
  @Override
  public GPUImageFilter getFilter(float interpolationValue, RectF normalizedHotspot, float normalizedScale) {
    return new GPUImageBoxBlurFilter(1 - interpolationValue);
  }
}
