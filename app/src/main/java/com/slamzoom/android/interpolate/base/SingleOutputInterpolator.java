package com.slamzoom.android.interpolate.base;

/**
 * Created by antrob on 2/24/16.
 */
public abstract class SingleOutputInterpolator extends BaseInterpolator {
  public SingleOutputInterpolator() {}

  protected abstract double getX(double input);

  public double getInterpolation(double input) {
    return getValue(getX(input));
  }
}
