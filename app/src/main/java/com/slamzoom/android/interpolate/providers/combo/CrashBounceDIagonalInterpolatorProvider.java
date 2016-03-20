package com.slamzoom.android.interpolate.providers.combo;

import com.slamzoom.android.interpolate.CubicSplineInterpolator;
import com.slamzoom.android.interpolate.Interpolator;
import com.slamzoom.android.interpolate.LinearInterpolator;

/**
 * Created by clocksmith on 3/16/16.
 */
public class CrashBounceDIagonalInterpolatorProvider implements ScaleAndTranslateInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new LinearInterpolator(Interpolator.newPointListBuilder()
        .add(0, 0)
        .add(0.3f, 1)
        .add(1, 1)
        .build());
  }

  @Override
  public Interpolator getXInterpolator() {
    return new CubicSplineInterpolator(Interpolator.newPointListBuilder()
        .add(0, 0)
        .add(0.3f, 0.3f)
        .add(0.45f, 0f)
        .add(0.6f, 0)
        .add(1, 0)
        .build());
  }

  @Override
  public Interpolator getYInterpolator() {
    return new CubicSplineInterpolator(CubicSplineInterpolator.newPointListBuilder()
        .add(0, 0)
        .add(0.25f, -0.35f)
        .add(0.45f, 0)
        .add(0.6f, 0)
        .add(1, 0)
        .build());

  }
}