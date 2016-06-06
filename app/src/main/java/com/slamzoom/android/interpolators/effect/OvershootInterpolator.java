package com.slamzoom.android.interpolators.effect;

import com.slamzoom.android.interpolators.spline.CubicSplineInterpolator;
import com.slamzoom.android.interpolators.spline.PointListBuilder;

/**
 * Created by clocksmith on 3/24/16.
 */
public class OvershootInterpolator extends CubicSplineInterpolator {
  public OvershootInterpolator() {
    super(PointListBuilder.create()
    .add(0, 0)
    .add(0.9f, 1f)
    .add(1f, 1f)
    .build());
  }
}
