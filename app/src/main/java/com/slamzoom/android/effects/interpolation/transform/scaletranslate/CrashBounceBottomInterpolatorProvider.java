package com.slamzoom.android.effects.interpolation.transform.scaletranslate;

import com.slamzoom.android.effects.interpolation.transform.TransformInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.spline.CubicSplineInterpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolators.spline.PointsBuilder;

/**
 * Created by clocksmith on 3/16/16.
 */
public class CrashBounceBottomInterpolatorProvider implements TransformInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new LinearSplineInterpolator(PointsBuilder.create()
        .withPoint(0, 0)
        .withPoint(0.3f, 1)
        .withPoint(1, 1)
        .build());
  }

  @Override
  public Interpolator getXInterpolator() {
    return new CubicSplineInterpolator(PointsBuilder.create()
        .withPoint(0, 0)
        .withPoint(0.3f, -0.05f)
        .withPoint(0.4f, 0)
        .withPoint(0.5f, 0)
        .withPoint(1, 0)
        .build());
  }

  @Override
  public Interpolator getYInterpolator() {
    return new CubicSplineInterpolator(PointsBuilder.create()
        .withPoint(0, 0)
        .withPoint(0.3f, 0.25f)
        .withPoint(0.4f, 0)
        .withPoint(0.5f, 0)
        .withPoint(1, 0)
        .build());
  }
}
