package com.slamzoom.android.interpolate.translate;

import com.slamzoom.android.interpolate.base.Interpolator;

/**
 * Created by clocksmith on 3/15/16.
 */
public interface TranslateInterpolatorProvider {
  Interpolator getXInterpolator();

  Interpolator getYInterpolator();
}
