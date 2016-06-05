package com.slamzoom.android.effects.interpolation.filter.base;

import android.graphics.PointF;

import com.slamzoom.android.effects.interpolation.filter.base.parameters.HasCenter;
import com.slamzoom.android.effects.interpolation.filter.base.parameters.HasRadius;
import com.slamzoom.android.effects.interpolation.filter.base.parameters.HasScale;
import com.slamzoom.android.effects.interpolation.filter.base.parameters.calculators.CenterCalculator;
import com.slamzoom.android.effects.interpolation.filter.base.parameters.calculators.FloatCalculator;
import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 6/4/16.
 */
public abstract class BaseBulgeFilterInterpolator extends FilterInterpolator
    implements HasRadius, HasScale, HasCenter {
  private static final float BASE_RADIUS = 0.5f;
  private static final float BASE_SCALE = 0.5f;

  protected FloatCalculator mRadiusCalculator;
  protected FloatCalculator mScaleCalculator;
  protected CenterCalculator mCenterCalculator;

  public BaseBulgeFilterInterpolator() {
    this(null);
  }

  public BaseBulgeFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
    mRadiusCalculator = new FloatCalculator(this, BASE_RADIUS);
    mScaleCalculator = new FloatCalculator(this, BASE_SCALE);
    mCenterCalculator = new CenterCalculator(this);
  }

  @Override
  protected GPUImageFilter getFilter() {
    return new GPUImageBulgeDistortionFilter(getRadius() ,getScale(), getCenter());
  }

  @Override
  public float getRadius() {
    return mRadiusCalculator.getBaseValue();
  }

  @Override
  public float getScale() {
    return mScaleCalculator.getBaseValue();
  }

  @Override
  public PointF getCenter() {
    return mCenterCalculator.getBaseValue();
  }
}
