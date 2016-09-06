package com.slamzoom.android.effects.interpolation;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.GaussianBlurFilterInterpolator;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolators.spline.PointsBuilder;

import java.util.List;

/**
 * Created by clocksmith on 9/5/16.
 */
public class CrashblurInterpolatorProvider extends EffectInterpolatorProvider {
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
        .withPoint(0.37f, 0.25f)
        .withPoint(0.52f, 0.03f)
        .withPoint(1, 0)
        .build());
  }

  @Override
  public Interpolator getYInterpolator() {
    return new LinearSplineInterpolator(PointsBuilder.create()
        .withPoint(0, 0)
        .withPoint(0.39f, -0.25f)
        .withPoint(0.52f, 0.05f)
        .withPoint(1, 0)
        .build());
  }

  @Override
  public List<FilterInterpolator> getFilterInterpolators() {
    return ImmutableList.<FilterInterpolator>of(
        new GaussianBlurFilterInterpolator(LinearSplineInterpolator.newBuilder()
            .withPoint(0, 0)
            .withPoint(0.3f, 1)
            .withPoint(0.6f, 1)
            .withPoint(0.9f, 0)
            .withPoint(1, 0)
            .build()));
  }
}