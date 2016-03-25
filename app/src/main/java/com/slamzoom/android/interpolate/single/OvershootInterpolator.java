package com.slamzoom.android.interpolate.single;

import android.graphics.Point;
import android.graphics.PointF;

import com.slamzoom.android.interpolate.base.Interpolator;
import com.slamzoom.android.interpolate.base.spline.CubicSplineInterpolator;
import com.slamzoom.android.interpolate.base.spline.PointListBuilder;

import java.util.List;

/**
 * Created by clocksmith on 3/24/16.
 */
public class OvershootInterpolator extends CubicSplineInterpolator {
  public OvershootInterpolator() {
    super(PointListBuilder.newPointListBuilder()
    .add(0, 0)
    .add(0.9f, 1f)
    .add(1f, 1f)
    .build());
  }
}
