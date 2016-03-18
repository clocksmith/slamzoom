package com.slamzoom.android.interpolate.scale;

import com.slamzoom.android.interpolate.base.Interpolator;
import com.slamzoom.android.interpolate.base.ReverseIdentityInterpolator;

/**
 * Created by clocksmith on 3/15/16.
 */
public class ReverseSmoothInterpolatorProvider implements ScaleInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new ReverseIdentityInterpolator();
  }
}