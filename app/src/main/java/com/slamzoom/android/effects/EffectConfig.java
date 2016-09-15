package com.slamzoom.android.effects;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.group.FilterInterpolatorsProvider;
import com.slamzoom.android.effects.interpolation.transform.ScaleInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.TranslateInterpolatorProvider;
import com.slamzoom.android.interpolators.ConstantInterpolator;
import com.slamzoom.android.interpolators.ZeroInterpolator;
import com.slamzoom.android.interpolators.Interpolator;

import java.util.List;

/**
 * Created by clocksmith on 8/30/16.
 */
public abstract class EffectConfig implements
    ScaleInterpolatorProvider, TranslateInterpolatorProvider, FilterInterpolatorsProvider {
  public String getName() {
    return "NAME ME!";
  }

  public float getStartPauseSeconds() {
    return Constants.DEFAULT_START_PAUSE_SECONDS;
  }

  public float getDurationSeconds() {
    return Constants.DEFAULT_DURATION_SECONDS;
  }

  public float getEndPauseSeconds() {
    return Constants.DEFAULT_END_PAUSE_SECONDS;
  }

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
    if (getFilterInterpolator() != null) {
      return ImmutableList.of(getFilterInterpolator());
    } else {
      return ImmutableList.of();
    }
  }

  public FilterInterpolator getFilterInterpolator() {
    return null;
  }
}
