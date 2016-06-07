package com.slamzoom.android.effects.interpolation.transform.scaletranslate;

import com.slamzoom.android.effects.interpolation.transform.ScaleAndTranslateInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.TranslateInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.BaseShakeInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.custom.SlamHardNoPauseInterpolator;

/**
 * Created by clocksmith on 6/6/16.
 */
public class CrashRumbleInterpolatorProvider implements ScaleAndTranslateInterpolatorProvider {
  private static final float PERCENT_SLAMMIN = 0.5f;

  @Override
  public Interpolator getScaleInterpolator() {
    return new Interpolator() {
      private SlamHardNoPauseInterpolator mSlamInterpolator = new SlamHardNoPauseInterpolator();
      @Override
      protected float getRangePercent(float t) {
        if (t <= PERCENT_SLAMMIN) {
          return mSlamInterpolator.getInterpolation(t / PERCENT_SLAMMIN);
        } else {
          return 1;
        }
      }
    };
  }

  private TranslateInterpolatorProvider mShakeIP = new BaseShakeInterpolatorProvider(3, 3);

  @Override
  public Interpolator getXInterpolator() {
    return new Interpolator() {
      @Override
      protected float getRangePercent(float t) {
        if (t <= PERCENT_SLAMMIN) {
          return 0;
        } else {
          return mShakeIP.getXInterpolator().getInterpolation(t / (1 - PERCENT_SLAMMIN));
        }
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new Interpolator() {
      @Override
      protected float getRangePercent(float t) {
        if (t <= PERCENT_SLAMMIN) {
          return 0;
        } else {
          return mShakeIP.getYInterpolator().getInterpolation(t / (1 - PERCENT_SLAMMIN));
        }
      }
    };
  }
}
