package com.slamzoom.android.interpolate.single;

import com.slamzoom.android.interpolate.Interpolator;
import com.slamzoom.android.interpolate.BinomialInterpolator;

/**
 * Created by antrob on 2/24/16.
 */
public class EaseInSlamHardInterpolator extends BinomialInterpolator {
  public EaseInSlamHardInterpolator() {
    super(2, 20, 0.2f);
  }
}
