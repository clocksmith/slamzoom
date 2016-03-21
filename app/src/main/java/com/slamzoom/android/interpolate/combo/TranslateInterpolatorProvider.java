package com.slamzoom.android.interpolate.combo;

import com.slamzoom.android.interpolate.base.Interpolator;

/**
 * Created by clocksmith on 3/15/16.
 */
public interface TranslateInterpolatorProvider {
  Interpolator getXInterpolator();

  Interpolator getYInterpolator();
}
