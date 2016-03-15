package com.slamzoom.android.interpolate.translate;

import com.slamzoom.android.interpolate.base.BaseInterpolator;

/**
 * Created by clocksmith on 3/15/16.
 */
public abstract class AbstractTranslateInterpolator {
  protected abstract BaseInterpolator getXInterpolator();

  protected abstract BaseInterpolator getYInterpolator();

  public float getXInterpolation(float input) {
    return getXInterpolator().getInterpolation(input);
  }

  public float getYInterpolation(float input) {
    return getYInterpolator().getInterpolation(input);
  }
}
