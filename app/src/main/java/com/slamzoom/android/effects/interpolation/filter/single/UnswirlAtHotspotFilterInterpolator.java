package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.PointF;

import com.slamzoom.android.effects.interpolation.filter.base.BaseSwirlFilterInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;

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
    return BASE_RADIUS * getInterpolationValueCompliment();
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
