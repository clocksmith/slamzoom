package com.slamzoom.android.interpolate.providers.scale;

import com.slamzoom.android.interpolate.ConstantInterpolator;
import com.slamzoom.android.interpolate.Interpolator;

/**
 * Created by clocksmith on 3/17/16.
 */
public class OneScaleInterpolatorProvider implements ScaleInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new ConstantInterpolator(1);
  }
}
