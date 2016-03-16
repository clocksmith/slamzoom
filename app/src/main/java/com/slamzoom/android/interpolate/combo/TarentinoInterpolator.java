package com.slamzoom.android.interpolate.combo;

import android.graphics.PointF;

import com.slamzoom.android.interpolate.base.BaseInterpolator;
import com.slamzoom.android.interpolate.base.CubicSplineInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 3/12/16.
 */
public class TarentinoInterpolator extends AbstractScaleAndTranslateInterpolator {

  @Override
  protected BaseInterpolator getScaleInterpolator() {
    return new CubicSplineInterpolator(CubicSplineInterpolator.newPointListBuilder()
        .add(0, 0)
        .add(1, 1)
        .build());
  }

  @Override
  protected BaseInterpolator getXInterpolator() {
    return new CubicSplineInterpolator(CubicSplineInterpolator.newPointListBuilder()
        .add(0, 0)
        .add(1, 1)
        .build());
  }

  @Override
  protected BaseInterpolator getYInterpolator() {
    return new CubicSplineInterpolator(CubicSplineInterpolator.newPointListBuilder()
        .add(0, 0)
        .add(1, 1)
        .build());
  }
}
