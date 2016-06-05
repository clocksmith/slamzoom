package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.PointF;

import com.slamzoom.android.effects.interpolation.filter.base.BaseBulgeFilterInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;

/**
 * Created by clocksmith on 6/4/16.
 */
public class BulgeInAtHotspotOnHotspotFilterInterpolator extends BaseBulgeFilterInterpolator {
  public BulgeInAtHotspotOnHotspotFilterInterpolator() {
    this(null);
  }

  public BulgeInAtHotspotOnHotspotFilterInterpolator(Interpolator interpolator) {
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
