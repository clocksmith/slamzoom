package com.slamzoom.android.effects.interpolation.transform.translate;

import com.slamzoom.android.effects.interpolation.transform.TranslateInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CircleCenterInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 6/5/16.
 *
 * TODO(clocksmith): This could be a general "aggregate interpolator provider."
 */
public class SpiralShakeInterpolatorProvider implements TranslateInterpolatorProvider {
  private CircleCenterInterpolatorProvider mSpiralIP = new CircleCenterInterpolatorProvider();
  private SuperShakeInterpolatorProvider mShakeIP = new SuperShakeInterpolatorProvider();

  @Override
  public Interpolator getXInterpolator() {
    return new Interpolator() {
      @Override
      protected float getRangePercent(float t) {
        return mSpiralIP.getXInterpolator().getInterpolation(t) + mShakeIP.getXInterpolator().getInterpolation(t);
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new Interpolator() {
      @Override
      protected float getRangePercent(float t) {
        return mSpiralIP.getYInterpolator().getInterpolation(t) + mShakeIP.getYInterpolator().getInterpolation(t);
      }
    };
  }
}
