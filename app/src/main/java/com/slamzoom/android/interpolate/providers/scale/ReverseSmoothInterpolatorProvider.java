package com.slamzoom.android.interpolate.providers.scale;

import com.slamzoom.android.interpolate.Interpolator;
import com.slamzoom.android.interpolate.ReverseIdentityInterpolator;

/**
 * Created by clocksmith on 3/15/16.
 */
public class ReverseSmoothInterpolatorProvider implements ScaleInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new ReverseIdentityInterpolator();
  }
}