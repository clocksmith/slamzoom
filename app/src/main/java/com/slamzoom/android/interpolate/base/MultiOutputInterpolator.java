package com.slamzoom.android.interpolate.base;

/**
 * Created by clocksmith on 3/11/16.
 */
public abstract class MultiOutputInterpolator extends BaseInterpolator {
  public MultiOutputInterpolator() {
    super();
    setDomain(0, 1);
  }

  protected abstract double getX1(double input);

  protected abstract double getX2(double input);

  public double[] getInterpolation(double input) {
    return new double[]{getValue(getX1(input)), getValue(getX2(input))};
  }
}
