package com.slamzoom.android.interpolate.base;

import com.google.common.base.Objects;

/**
 * Created by clocksmith on 3/11/16.
 */
public abstract class Interpolator {
  private float start;
  private float end;

  public Interpolator() {
    setDomain(0, 1);
  }

  public Interpolator(float start, float end) {
    setDomain(start, end);
  }

  public void setDomain(float start, float end) {
    this.start = start;
    this.end = end;
  }

  public float getStart() {
    return start;
  }

  public float getEnd() {
    return end;
  }

  public float getInterpolation(float input) {
    return getStart() + getValue(input) * (getEnd() - getStart());
  }

  protected abstract float getValue(float input);
}
