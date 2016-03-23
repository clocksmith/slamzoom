package com.slamzoom.android.interpolate.combo.scaletranslate;

import com.slamzoom.android.interpolate.base.spline.CubicSplineInterpolator;
import com.slamzoom.android.interpolate.base.Interpolator;
import com.slamzoom.android.interpolate.base.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolate.base.spline.PointListBuilder;
import com.slamzoom.android.interpolate.combo.ScaleAndTranslateInterpolatorProvider;

/**
 * Created by clocksmith on 3/16/16.
 */
public class CrashBounceDiagonalInterpolatorProvider implements ScaleAndTranslateInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new LinearSplineInterpolator(PointListBuilder.newPointListBuilder()
        .add(0, 0)
        .add(0.3f, 1)
        .add(1, 1)
        .build());
  }

  @Override
  public Interpolator getXInterpolator() {
    return new CubicSplineInterpolator(PointListBuilder.newPointListBuilder()
        .add(0, 0)
        .add(0.3f, 0.3f)
        .add(0.45f, 0f)
        .add(0.6f, 0)
        .add(1, 0)
        .build());
  }

  @Override
  public Interpolator getYInterpolator() {
    return new CubicSplineInterpolator(PointListBuilder.newPointListBuilder()
        .add(0, 0)
        .add(0.25f, -0.35f)
        .add(0.45f, 0)
        .add(0.6f, 0)
        .add(1, 0)
        .build());

  }
}