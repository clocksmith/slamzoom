package com.slamzoom.android.interpolators.spline;

import android.graphics.PointF;

import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.LinearInterpolator;


import java.util.List;

/**
 * Created by clocksmith on 3/16/16.
 */
public class LinearSplineInterpolator extends Interpolator {
  private static LinearInterpolator LINEAR_INTERPOLATOR = new LinearInterpolator();

  List<PointF> mPointList;

  public LinearSplineInterpolator(List<PointF> pointList) {
    mPointList = pointList;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private List<PointF> mPoints;

    public Builder() {
      mPoints = Lists.newArrayList();
    }

    public Builder withPoint(float x, float y) {
      mPoints.add(new PointF(x, y));
      return this;
    }

    public LinearSplineInterpolator build() {
      return new LinearSplineInterpolator(mPoints);
    }
  }

  @Override
  public float getValue(float t) {
    for (int i = 0; i < mPointList.size() - 1; i++) {
      PointF left = mPointList.get(i);
      PointF right = mPointList.get(i + 1);

      if (left.x <= t && t <= right.x) {
        float percent = (t - left.x) / (right.x - left.x);

        LINEAR_INTERPOLATOR.setRange(left.y, right.y);
        return LINEAR_INTERPOLATOR.getInterpolation(percent);
      }
    }

    return 0;
  }
}
