package com.slamzoom.android.interpolators.spline;

import android.graphics.PointF;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.slamzoom.android.interpolators.base.Interpolator;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import java.util.List;

/**
 * Created by clocksmith on 3/15/16.
 */
public class CubicSplineInterpolator extends Interpolator {
  PolynomialSplineFunction mPolynomialSplineFunction;

  public CubicSplineInterpolator(List<PointF> pointList) {
    double[] xArray = Doubles.toArray(Lists.transform(pointList, new Function<PointF, Double>() {
      @Override
      public Double apply(PointF input) {
        return (double) input.x;
      }
    }));
    double[] yArray = Doubles.toArray(Lists.transform(pointList, new Function<PointF, Double>() {
      @Override
      public Double apply(PointF input) {
        return (double) input.y;
      }
    }));
    mPolynomialSplineFunction = new SplineInterpolator().interpolate(xArray, yArray);
  }

  @Override
  protected float getValue(float percent) {
    return (float) mPolynomialSplineFunction.value(percent);
  }
}
