package com.slamzoom.android.interpolate.scale;

import com.slamzoom.android.interpolate.base.BaseInterpolator;

/**
 * Created by antrob on 2/24/16.
 */
public abstract class AbstractScaleInterpolator {
  protected abstract BaseInterpolator getScaleInterpolator();

  public float getScaleInterpolation(float input) {
    return getScaleInterpolator().getInterpolation(input);
  }
}
