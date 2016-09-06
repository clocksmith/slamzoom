package com.slamzoom.android.effects.interpolation;

import com.slamzoom.android.effects.interpolation.transform.TranslateInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scale.SlamHardInterpolator;
import com.slamzoom.android.effects.interpolation.transform.translate.ShakeInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 9/5/16.
 */
public class RumbleslamInterpolatorProvider extends EffectInterpolatorProvider {
  private static final TranslateInterpolatorProvider SHAKE_IP = new ShakeInterpolatorProvider();

  @Override
  public Interpolator getScaleInterpolator() {
    return new SlamHardInterpolator();
  }

  @Override
  public Interpolator getXInterpolator() {
    return SHAKE_IP.getXInterpolator();
  }

  @Override
  public Interpolator getYInterpolator() {
    return SHAKE_IP.getYInterpolator();
  }
}
