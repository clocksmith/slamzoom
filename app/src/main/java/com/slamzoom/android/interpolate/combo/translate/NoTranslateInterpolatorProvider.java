package com.slamzoom.android.interpolate.combo.translate;

import com.slamzoom.android.interpolate.ConstantInterpolator;
import com.slamzoom.android.interpolate.Interpolator;
import com.slamzoom.android.interpolate.combo.TranslateInterpolatorProvider;

/**
 * Created by clocksmith on 3/17/16.
 */
public class NoTranslateInterpolatorProvider implements TranslateInterpolatorProvider {
  @Override
  public Interpolator getXInterpolator() {
    return new ConstantInterpolator(0);
  }

  @Override
  public Interpolator getYInterpolator() {
    return new ConstantInterpolator(0);
  }
}
