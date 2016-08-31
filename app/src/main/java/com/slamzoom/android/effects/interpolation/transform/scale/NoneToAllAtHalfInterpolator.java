package com.slamzoom.android.effects.interpolation.transform.scale;


import com.slamzoom.android.interpolators.spline.PointsBuilder;
import com.slamzoom.android.interpolators.spline.StepInterpolator;

/**
 * Created by clocksmith on 7/8/16.
 */
public class NoneToAllAtHalfInterpolator extends StepInterpolator{
  public NoneToAllAtHalfInterpolator() {
    super(PointsBuilder.create()
        .withPoint(0.5f, 0)
        .withPoint(1, 1)
        .build());
  }
}
