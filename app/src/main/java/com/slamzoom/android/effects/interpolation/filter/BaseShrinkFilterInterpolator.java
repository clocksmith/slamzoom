package com.slamzoom.android.effects.interpolation.filter;

import android.graphics.PointF;

import com.slamzoom.android.effects.interpolation.filter.calculators.CenterCalculator;
import com.slamzoom.android.effects.interpolation.filter.calculators.FloatCalculator;
import com.slamzoom.android.effects.interpolation.filter.parameters.HasCenter;
import com.slamzoom.android.effects.interpolation.filter.parameters.HasRadius;
import com.slamzoom.android.effects.interpolation.filter.parameters.HasScale;
import com.slamzoom.android.interpolators.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 6/7/16.
 */
public class BaseShrinkFilterInterpolator extends FilterInterpolator
    implements HasRadius, HasScale, HasCenter {
  private static final float BASE_RADIUS = 0.49f;
  private static final float BASE_SCALE = -0.5f;

  protected FloatCalculator mRadiusCalculator;
  protected FloatCalculator mScaleCalculator;
  protected CenterCalculator mCenterCalculator;

  public BaseShrinkFilterInterpolator() {
    this(null);
  }

  public BaseShrinkFilterInterpolator(Interpolator interpolator) {
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
