package com.slamzoom.android.effects.interpolation.transform.scaletranslate;

import com.slamzoom.android.effects.interpolation.transform.ScaleAndTranslateInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.TranslateInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.BaseShakeInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 6/6/16.
 */
public class CrashRumbleInterpolatorProvider implements ScaleAndTranslateInterpolatorProvider {
  private static final float PERCENT_CRASH = 0.5f;

  @Override
  public Interpolator getScaleInterpolator() {
    return new Interpolator() {
      @Override
      protected float getRangePercent(float t) {
        if (t <= PERCENT_CRASH) {
          return mCrashIP.getScaleInterpolator().getInterpolation(t / PERCENT_CRASH);
        } else {
          return 1;
        }
      }
    };
  }

  private CrashMissInterpolatorProvider mCrashIP = new CrashMissInterpolatorProvider();
  private TranslateInterpolatorProvider mShakeIP = new BaseShakeInterpolatorProvider(3, 3);

  @Override
  public Interpolator getXInterpolator() {
    return new Interpolator() {
      @Override
      protected float getRangePercent(float t) {
        if (t <= PERCENT_CRASH) {
          return mCrashIP.getXInterpolator().getInterpolation(t / PERCENT_CRASH);
        } else {
          return mShakeIP.getXInterpolator().getInterpolation(t / (1 - PERCENT_CRASH));
        }
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new Interpolator() {
      @Override
      protected float getRangePercent(float t) {
        if (t <= PERCENT_CRASH) {
          return mCrashIP.getYInterpolator().getInterpolation(t / PERCENT_CRASH);
        } else {
          return mShakeIP.getYInterpolator().getInterpolation(t / (1 - PERCENT_CRASH));
        }
      }
    };
  }
}
