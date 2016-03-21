package com.slamzoom.android.interpolate.single;

import com.slamzoom.android.interpolate.Interpolator;
import com.slamzoom.android.interpolate.BinomialInterpolator;

/**
 * Created by antrob on 2/24/16.
 */
public class EaseInSlamSoftInterpolator extends BinomialInterpolator {
  public EaseInSlamSoftInterpolator() {
    super(2, 8, 0.2f);
  }
}
