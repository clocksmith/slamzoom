package com.slamzoom.android.interpolate.combo.scaletranslate;

import com.slamzoom.android.interpolate.base.Interpolator;
import com.slamzoom.android.interpolate.base.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolate.base.spline.PointListBuilder;
import com.slamzoom.android.interpolate.combo.ScaleAndTranslateInterpolatorProvider;

/**
 * Created by clocksmith on 3/12/16.
 */
public class CrashInterpolatorProvider implements ScaleAndTranslateInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new LinearSplineInterpolator(PointListBuilder.newPointListBuilder()
        .add(0, 0)
        .add(0.4f, 1)
        .add(1, 1)
        .build());
  }

  @Override
  public Interpolator getXInterpolator() {
    return new LinearSplineInterpolator(PointListBuilder.newPointListBuilder()
        .add(0, 0)
        .add(0.37f, -0.35f)
        .add(0.52f, 0.05f)
        .add(1, 0)
        .build());
  }

  @Override
  public Interpolator getYInterpolator() {
    return new LinearSplineInterpolator(PointListBuilder.newPointListBuilder()
        .add(0, 0)
        .add(0.4f, -0.05f)
        .add(0.55f, 0.05f)
        .add(1, 0)
        .build());
  }
}
