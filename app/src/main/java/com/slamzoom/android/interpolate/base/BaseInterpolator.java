package com.slamzoom.android.interpolate.base;

/**
 * Created by clocksmith on 3/11/16.
 */
public abstract class BaseInterpolator {
  private double start;
  private double end;

  public BaseInterpolator() {}

  public void setDomain(double start, double end) {
    this.start = start;
    this.end = end;
  }

  public double getStart() {
    return start;
  }

  public double getEnd() {
    return end;
  }

  protected double getValue(double x) {
    return getStart() + x * (getEnd() - getStart());
  }
}
