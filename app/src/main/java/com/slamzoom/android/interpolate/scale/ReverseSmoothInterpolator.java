package com.slamzoom.android.interpolate.scale;

import com.slamzoom.android.interpolate.base.BaseInterpolator;
import com.slamzoom.android.interpolate.base.LinearInterpolator;

/**
 * Created by clocksmith on 3/15/16.
 */
public class ReverseSmoothInterpolator extends AbstractScaleInterpolator {
  @Override
  protected BaseInterpolator getScaleInterpolator() {
    return new ReverseSmoothInterpolator();
  }
}