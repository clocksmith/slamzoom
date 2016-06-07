package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.PointF;

import com.slamzoom.android.effects.interpolation.filter.BaseSwirlFilterInterpolator;
import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 6/4/16.
 */
public class UnswirlAtHotspotFilterInterpolator extends BaseSwirlFilterInterpolator {
  public UnswirlAtHotspotFilterInterpolator() {
    this(null);
  }

  public UnswirlAtHotspotFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  public float getRadius() {
    return mRadiusCalculator.getValueFromInterpolationCompliment();
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
