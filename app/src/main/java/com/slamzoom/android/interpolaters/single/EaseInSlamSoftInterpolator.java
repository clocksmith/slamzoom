package com.slamzoom.android.interpolaters.single;

import com.slamzoom.android.interpolaters.base.BinomialInterpolator;

/**
 * Created by antrob on 2/24/16.
 */
public class EaseInSlamSoftInterpolator extends BinomialInterpolator {
  public EaseInSlamSoftInterpolator() {
    super(2, 8, 0.2f);
  }
}
