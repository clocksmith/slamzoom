package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.PointF;

import com.slamzoom.android.effects.interpolation.filter.base.BaseBulgeFilterInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;

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
    return BASE_RADIUS * getInterpolationValue();
  }

  @Override
  public float getScale() {
    return BASE_SCALE * getInterpolationValue();
  }

  @Override
  public PointF getCenter() {
    return getCenterOfHotspot();
  }
}
