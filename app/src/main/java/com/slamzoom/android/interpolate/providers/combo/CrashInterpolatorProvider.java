package com.slamzoom.android.interpolate.providers.combo;

import com.slamzoom.android.interpolate.Interpolator;
import com.slamzoom.android.interpolate.CubicSplineInterpolator;
import com.slamzoom.android.interpolate.LinearInterpolator;

/**
 * Created by clocksmith on 3/12/16.
 */
public class CrashInterpolatorProvider implements ScaleAndTranslateInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new LinearInterpolator(Interpolator.newPointListBuilder()
        .add(0, 0)
        .add(0.4f, 1)
        .add(1, 1)
        .build());
  }

  @Override
  public Interpolator getXInterpolator() {
    return new LinearInterpolator(Interpolator.newPointListBuilder()
        .add(0, 0)
        .add(0.37f, -0.35f)
        .add(0.52f, 0.05f)
        .add(1, 0)
        .build());
  }

  @Override
  public Interpolator getYInterpolator() {
    return new LinearInterpolator(CubicSplineInterpolator.newPointListBuilder()
        .add(0, 0)
        .add(0.4f, -0.05f)
        .add(0.55f, 0.05f)
        .add(1, 0)
        .build());
  }
}
