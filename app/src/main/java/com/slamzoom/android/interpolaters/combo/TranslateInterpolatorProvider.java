package com.slamzoom.android.interpolaters.combo;

import com.slamzoom.android.interpolaters.base.Interpolator;

/**
 * Created by clocksmith on 3/15/16.
 */
public interface TranslateInterpolatorProvider {
  Interpolator getXInterpolator();

  Interpolator getYInterpolator();
}
