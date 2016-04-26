package com.slamzoom.android.effects.interpolation.transform.base;

import com.slamzoom.android.interpolators.base.Interpolator;

/**
 * Created by clocksmith on 3/15/16.
 */
public interface TranslateInterpolatorProvider {
  Interpolator getXInterpolator();

  Interpolator getYInterpolator();
}
