package com.slamzoom.android.effects.packs.distort;

import com.slamzoom.android.effects.EffectConfig;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.BulgeInAtHotspotFilterInterpolator;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.LinearInterpolator;
import com.slamzoom.android.interpolators.SquareInterpolator;

/**
 * Created by clocksmith on 9/6/16.
 */
public class BulgerinoEffectConfig extends EffectConfig {
  @Override
  public String getName() {
    return "bulgerino";
  }

  @Override
  public Interpolator getScaleInterpolator() {
    return new SquareInterpolator();
  }

  @Override
  public FilterInterpolator getFilterInterpolator() {
    return new BulgeInAtHotspotFilterInterpolator();
  }
}
