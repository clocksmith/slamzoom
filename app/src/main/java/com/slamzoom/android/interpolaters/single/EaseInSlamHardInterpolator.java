package com.slamzoom.android.interpolaters.single;

import com.slamzoom.android.interpolaters.base.BinomialInterpolator;

/**
 * Created by antrob on 2/24/16.
 */
public class EaseInSlamHardInterpolator extends BinomialInterpolator {
  public EaseInSlamHardInterpolator() {
    super(2, 20, 0.2f);
  }
}
