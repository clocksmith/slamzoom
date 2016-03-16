package com.slamzoom.android.interpolate.scale;

import com.slamzoom.android.interpolate.base.Interpolator;
import com.slamzoom.android.interpolate.base.MonomialInterpolator;

/**
 * Created by antrob on 2/24/16.
 */
public class SlamSoftInterpolatorProvider implements ScaleInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new MonomialInterpolator(10);
  }
}
