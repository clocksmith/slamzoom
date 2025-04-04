package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.Matrix;

import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageTransformFilter;

/**
 * Created by clocksmith on 9/17/16.
 */
public class RotateFilterInterpolator extends FilterInterpolator {
  @Override
  protected GPUImageFilter getFilter() {
    GPUImageTransformFilter filter = new GPUImageTransformFilter();
    Matrix m = new Matrix();
    m.postRotate((float) (getInterpolationValue() * Math.PI));
    return new GPUImageTransformFilter();
  }
}
