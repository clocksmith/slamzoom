package com.slamzoom.android.effects.interpolation.transform;

import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 3/15/16.
 */
public interface TranslateInterpolatorProvider {
  Interpolator getXInterpolator();

  Interpolator getYInterpolator();
}
