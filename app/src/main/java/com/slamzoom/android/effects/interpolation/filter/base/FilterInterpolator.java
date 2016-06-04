package com.slamzoom.android.effects.interpolation.filter.base;

import android.graphics.PointF;
import android.graphics.RectF;

import com.slamzoom.android.common.utils.RectFUtils;
import com.slamzoom.android.interpolators.base.Interpolator;
import com.slamzoom.android.interpolators.base.HasInterpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 4/25/16.
 *
 * marker abstract class
 */
public abstract class FilterInterpolator extends HasInterpolator {
  private float mInterpolationValue;
  private RectF mNormalizedHotspot;

  public FilterInterpolator() {}

  public FilterInterpolator(Interpolator interpolator) {
   super(interpolator);
  }

  public GPUImageFilter getInterpolationFilter(float percent, RectF normalizedHotspot) {
    mInterpolationValue = mInterpolator.getInterpolation(percent);
    mNormalizedHotspot = normalizedHotspot;
    return getFilter();
  }

  public float getInterpolationValue() {
    return mInterpolationValue;
  }

  public RectF getNormalizedHotspot() {
    return mNormalizedHotspot;
  }

  public float getInterpolationValueCompliment() {
    return 1 - getInterpolationValue();
  }

  public PointF getCenterOfHotspot() {
    return RectFUtils.getCenterPointF(getNormalizedHotspot());
  }

  public float getMinDimenOfHotspot() {
    return RectFUtils.getMinDimen(getNormalizedHotspot());
  }

  protected abstract GPUImageFilter getFilter();
}
