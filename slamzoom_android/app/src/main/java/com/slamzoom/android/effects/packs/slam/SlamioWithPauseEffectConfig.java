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
  private static final float SLAM_DURATION = 0.6f;
  @Override
  public String getName() {
    return "slamio";
  }

  @Override
  public float getStartPauseSeconds() {
    return 2.5f;
  }

  @Override
  public float getDurationSeconds() {
    return 3.7f;
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
        float newT = t * getDurationSeconds();
        if (newT < SLAM_DURATION) {
          return new SlaminEffectConfig().getScaleInterpolator().getValue(newT / SLAM_DURATION);
        } else if (newT <= getDurationSeconds() - SLAM_DURATION) {
          return new SlaminEffectConfig().getScaleInterpolator().getValue(1);
        } else {
          return new SlamoutEffectConfig().getScaleInterpolator().getValue(
              (newT - (getDurationSeconds() - SLAM_DURATION)) / SLAM_DURATION);
        }
      }
    };
  }

  @Override
  public FilterInterpolator getFilterInterpolator() {
    return new ZoomBlurAtHotspotFilterInterpolator(new Interpolator() {
      @Override
      public float getValue(float t) {
        float newT = t * getDurationSeconds();
        if (newT < SLAM_DURATION) {
          return new SlaminEffectConfig().getFilterInterpolator().getInterpolator().getValue(newT / SLAM_DURATION);
        } else if (newT <= getDurationSeconds() - SLAM_DURATION) {
          return new SlaminEffectConfig().getFilterInterpolator().getInterpolator().getValue(1);
        } else {
          return new SlamoutEffectConfig().getFilterInterpolator().getInterpolator().getValue(
              ((newT - (getDurationSeconds() - SLAM_DURATION)) / SLAM_DURATION) * 0.99f);
        }
      }
    });
  }
}
