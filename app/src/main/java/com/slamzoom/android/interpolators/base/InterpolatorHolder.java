package com.slamzoom.android.interpolators.base;

/**
 * Created by clocksmith on 3/21/16.
 *
 * Holds an interpolator.
 */
public abstract class InterpolatorHolder {
  protected Interpolator mInterpolator;

  /**
   * Default constructor creates a holder with no interpoaltor.
   */
  public InterpolatorHolder() {}

  public InterpolatorHolder(Interpolator interpolator) {
    mInterpolator = interpolator;
  }

  public boolean hasInterpolator() {
    return mInterpolator != null;
  }

  public void setInterpolator(Interpolator interpolator) {
    mInterpolator = interpolator;
  }

  public Interpolator getInterpolator() {
    return mInterpolator;
  }
}
