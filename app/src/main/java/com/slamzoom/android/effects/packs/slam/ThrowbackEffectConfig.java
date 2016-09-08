package com.slamzoom.android.effects.packs.slam;

import com.slamzoom.android.effects.EffectConfig;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.DesaturateFilterInterpolator;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;

/**
 * Created by clocksmith on 9/5/16.
 */
public class ThrowbackEffectConfig extends EffectConfig {
  @Override
  public String getName() {
    return "throwback";
  }

  @Override
  public Interpolator getScaleInterpolator() {
    return LinearSplineInterpolator.newBuilder()
        .withPoint(0, 0)
        .withPoint(0.2f, 1.4f)
        .withPoint(0.45f, 1)
        .withPoint(1, 1)
        .build();
  }

  @Override
  public FilterInterpolator getFilterInterpolator() {
    return new DesaturateFilterInterpolator(LinearSplineInterpolator.newBuilder()
        .withPoint(0, 0)
        .withPoint(0.5f, 0)
        .withPoint(1, 1)
        .build());
  }
}
