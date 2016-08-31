package com.slamzoom.android.effects.interpolation.transform.scale;

import com.slamzoom.android.interpolators.BinomialInterpolator;

/**
 * Created by antrob on 2/24/16.
 */
public class EaseInSlamHardInterpolator extends BinomialInterpolator {
  public EaseInSlamHardInterpolator() {
    super(2, 20, 0.2f);
  }
}
