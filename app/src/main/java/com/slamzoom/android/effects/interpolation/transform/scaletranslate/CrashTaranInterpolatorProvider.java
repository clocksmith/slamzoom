package com.slamzoom.android.effects.interpolation.transform.scaletranslate;

import com.slamzoom.android.effects.interpolation.transform.ScaleAndTranslateInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolators.spline.PointsBuilder;

/**
 * Created by clocksmith on 6/6/16.
 */
public class CrashTaranInterpolatorProvider implements ScaleAndTranslateInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new LinearSplineInterpolator(PointsBuilder.create()
        .withPoint(0, 0)
        .withPoint(0.4f, 1)
        .withPoint(1, 1)
        .build());
  }

  @Override
  public Interpolator getXInterpolator() {
    return new LinearSplineInterpolator(PointsBuilder.create()
        .withPoint(0, 0)
        .withPoint(0.37f, 0.75f)
        .withPoint(0.52f, 0.05f)
        .withPoint(1, 0)
        .build());
  }

  @Override
  public Interpolator getYInterpolator() {
    return new LinearSplineInterpolator(PointsBuilder.create()
        .withPoint(0, 0)
        .withPoint(0.39f, -0.75f)
        .withPoint(0.52f, 0.1f)
        .withPoint(1, 0)
        .build());
  }
}
