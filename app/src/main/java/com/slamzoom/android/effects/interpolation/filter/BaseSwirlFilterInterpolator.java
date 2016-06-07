package com.slamzoom.android.effects.interpolation.filter;

import android.graphics.PointF;

import com.slamzoom.android.effects.interpolation.filter.parameters.HasCenter;
import com.slamzoom.android.effects.interpolation.filter.parameters.HasRadius;
import com.slamzoom.android.effects.interpolation.filter.parameters.HasRotation;
import com.slamzoom.android.effects.interpolation.filter.calculators.CenterCalculator;
import com.slamzoom.android.effects.interpolation.filter.calculators.FloatCalculator;
import com.slamzoom.android.interpolators.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSwirlFilter;

/**
 * Created by clocksmith on 6/4/16.
 */
public abstract class BaseSwirlFilterInterpolator extends FilterInterpolator
    implements HasRadius, HasRotation, HasCenter {
  protected static final float BASE_RADIUS = 0.5f;
  protected static final float BASE_ROTATION = 1;

  protected FloatCalculator mRadiusCalculator;
  protected FloatCalculator mRotationCalculator;
  protected CenterCalculator mCenterCalculator;

  public BaseSwirlFilterInterpolator() {
    this(null);
  }

  public BaseSwirlFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
    mRadiusCalculator = new FloatCalculator(this, BASE_RADIUS);
    mRotationCalculator = new FloatCalculator(this, BASE_ROTATION);
    mCenterCalculator = new CenterCalculator(this);
  }

  @Override
  public final GPUImageFilter getFilter() {
    return new GPUImageSwirlFilter(getRadius(), getRotation(), getCenter());
  }

  @Override
  public float getRadius() {
    return mRadiusCalculator.getBaseValue();
  }

  @Override
  public float getRotation() {
    return mRotationCalculator.getBaseValue();
  }

  @Override
  public PointF getCenter() {
    return mCenterCalculator.getBaseValue();
  }
}
