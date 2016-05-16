package com.slamzoom.android.interpolators.base;

/**
 * Created by clocksmith on 3/11/16.
 */
public abstract class Interpolator implements Cloneable {
  private float start;
  private float end;

  public Interpolator() {
    setDomain(0, 1);
  }

  public Interpolator(float start, float end) {
    setDomain(start, end);
  }

  @Override
  public Interpolator clone() throws CloneNotSupportedException {
    Interpolator clone = (Interpolator) super.clone();
    clone.setDomain(0, 1);
    return clone;
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

  public float getInterpolation(float percent) {
    return getStart() + getValue(percent) * (getEnd() - getStart());
  }

  protected abstract float getValue(float percent);
}
