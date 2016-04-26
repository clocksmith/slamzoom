package com.slamzoom.android.interpolators.effect;

import com.slamzoom.android.interpolators.base.BinomialInterpolator;

/**
 * Created by antrob on 2/24/16.
 */
public class EaseInSlamHardInterpolator extends BinomialInterpolator {
  public EaseInSlamHardInterpolator() {
    super(2, 20, 0.2f);
  }
}
