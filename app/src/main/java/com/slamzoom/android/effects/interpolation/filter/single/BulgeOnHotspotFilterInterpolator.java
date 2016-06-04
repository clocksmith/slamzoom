package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.PointF;

import com.slamzoom.android.effects.interpolation.filter.base.BaseBulgeFilterInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;

/**
 * Created by clocksmith on 6/4/16.
 */
public class BulgeOnHotspotFilterInterpolator extends BaseBulgeFilterInterpolator {
  public BulgeOnHotspotFilterInterpolator() {
    this(null);
  }

  public BulgeOnHotspotFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  public float getRadius() {
    return BASE_RADIUS * getMinDimenOfHotspot();
  }

  @Override
  public float getScale() {
    return BASE_SCALE * getInterpolationValueCompliment();
  }

  @Override
  public PointF getCenter() {
    return getCenterOfHotspot();
  }
}
