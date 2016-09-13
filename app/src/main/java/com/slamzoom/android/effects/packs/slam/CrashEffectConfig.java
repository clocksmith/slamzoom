package com.slamzoom.android.effects.packs.slam;

import com.slamzoom.android.effects.EffectConfig;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.GaussianBlurFilterInterpolator;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolators.spline.PointsBuilder;

/**
 * Created by clocksmith on 9/5/16.
 */
public class CrashEffectConfig extends EffectConfig {
  @Override
  public String getName() {
    return "crash";
  }

  @Override
  public float getDurationSeconds() {
    return 0.75f;
  }

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
        .withPoint(0.37f, 0.40f)
        .withPoint(0.52f, 0.05f)
        .withPoint(1, 0)
        .build());
  }

  @Override
  public Interpolator getYInterpolator() {
    return new LinearSplineInterpolator(PointsBuilder.create()
        .withPoint(0, 0)
        .withPoint(0.39f, -0.40f)
        .withPoint(0.52f, 0.10f)
        .withPoint(1, 0)
        .build());
  }

  @Override
  public FilterInterpolator getFilterInterpolator() {
    return new GaussianBlurFilterInterpolator(LinearSplineInterpolator.newBuilder()
        .withPoint(0, 0)
        .withPoint(0.3f, 1)
        .withPoint(0.6f, 1)
        .withPoint(0.9f, 0)
        .withPoint(1, 0)
        .build());
  }
}