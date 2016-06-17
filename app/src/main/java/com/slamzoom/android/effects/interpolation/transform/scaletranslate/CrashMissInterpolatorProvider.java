package com.slamzoom.android.effects.interpolation.transform.scaletranslate;

import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolators.spline.PointListBuilder;
import com.slamzoom.android.effects.interpolation.transform.ScaleAndTranslateInterpolatorProvider;

/**
 * Created by clocksmith on 3/12/16.
 */
public class CrashMissInterpolatorProvider implements ScaleAndTranslateInterpolatorProvider {
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
        .add(0.37f, -0.35f)
        .add(0.52f, 0.05f)
        .add(1, 0)
        .build());
  }

  @Override
  public Interpolator getYInterpolator() {
    return new LinearSplineInterpolator(PointListBuilder.create()
        .add(0, 0)
        .add(0.4f, -0.05f)
        .add(0.55f, 0.05f)
        .add(1, 0)
        .build());
  }
}
