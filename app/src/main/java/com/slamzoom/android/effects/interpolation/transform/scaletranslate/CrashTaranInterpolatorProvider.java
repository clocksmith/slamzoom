package com.slamzoom.android.effects.interpolation.transform.scaletranslate;

import com.slamzoom.android.effects.interpolation.transform.ScaleAndTranslateInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolators.spline.PointListBuilder;

/**
 * Created by clocksmith on 6/6/16.
 */
public class CrashTaranInterpolatorProvider implements ScaleAndTranslateInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new LinearSplineInterpolator(PointListBuilder.create()
        .add(0, 0)
        .add(0.4f, 1)
        .add(1, 1)
        .build());
  }

  @Override
  public Interpolator getXInterpolator() {
    return new LinearSplineInterpolator(PointListBuilder.create()
        .add(0, 0)
        .add(0.37f, 0.5f)
        .add(0.52f, 0)
        .add(1, 0)
        .build());
  }

  @Override
  public Interpolator getYInterpolator() {
    return new LinearSplineInterpolator(PointListBuilder.create()
        .add(0, 0)
        .add(0.39f, -0.5f)
        .add(0.53f, 0.05f)
        .add(1, 0)
        .build());
  }
}
