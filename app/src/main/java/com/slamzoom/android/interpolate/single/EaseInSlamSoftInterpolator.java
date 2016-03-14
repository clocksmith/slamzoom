package com.slamzoom.android.interpolate.single;

import com.slamzoom.android.interpolate.base.BinomialSingleOutputInterpolator;

/**
 * Created by antrob on 2/24/16.
 */
public class EaseInSlamSoftInterpolator extends BinomialSingleOutputInterpolator {
  public EaseInSlamSoftInterpolator() {
    super(2, 8, 0.2);
  }
}
