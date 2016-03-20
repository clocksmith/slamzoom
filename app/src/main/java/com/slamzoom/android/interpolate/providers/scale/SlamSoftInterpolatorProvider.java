package com.slamzoom.android.interpolate.providers.scale;

import com.slamzoom.android.interpolate.Interpolator;
import com.slamzoom.android.interpolate.MonomialInterpolator;

/**
 * Created by antrob on 2/24/16.
 */
public class SlamSoftInterpolatorProvider implements ScaleInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new MonomialInterpolator(10);
  }
}
