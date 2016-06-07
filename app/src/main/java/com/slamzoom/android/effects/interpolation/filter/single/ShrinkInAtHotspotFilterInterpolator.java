package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.PointF;

import com.slamzoom.android.effects.interpolation.filter.BaseShrinkFilterInterpolator;
import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 6/7/16.
 */
public class ShrinkInAtHotspotFilterInterpolator extends BaseShrinkFilterInterpolator {
  public ShrinkInAtHotspotFilterInterpolator() {
    this(null);
  }

  public ShrinkInAtHotspotFilterInterpolator(Interpolator interpolator) {
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
