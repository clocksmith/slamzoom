package com.slamzoom.android.effects.interpolation.transform.scale;

import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolators.spline.PointsBuilder;

/**
 * Created by clocksmith on 3/22/16.
 */
public class InAndOutInterpolator extends LinearSplineInterpolator {
  public InAndOutInterpolator() {
    super(PointsBuilder.create()
        .withPoint(0, 0)
        .withPoint(0.5f, 1)
        .withPoint(1, 0)
        .build());
  }
}