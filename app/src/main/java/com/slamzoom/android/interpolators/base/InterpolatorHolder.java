package com.slamzoom.android.interpolators.base;

import com.slamzoom.android.interpolators.base.Interpolator;

/**
 * Created by clocksmith on 3/21/16.
 */
public abstract class InterpolatorHolder {
  protected Interpolator mInterpolator;

  public InterpolatorHolder() {}

  public InterpolatorHolder(Interpolator interpolator) {
    mInterpolator = interpolator;
  }

  public boolean hasInterpoolator() {
    return mInterpolator != null;
  }

  public void setInterpolator(Interpolator interpolator) {
    mInterpolator = interpolator;
  }

  public Interpolator getInterpolator() {
    return mInterpolator;
  }
}
