package com.slamzoom.android.effects.packs.distort;

import com.slamzoom.android.effects.EffectConfig;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeLeftRightSwapFilterInterpolationGroup;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.LinearInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 9/6/16.
 */
public class BulgeSwapEffectConfig extends EffectConfig {
  @Override
  public String getName() {
    return "bulgeswap";
  }

  @Override
  public Interpolator getScaleInterpolator() {
    return new LinearInterpolator();
  }

  @Override
  public List<FilterInterpolator> getFilterInterpolators() {
    return new BulgeLeftRightSwapFilterInterpolationGroup().getFilterInterpolators();
  }
}