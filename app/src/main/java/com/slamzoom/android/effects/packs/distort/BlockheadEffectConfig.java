package com.slamzoom.android.effects.packs.distort;

import com.slamzoom.android.effects.EffectConfig;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.group.SumoBulgeFilterInterpolator;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.SquareInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 9/6/16.
 */
public class BlockheadEffectConfig extends EffectConfig {
  @Override
  public String getName() {
    return "blockhead";
  }

  @Override
  public Interpolator getScaleInterpolator() {
    return new SquareInterpolator();
  }

  @Override
  public List<FilterInterpolator> getFilterInterpolators() {
    return new SumoBulgeFilterInterpolator().getFilterInterpolators();
  }
}