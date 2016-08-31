package com.slamzoom.android.effects.interpolation.transformfilter;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.ZoomBlurAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.transform.TranslateInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.NoTranslateInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.effects.interpolation.transform.scale.SlamHardNoPauseInterpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;

/**
 * Created by clocksmith on 8/30/16.
 */
public class SlaminNoPauseInterpolatorProvider extends TransformAndFilterInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new SlamHardNoPauseInterpolator();
  }

  @Override
  public TranslateInterpolatorProvider getTranslateInterpolationProvider() {
    return new NoTranslateInterpolatorProvider();
  }

  @Override
  public ImmutableList<FilterInterpolator> getFilterInterpolators() {
    return ImmutableList.<FilterInterpolator>of(
        new ZoomBlurAtHotspotFilterInterpolator(LinearSplineInterpolator.newBuilder()
            .withPoint(0, 0)
            .withPoint(0.9f, 1)
            .withPoint(0.9999f, 1)
            .withPoint(1, 0)
            .build()));
  }
}
