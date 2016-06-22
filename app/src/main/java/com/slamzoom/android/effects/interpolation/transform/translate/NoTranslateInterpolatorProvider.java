package com.slamzoom.android.effects.interpolation.transform.translate;

import com.slamzoom.android.effects.interpolation.transform.TranslateInterpolatorProvider;
import com.slamzoom.android.interpolators.ConstantInterpolator;
import com.slamzoom.android.interpolators.Interpolator;

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
