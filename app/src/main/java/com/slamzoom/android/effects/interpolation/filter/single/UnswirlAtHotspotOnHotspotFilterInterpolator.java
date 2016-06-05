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
    return BASE_RADIUS * getMinDimenOfHotspot();
  }

  @Override
  public float getRotation() {
    return BASE_ROTATION * getInterpolationValueCompliment();
  }

  @Override
  public PointF getCenter() {
    return getCenterOfHotspot();
  }
}
