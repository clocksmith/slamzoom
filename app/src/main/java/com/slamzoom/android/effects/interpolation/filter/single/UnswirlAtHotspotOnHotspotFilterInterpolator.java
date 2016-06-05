package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.PointF;

import com.slamzoom.android.interpolators.base.Interpolator;

/**
 * Created by clocksmith on 6/2/16.
 */
public class UnswirlAtHotspotOnHotspotFilterInterpolator extends UnswirlAtHotspotFilterInterpolator {
  public UnswirlAtHotspotOnHotspotFilterInterpolator() {
    this(null);
  }

  public UnswirlAtHotspotOnHotspotFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  public float getRadius() {
    return mRadiusCalculator.getValueFromMinHotspotDimen();
  }

  @Override
  public float getRotation() {
    return mRotationCalculator.getValueFromInterpolationCompliment();
  }

  @Override
  public PointF getCenter() {
    return mCenterCalculator.getHotspotCenter();
  }
}
