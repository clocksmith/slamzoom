package com.slamzoom.android.interpolate.scale;

import com.slamzoom.android.interpolate.base.Interpolator;
import com.slamzoom.android.interpolate.base.IdentityInterpolator;

/**
 * Created by clocksmith on 3/15/16.
 */
public class SmoothInterpolatorProvider implements ScaleInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new IdentityInterpolator();
  }
}
