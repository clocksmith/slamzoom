package com.slamzoom.android.interpolaters.base.spline;

import android.graphics.PointF;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.slamzoom.android.interpolaters.base.Interpolator;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import java.util.List;

/**
 * Created by clocksmith on 3/16/16.
 */
public class LinearSplineInterpolator extends Interpolator {
  PolynomialSplineFunction mPolynomialSplineFunction;

  public LinearSplineInterpolator(List<PointF> pointList) {
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
    mPolynomialSplineFunction = new LinearInterpolator().interpolate(xArray, yArray);
  }

  @Override
  protected float getValue(float input) {
    return (float) mPolynomialSplineFunction.value(input);
  }
}
