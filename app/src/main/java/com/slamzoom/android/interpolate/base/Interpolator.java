package com.slamzoom.android.interpolate.base;

import android.graphics.PointF;

import com.google.common.collect.Lists;

import java.util.List;

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

  public static PointListBuilder newPointListBuilder() {
    return new PointListBuilder();
  }

  public static class PointListBuilder {
    private List<PointF> mPointList;

    public PointListBuilder() {
      mPointList = Lists.newArrayList();
    }

    public PointListBuilder add(float x, float y) {
      mPointList.add(new PointF(x, y));
      return this;
    }

    public List<PointF> build() {
      return mPointList;
    }
  }
}
