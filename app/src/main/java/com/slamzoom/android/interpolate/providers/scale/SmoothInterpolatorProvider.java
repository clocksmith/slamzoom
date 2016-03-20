package com.slamzoom.android.interpolate.providers.scale;

import com.slamzoom.android.interpolate.Interpolator;
import com.slamzoom.android.interpolate.IdentityInterpolator;

/**
 * Created by clocksmith on 3/15/16.
 */
public class SmoothInterpolatorProvider implements ScaleInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new IdentityInterpolator();
  }
}
