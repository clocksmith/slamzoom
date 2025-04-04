package com.slamzoom.android.effects.interpolation.transform.scaletranslate;

import com.slamzoom.android.effects.interpolation.transform.TransformInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.TranslateInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.BaseShakeInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.effects.interpolation.transform.scale.TeaseInterpolator;

/**
 * Created by clocksmith on 7/4/16.
 */
public class RumbleTeaseInterpolatorProvider implements TransformInterpolatorProvider {
  private Interpolator mScaleInterpolator;

  private BaseShakeInterpolatorProvider mFirstShaker;
  private BaseShakeInterpolatorProvider mSecondShaker;
  private BaseShakeInterpolatorProvider mThirdShaker;
  private BaseShakeInterpolatorProvider mFourthShaker;

  public RumbleTeaseInterpolatorProvider() {
    mScaleInterpolator = new TeaseInterpolator();
    mFirstShaker = new BaseShakeInterpolatorProvider(6, 12);
    mSecondShaker = new BaseShakeInterpolatorProvider(5, 15);
    mThirdShaker = new BaseShakeInterpolatorProvider(4, 20);
    mFourthShaker = new BaseShakeInterpolatorProvider(2, 30);
  }

  @Override
  public Interpolator getScaleInterpolator() {
    return mScaleInterpolator;
  }

  @Override
  public Interpolator getXInterpolator() {
    return new Interpolator() {
      @Override
      public float getValue(float t) {
        return getShakeInterpolator(t).getXInterpolator().getInterpolation(t);
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new Interpolator() {
      @Override
      public float getValue(float t) {
        return getShakeInterpolator(t).getYInterpolator().getInterpolation(t);
      }
    };
  }

  private TranslateInterpolatorProvider getShakeInterpolator(float t) {
    if (t < 0.25) {
      return mFirstShaker;
    } else if (t < 0.5) {
      return mSecondShaker;
    } else if (t < 0.75) {
      return mThirdShaker;
    } else {
      return mFourthShaker;
    }
  }
}
