package com.slamzoom.android.interpolate.combo;

import com.slamzoom.android.interpolate.base.Interpolator;
import com.slamzoom.android.interpolate.base.CubicSplineInterpolator;
import com.slamzoom.android.interpolate.base.LinearInterpolator;

/**
 * Created by clocksmith on 3/12/16.
 */
public class CrashInterpolatorProvider implements ScaleAndTranslateInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new LinearInterpolator(Interpolator.newPointListBuilder()
        .add(0, 0)
        .add(0.2f, 1)
        .add(1, 1)
        .build());
  }

  @Override
  public Interpolator getXInterpolator() {
    return new CubicSplineInterpolator(Interpolator.newPointListBuilder()
        .add(0, 0)
        .add(0.3f, 0.25f)
        .add(0.45f, 0.1f)
        .add(0.75f, 0)
        .add(1, 0)
        .build());
  }

  @Override
  public Interpolator getYInterpolator() {
    return new CubicSplineInterpolator(CubicSplineInterpolator.newPointListBuilder()
        .add(0, 0)
        .add(0.2f, 0.1f)
        .add(0.45f, 0)
        .add(0.6f, 0)
        .add(1, 0)
        .build());

  }
}
