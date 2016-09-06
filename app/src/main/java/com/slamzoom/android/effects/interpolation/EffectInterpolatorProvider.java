package com.slamzoom.android.effects.interpolation;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.group.FilterInterpolatorsProvider;
import com.slamzoom.android.effects.interpolation.transform.ScaleInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.TranslateInterpolatorProvider;
import com.slamzoom.android.interpolators.ZeroInterpolator;
import com.slamzoom.android.interpolators.Interpolator;

import java.util.List;

/**
 * Created by clocksmith on 8/30/16.
 */
public abstract class EffectInterpolatorProvider implements
    ScaleInterpolatorProvider, TranslateInterpolatorProvider, FilterInterpolatorsProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new ZeroInterpolator();
  }

  @Override
  public Interpolator getXInterpolator() {
    return new ZeroInterpolator();
  }

  @Override
  public Interpolator getYInterpolator() {
    return new ZeroInterpolator();
  }

  @Override
  public List<FilterInterpolator> getFilterInterpolators() {
    return ImmutableList.of();
  }
}
