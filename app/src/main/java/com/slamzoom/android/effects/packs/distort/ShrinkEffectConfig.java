package com.slamzoom.android.effects.packs.distort;

import com.slamzoom.android.effects.EffectConfig;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.group.DeflateFaceFilterInterpolatorsProvider;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.LinearInterpolator;
import com.slamzoom.android.interpolators.SquareInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 9/6/16.
 */
public class ShrinkEffectConfig extends EffectConfig {
  @Override
  public String getName() {
    return "shrink";
  }

  @Override
  public Interpolator getScaleInterpolator() {
    return new SquareInterpolator();
  }

  @Override
  public List<FilterInterpolator> getFilterInterpolators() {
    return new DeflateFaceFilterInterpolatorsProvider().getFilterInterpolators();
  }
}