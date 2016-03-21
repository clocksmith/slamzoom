package com.slamzoom.android.interpolate.single;

import com.slamzoom.android.interpolate.Interpolator;
import com.slamzoom.android.interpolate.MonomialInterpolator;
import com.slamzoom.android.interpolate.combo.ScaleInterpolatorProvider;

/**
 * Created by antrob on 2/24/16.
 */
public class SlamSoftInterpolator extends MonomialInterpolator {
  public SlamSoftInterpolator() {
    super(10);
  }
}
