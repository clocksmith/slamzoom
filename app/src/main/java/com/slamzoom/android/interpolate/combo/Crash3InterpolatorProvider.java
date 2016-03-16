package com.slamzoom.android.interpolate.combo;

import com.slamzoom.android.interpolate.base.CubicSplineInterpolator;
import com.slamzoom.android.interpolate.base.Interpolator;
import com.slamzoom.android.interpolate.base.LinearInterpolator;

/**
 * Created by clocksmith on 3/16/16.
 */
public class Crash3InterpolatorProvider implements ScaleAndTranslateInterpolatorProvider {
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
        .add(0.3f, -0.05f)
        .add(0.4f, 0f)
        .add(0.5f, 0)
        .add(1, 0)
        .build());
  }

  @Override
  public Interpolator getYInterpolator() {
    return new CubicSplineInterpolator(CubicSplineInterpolator.newPointListBuilder()
        .add(0, 0)
        .add(0.3f, 0.25f)
        .add(0.4f, 0f)
        .add(0.5f, 0)
        .add(1, 0)
        .build());
  }
}
