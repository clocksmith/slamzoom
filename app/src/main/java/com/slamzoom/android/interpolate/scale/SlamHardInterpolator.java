package com.slamzoom.android.interpolate.scale;

import com.slamzoom.android.interpolate.base.BaseInterpolator;
import com.slamzoom.android.interpolate.base.MonomialInterpolator;

/**
 * Created by antrob on 2/24/16.
 */
public class SlamHardInterpolator extends AbstractScaleInterpolator {
  @Override
  protected BaseInterpolator getScaleInterpolator() {
    return new MonomialInterpolator(20);
  }
}
