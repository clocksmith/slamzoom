package com.slamzoom.android.effects.packs.slam;

import com.slamzoom.android.effects.EffectConfig;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.ZoomBlurAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.transform.scale.SlamOutNoPauseInterpolator;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;

/**
 * Created by clocksmith on 9/5/16.
 */
public class SlamoutEffectConfig extends EffectConfig {
  @Override
  public String getName() {
    return "slamout";
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
    return new SlamOutNoPauseInterpolator();
  }

  @Override
  public FilterInterpolator getFilterInterpolator() {
    return new ZoomBlurAtHotspotFilterInterpolator(LinearSplineInterpolator.newBuilder()
        .withPoint(0, 0)
        .withPoint(0.5f, 1)
        .withPoint(1, 0)
        .build());
  }
}
