package com.slamzoom.android.effects.interpolation.transform.scale;

import com.slamzoom.android.interpolators.spline.PointsBuilder;
import com.slamzoom.android.interpolators.spline.StepInterpolator;

/**
 * Created by clocksmith on 7/4/16.
 */
public class TeaseInterpolator extends StepInterpolator {

  public TeaseInterpolator() {
    super(PointsBuilder.create()
        .withPoint(0.1667f, 0)
        .withPoint(0.5f, 0.2f)
        .withPoint(0.8333f, 0.45f)
        .withPoint(1, 1)
        .build());
  }
}
