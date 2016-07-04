package com.slamzoom.android.interpolators.custom;

import com.slamzoom.android.interpolators.spline.CubicSplineInterpolator;
import com.slamzoom.android.interpolators.spline.PointsBuilder;

/**
 * Created by clocksmith on 3/24/16.
 */
public class OvershootInterpolator extends CubicSplineInterpolator {
  public OvershootInterpolator() {
    super(PointsBuilder.create()
    .withPoint(0, 0)
    .withPoint(0.4f, 1f)
    .withPoint(1f, 1f)
    .build());
  }
}
