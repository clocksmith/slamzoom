package com.slamzoom.android.effects.interpolation.transformfilter;

import com.slamzoom.android.effects.interpolation.filter.group.FilterInterpolatorsProvider;
import com.slamzoom.android.effects.interpolation.transform.ScaleInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.TranslateInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 8/30/16.
 */
public abstract class TransformAndFilterInterpolatorProvider implements
    ScaleInterpolatorProvider, TranslateInterpolatorProvider, FilterInterpolatorsProvider {
  public abstract TranslateInterpolatorProvider getTranslateInterpolationProvider();

  @Override
  public Interpolator getXInterpolator() {
    return getTranslateInterpolationProvider().getXInterpolator();
  }

  @Override
  public Interpolator getYInterpolator() {
    return getTranslateInterpolationProvider().getXInterpolator();
  }
}
