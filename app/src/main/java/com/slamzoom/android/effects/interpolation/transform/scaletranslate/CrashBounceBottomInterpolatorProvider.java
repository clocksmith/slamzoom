package com.slamzoom.android.effects.interpolation.transform.scaletranslate;

import com.slamzoom.android.interpolators.spline.CubicSplineInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolators.spline.PointListBuilder;
import com.slamzoom.android.effects.interpolation.transform.base.ScaleAndTranslateInterpolatorProvider;

/**
 * Created by clocksmith on 3/16/16.
 */
public class CrashBounceBottomInterpolatorProvider implements ScaleAndTranslateInterpolatorProvider {
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
        .add(0.3f, -0.05f)
        .add(0.4f, 0f)
        .add(0.5f, 0)
        .add(1, 0)
        .build());
  }

  @Override
  public Interpolator getYInterpolator() {
    return new CubicSplineInterpolator(PointListBuilder.newPointListBuilder()
        .add(0, 0)
        .add(0.3f, 0.25f)
        .add(0.4f, 0f)
        .add(0.5f, 0)
        .add(1, 0)
        .build());
  }
}
