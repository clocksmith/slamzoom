package com.slamzoom.android.effects.packs.slam;


import com.slamzoom.android.effects.EffectConfig;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.OverExposeFilterInterpolator;
import com.slamzoom.android.effects.interpolation.transform.scale.ThreeEaseInHardOutInterpolator;
import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 9/5/16.
 */
public class FlashEffectConfig extends EffectConfig {
  @Override
  public String getName() {
    return "flash";
  }

  @Override
  public float getStartPauseSeconds() {
    return 0.6667f;
  }

  @Override
  public float getDurationSeconds() {
    return 4;
  }

  @Override
  public float getEndPauseSeconds() {
    return 0.6667f;
  }

  @Override
  public Interpolator getScaleInterpolator() {
    return new Interpolator() {
      @Override
      public float getValue(float t) {
        if (t < 0.1667f) {
          return 0;
        } else if (t < 0.5f) {
          return 0.3333f * 0.3333f;
        } else if (t < 0.8333f) {
          return 0.6667f * 0.6667f;
        } else {
          return 1;
        }
      }
    };
  }

  @Override
  public FilterInterpolator getFilterInterpolator() {
    final Interpolator base = new ThreeEaseInHardOutInterpolator();
    return new OverExposeFilterInterpolator(new Interpolator() {
      @Override
      public float getValue(float t) {
        if (0.15 < t && t < 0.1833 ||
            0.4833 < t && t < 0.5167 ||
            0.8167 < t && t < 0.85) {
          return 1;
        } else {
          return base.getValue(t);
        }
      }
    });
  }
}
