package com.slamzoom.android.interpolate.combo.scaletranslate;

import com.slamzoom.android.interpolate.CubicSplineInterpolator;
import com.slamzoom.android.interpolate.Interpolator;
import com.slamzoom.android.interpolate.LinearSplineInterpolator;
import com.slamzoom.android.interpolate.PointListBuilder;
import com.slamzoom.android.interpolate.combo.ScaleAndTranslateInterpolatorProvider;

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
