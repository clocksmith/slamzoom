package com.slamzoom.android.interpolators.effect;

import com.slamzoom.android.interpolators.base.MonomialInterpolator;

/**
 * Created by antrob on 2/24/16.
 */
public class IdentityInterpolator extends MonomialInterpolator {
  public IdentityInterpolator(float start, float end) {
    this();
    super.setDomain(start, end);
  }

  public IdentityInterpolator() {
    super(1);
  }
}
