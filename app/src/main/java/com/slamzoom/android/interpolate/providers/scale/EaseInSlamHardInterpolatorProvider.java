package com.slamzoom.android.interpolate.providers.scale;

import com.slamzoom.android.interpolate.Interpolator;
import com.slamzoom.android.interpolate.BinomialInterpolator;

/**
 * Created by antrob on 2/24/16.
 */
public class EaseInSlamHardInterpolatorProvider implements ScaleInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new BinomialInterpolator(2, 20, 0.2f);
  }
}
