package com.slamzoom.android.effects.packs.slam;

import com.slamzoom.android.effects.EffectConfig;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.ZoomBlurAtHotspotFilterInterpolator;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.effects.interpolation.transform.scale.SlamHardNoPauseInterpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;

/**
 * Created by clocksmith on 8/30/16.
 */
public class SlaminEffectConfig extends EffectConfig {
  @Override
  public String getName() {
    return "slamin";
  }

  @Override
  public float getStartPauseSeconds() {
    return 1.4f;
  }

  @Override
  public float getDurationSeconds() {
    return 0.6f;
  }

  @Override
  public Interpolator getScaleInterpolator() {
    return new SlamHardNoPauseInterpolator();
  }

  @Override
  public FilterInterpolator getFilterInterpolator() {
    return new ZoomBlurAtHotspotFilterInterpolator(LinearSplineInterpolator.newBuilder()
        .withPoint(0, 0)
        .withPoint(0.9f, 1)
        .withPoint(0.9999f, 1)
        .withPoint(1, 0)
        .build());
  }
}
