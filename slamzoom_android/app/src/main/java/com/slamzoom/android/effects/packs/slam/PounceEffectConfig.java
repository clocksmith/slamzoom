package com.slamzoom.android.effects.packs.slam;

import com.slamzoom.android.effects.EffectConfig;
import com.slamzoom.android.effects.interpolation.transform.TranslateInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scale.SlamHardInterpolator;
import com.slamzoom.android.effects.interpolation.transform.translate.ShakeInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 9/5/16.
 */
public class PounceEffectConfig extends EffectConfig {
  private static final TranslateInterpolatorProvider SHAKE_IP = new ShakeInterpolatorProvider();

  @Override
  public String getName() {
    return "pounce";
  }

  @Override
  public float getEndPauseSeconds() {
    return 1;
  }

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
