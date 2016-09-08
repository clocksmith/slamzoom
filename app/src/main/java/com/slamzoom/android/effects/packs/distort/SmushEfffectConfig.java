package com.slamzoom.android.effects.packs.distort;

import com.slamzoom.android.effects.EffectConfig;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlAtHotspotOnHotspotFilterInterpolator;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.LinearInterpolator;


/**
 * Created by clocksmith on 9/6/16.
 */
public class SmushEfffectConfig extends EffectConfig {
  @Override
  public String getName() {
    return "smush";
  }

  @Override
  public Interpolator getScaleInterpolator() {
    return new LinearInterpolator();
  }

  @Override
  public FilterInterpolator getFilterInterpolator() {
    return new UnswirlAtHotspotOnHotspotFilterInterpolator();
  }
}