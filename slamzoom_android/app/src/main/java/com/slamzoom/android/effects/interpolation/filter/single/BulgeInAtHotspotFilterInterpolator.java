package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.PointF;

import com.slamzoom.android.effects.interpolation.filter.BaseBulgeFilterInterpolator;
import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 4/2/16.
 */
public class BulgeInAtHotspotFilterInterpolator extends BaseBulgeFilterInterpolator {
  public BulgeInAtHotspotFilterInterpolator() {
    this(null);
  }

  public BulgeInAtHotspotFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  public float getRadius() {
    return mRadiusCalculator.getValueFromInterpolation();
  }

  @Override
  public float getScale() {
    return mScaleCalculator.getValueFromInterpolation();
  }

  @Override
  public PointF getCenter() {
    return mCenterCalculator.getHotspotCenter();
  }
}
