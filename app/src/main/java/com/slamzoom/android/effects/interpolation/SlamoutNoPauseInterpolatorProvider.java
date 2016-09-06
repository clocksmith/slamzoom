package com.slamzoom.android.effects.interpolation;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.ZoomBlurAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.transform.scale.SlamOutNoPauseInterpolator;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;

/**
 * Created by clocksmith on 9/5/16.
 */
public class SlamoutNoPauseInterpolatorProvider extends EffectInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new SlamOutNoPauseInterpolator();
  }

  @Override
  public ImmutableList<FilterInterpolator> getFilterInterpolators() {
    return ImmutableList.<FilterInterpolator>of(
        new ZoomBlurAtHotspotFilterInterpolator(LinearSplineInterpolator.newBuilder()
            .withPoint(0, 0)
            .withPoint(0.5f, 1)
            .withPoint(1, 0)
            .build()));
  }
}
