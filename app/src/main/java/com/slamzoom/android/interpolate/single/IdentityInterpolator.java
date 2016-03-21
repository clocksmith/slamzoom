package com.slamzoom.android.interpolate.single;

import com.slamzoom.android.interpolate.base.MonomialInterpolator;

/**
 * Created by antrob on 2/24/16.
 */
public class IdentityInterpolator extends MonomialInterpolator {
  public IdentityInterpolator() {
    super(1);
  }
}
