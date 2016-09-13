package com.slamzoom.android.effects.packs.slam;

import com.slamzoom.android.effects.EffectConfig;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.ZoomBlurAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.transform.scale.SlamHardNoPauseInterpolator;
import com.slamzoom.android.interpolators.ConstantInterpolator;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.LinearInterpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;

/**
 * Created by clocksmith on 9/13/16.
 */
public class SlamioWithPauseEffectConfig extends EffectConfig {
  @Override
  public String getName() {
    return "slamio";
  }

  @Override
  public float getStartPauseSeconds() {
    return 1f;
  }

  @Override
  public float getDurationSeconds() {
    return 2.2f;
  }

  @Override
  public float getEndPauseSeconds() {
    return 0;
  }

  @Override
  public Interpolator getScaleInterpolator() {
    return new Interpolator() {
      @Override
      public float getValue(float t) {
        float newT = t * 2.2f;
        if (newT < 0.6f) {
          return new SlaminEffectConfig().getScaleInterpolator().getValue(newT / 0.6f);
        } else if (newT < 1.6f) {
          return new SlaminEffectConfig().getScaleInterpolator().getValue(1);
        } else {
          return new SlamoutEffectConfig().getScaleInterpolator().getValue((newT - 1.6f) / 0.6f);
        }
      }
    };
  }

  @Override
  public FilterInterpolator getFilterInterpolator() {
    return new ZoomBlurAtHotspotFilterInterpolator(new Interpolator() {
      @Override
      public float getValue(float t) {
        float newT = t * 2.2f;
        if (newT < 0.6f) {
          return new SlaminEffectConfig().getFilterInterpolator().getInterpolator().getValue(newT / 0.6f);
        } else if (newT < 1.6f) {
          return new SlaminEffectConfig().getFilterInterpolator().getInterpolator().getValue(1);
        } else {
          return new SlamoutEffectConfig().getFilterInterpolator().getInterpolator().getValue((newT - 1.6f) / 0.6f);
        }
      }
    });
  }
}
