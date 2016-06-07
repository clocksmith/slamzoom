package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.PointF;

import com.slamzoom.android.effects.interpolation.filter.BaseZoomBlurFilterInterpolator;
import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 3/21/16.
 */
public class ZoomBlurAtHotspotFilterInterpolator extends BaseZoomBlurFilterInterpolator {
  public ZoomBlurAtHotspotFilterInterpolator() {
    this(null);
  }

  public ZoomBlurAtHotspotFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  public float getBlurSize() {
    return mBlurCalculator.getValueFromInterpolation();
  }

  @Override
  public PointF getCenter() {
    return mCenterCalculator.getHotspotCenter();
  }
}