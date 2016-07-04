package com.slamzoom.android.interpolators.spline;

import android.graphics.PointF;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by clocksmith on 3/20/16.
 */
public class PointsBuilder {
  private List<PointF> mPointList;

  public PointsBuilder() {
    mPointList = Lists.newArrayList();
  }

  public static PointsBuilder create() {
    return new PointsBuilder();
  }

  public PointsBuilder withPoint(float x, float y) {
    mPointList.add(new PointF(x, y));
    return this;
  }

  public List<PointF> build() {
    return mPointList;
  }
}
