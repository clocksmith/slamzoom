package com.slamzoom.android.interpolaters.combo.translate;

import com.slamzoom.android.interpolaters.base.ConstantInterpolator;
import com.slamzoom.android.interpolaters.base.Interpolator;
import com.slamzoom.android.interpolaters.combo.TranslateInterpolatorProvider;

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
