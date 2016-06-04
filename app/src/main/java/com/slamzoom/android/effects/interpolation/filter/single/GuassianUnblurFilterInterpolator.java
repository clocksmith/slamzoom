package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.RectF;

import com.slamzoom.android.effects.interpolation.filter.base.BaseGuassianBlurFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;

/**
 * Created by clocksmith on 3/21/16.
 */
public class GuassianUnblurFilterInterpolator extends BaseGuassianBlurFilterInterpolator {
  public GuassianUnblurFilterInterpolator() {
    this(null);
  }

  public GuassianUnblurFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  public float getBlurSize() {
    return BASE_BLUR_SIZE * getInterpolationValueCompliment();
  }
}
