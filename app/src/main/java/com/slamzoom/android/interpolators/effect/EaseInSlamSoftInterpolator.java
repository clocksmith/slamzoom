package com.slamzoom.android.interpolators.effect;

import com.slamzoom.android.interpolators.base.BinomialInterpolator;

/**
 * Created by antrob on 2/24/16.
 */
public class EaseInSlamSoftInterpolator extends BinomialInterpolator {
  public EaseInSlamSoftInterpolator() {
    super(2, 8, 0.2f);
  }
}
