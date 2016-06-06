package com.slamzoom.android.effects.interpolation.transform.translate;

import com.slamzoom.android.effects.interpolation.transform.base.TranslateInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.SpiralInterpolatorProvider;
import com.slamzoom.android.interpolators.base.Interpolator;

/**
 * Created by clocksmith on 6/5/16.
 *
 * TODO(clocksmith): This could be a general "aggregate interpolator provider."
 */
public class SpiralShakeInterpolatorProvider implements TranslateInterpolatorProvider {
  private SpiralInterpolatorProvider mSpiralIP = new SpiralInterpolatorProvider();
  private ShakeInterpolatorProvider mShakeIP = new ShakeInterpolatorProvider();

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
