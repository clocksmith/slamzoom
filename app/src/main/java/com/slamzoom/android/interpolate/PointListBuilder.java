package com.slamzoom.android.interpolate;

import android.graphics.PointF;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by clocksmith on 3/20/16.
 */
public class PointListBuilder {
  private List<PointF> mPointList;

  public PointListBuilder() {
    mPointList = Lists.newArrayList();
  }

  public static PointListBuilder newPointListBuilder() {
    return new PointListBuilder();
  }

  public PointListBuilder add(float x, float y) {
    mPointList.add(new PointF(x, y));
    return this;
  }

  public List<PointF> build() {
    return mPointList;
  }
}
