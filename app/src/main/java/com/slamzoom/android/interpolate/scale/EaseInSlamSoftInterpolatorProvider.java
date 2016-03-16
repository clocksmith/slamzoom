package com.slamzoom.android.interpolate.scale;

import com.slamzoom.android.interpolate.base.Interpolator;
import com.slamzoom.android.interpolate.base.BinomialInterpolator;

/**
 * Created by antrob on 2/24/16.
 */
public class EaseInSlamSoftInterpolatorProvider implements ScaleInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new BinomialInterpolator(2, 8, 0.2f);
  }
}
