package com.slamzoom.android.interpolate.base;

/**
 * Created by clocksmith on 3/11/16.
 */
public abstract class BaseInterpolator {
  private float start;
  private float end;

  public BaseInterpolator() {
    setDomain(0, 1);
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
