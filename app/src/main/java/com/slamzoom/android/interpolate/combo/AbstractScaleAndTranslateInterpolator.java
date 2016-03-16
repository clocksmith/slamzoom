package com.slamzoom.android.interpolate.combo;

import com.slamzoom.android.interpolate.base.BaseInterpolator;

/**
 * Created by clocksmith on 3/15/16.
 */
public abstract class AbstractScaleAndTranslateInterpolator {
  protected abstract BaseInterpolator getScaleInterpolator();

  protected abstract BaseInterpolator getXInterpolator();

  protected abstract BaseInterpolator getYInterpolator();

  public float getScaleInterpolation(float input) {
    return getScaleInterpolator().getInterpolation(input);
  }

  public float getXInterpolation(float input) {
    return getXInterpolator().getInterpolation(input);
  }

  public float getYInterpolation(float input) {
    return getYInterpolator().getInterpolation(input);
  }
}
