package com.slamzoom.android.interpolators.spline;

import android.graphics.PointF;

import com.google.common.collect.Lists;
import com.slamzoom.android.interpolators.Interpolator;

import java.util.List;

/**
 * Created by clocksmith on 7/4/16.
 */
public class StepInterpolator extends Interpolator {
  List<PointF> mSteps;

  public StepInterpolator(List<PointF> steps) {
    mSteps = steps;
  }

  @Override
  protected float getValue(float t) {
    if (mSteps.isEmpty()) {
      return 0;
    } else {
      for (PointF step : mSteps) {
        if (t <= step.x) {
          return step.y;
        }
      }
      return mSteps.get(mSteps.size() - 1).y;
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private List<PointF> mPoints;

    public Builder() {
      mPoints = Lists.newArrayList();
    }

    public Builder withStep(float x, float y) {
      mPoints.add(new PointF(x, y));
      return this;
    }

    public StepInterpolator build() {
      return new StepInterpolator(mPoints);
    }
  }
}
