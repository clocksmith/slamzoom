package com.slamzoom.android.interpolators;

/**
 * Created by antrob on 9/25/16.
 */

public class SquareInterpolator extends MonomialInterpolator {
  public SquareInterpolator(float start, float end) {
    this();
    setRange(start, end);
  }

  public SquareInterpolator() {
    super(2);
  }
}
