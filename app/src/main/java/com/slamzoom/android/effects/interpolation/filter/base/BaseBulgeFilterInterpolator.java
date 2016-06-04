package com.slamzoom.android.effects.interpolation.filter.base;

import android.graphics.PointF;

import com.slamzoom.android.common.Constants;
import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 6/4/16.
 */
public abstract class BaseBulgeFilterInterpolator extends FilterInterpolator implements HasRadius, HasScale, HasCenter {
  protected static final float BASE_RADIUS = 0.5f;
  protected static final float BASE_SCALE = 0.5f;
  protected static final PointF BASE_CENTER = Constants.NORMAL_CENTER_POINT;

  public BaseBulgeFilterInterpolator() {
    this(null);
  }

  public BaseBulgeFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  protected GPUImageFilter getFilter() {
    return new GPUImageBulgeDistortionFilter(getRadius() ,getScale(), getCenter());
  }

  @Override
  public float getRadius() {
    return BASE_RADIUS;
  }

  @Override
  public float getScale() {
    return BASE_SCALE;
  }

  @Override
  public PointF getCenter() {
    return BASE_CENTER;
  }
}
