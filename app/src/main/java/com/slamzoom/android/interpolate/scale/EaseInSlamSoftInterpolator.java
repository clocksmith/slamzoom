package com.slamzoom.android.interpolate.scale;

import com.slamzoom.android.interpolate.base.BaseInterpolator;
import com.slamzoom.android.interpolate.base.BinomialInterpolator;

/**
 * Created by antrob on 2/24/16.
 */
public class EaseInSlamSoftInterpolator extends AbstractScaleInterpolator {
  @Override
  protected BaseInterpolator getScaleInterpolator() {
    return new BinomialInterpolator(2, 8, 0.2f);
  }
}
