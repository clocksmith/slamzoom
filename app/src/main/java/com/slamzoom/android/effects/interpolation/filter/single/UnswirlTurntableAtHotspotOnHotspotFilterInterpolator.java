package com.slamzoom.android.effects.interpolation.filter.single;

import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolators.spline.PointsBuilder;

/**
 * Created by clocksmith on 6/3/16.
 */
public class UnswirlTurntableAtHotspotOnHotspotFilterInterpolator extends UnswirlAtHotspotOnHotspotFilterInterpolator {
  Interpolator mAngleInterpolator;

  public UnswirlTurntableAtHotspotOnHotspotFilterInterpolator() {
    this(null);
  }

  public UnswirlTurntableAtHotspotOnHotspotFilterInterpolator(Interpolator interpolator) {
    super(interpolator);

    mAngleInterpolator = new LinearSplineInterpolator(
        PointsBuilder.create()
            .withPoint(0f, 1f)
            .withPoint(0.2f, 0.4f)
            .withPoint(0.3f, 0.45f)
            .withPoint(0.4f, 0.4f)
            .withPoint(0.5f, 0.45f)
            .withPoint(0.6f, 0.4f)
            .withPoint(0.7f, 1f)
            .withPoint(1f, 0)
            .build());
  }

  @Override
  public float getRotation() {
    return mRotationCalculator.getValueFromSubInterpolationOfInterpolation(mAngleInterpolator);
  }
}
