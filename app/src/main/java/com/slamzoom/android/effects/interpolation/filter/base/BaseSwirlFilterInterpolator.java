package com.slamzoom.android.effects.interpolation.filter.base;

import android.graphics.PointF;

import com.slamzoom.android.common.Constants;
import com.slamzoom.android.effects.interpolation.filter.base.parameters.HasCenter;
import com.slamzoom.android.effects.interpolation.filter.base.parameters.HasRadius;
import com.slamzoom.android.effects.interpolation.filter.base.parameters.HasRotation;
import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSwirlFilter;

/**
 * Created by clocksmith on 6/4/16.
 */
public abstract class BaseSwirlFilterInterpolator extends FilterInterpolator
    implements HasRadius, HasRotation, HasCenter {
  protected static final float BASE_RADIUS = 0.5f;
  protected static final float BASE_ROTATION = 1;
  protected static final PointF BASE_CENTER = Constants.NORMAL_CENTER_POINT;

  public BaseSwirlFilterInterpolator() {
    this(null);
  }

  public BaseSwirlFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  public final GPUImageFilter getFilter() {
    return new GPUImageSwirlFilter(getRadius(), getRotation(), getCenter());
  }

  @Override
  public float getRadius() {
    return BASE_RADIUS;
  }

  @Override
  public float getRotation() {
    return BASE_ROTATION;
  }

  @Override
  public PointF getCenter() {
    return BASE_CENTER;
  }
}
