package com.slamzoom.android.interpolate.scale;

import com.slamzoom.android.interpolate.base.ConstantInterpolator;
import com.slamzoom.android.interpolate.base.Interpolator;

/**
 * Created by clocksmith on 3/17/16.
 */
public class OneScaleInterpolatorProvider implements ScaleInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new ConstantInterpolator(1);
  }
}
