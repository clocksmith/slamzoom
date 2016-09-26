package com.slamzoom.android.effects.packs.slam;

import com.slamzoom.android.effects.EffectConfig;
import com.slamzoom.android.effects.interpolation.transform.TranslateInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scale.SlaminNoPauseInterpolator;
import com.slamzoom.android.effects.interpolation.transform.translate.BaseShakeInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;

/**
 * Created by antrob on 9/25/16.
 */

public class RumblestiltskinEffectConfig extends EffectConfig {
  private static final float START_ZOOM_AT = 0.4f;
  private static final float END_ZOOM_AT = 0.5f;
  private static final float END_ZOOM_SCALE = 0.8f;

  private static final SlaminNoPauseInterpolator SLAMIN = new SlaminNoPauseInterpolator();
  private static final Interpolator AFTER_SLAMIN_SCALE = LinearSplineInterpolator.newBuilder()
      .withPoint(END_ZOOM_AT, END_ZOOM_SCALE)
      .withPoint(1, 1)
      .build();
  private static final TranslateInterpolatorProvider START_SHAKE_IP = new BaseShakeInterpolatorProvider(1, 24);
  private static final TranslateInterpolatorProvider END_SHAKE_IP = new BaseShakeInterpolatorProvider(0.5f, 24);

  @Override
  public String getName() {
    return "rumblestiltskin";
  }

  @Override
  public float getDurationSeconds() {
    return 3;
  }

  @Override
  public float getEndPauseSeconds() {
    return 0;
  }

  @Override
  public Interpolator getScaleInterpolator() {
    return new Interpolator() {
      @Override
      public float getValue(float t) {
        if (t < START_ZOOM_AT) {
          return 0;
        } else if (t < END_ZOOM_AT){
          return SLAMIN.getInterpolation((t - START_ZOOM_AT) / (END_ZOOM_AT - START_ZOOM_AT)) * END_ZOOM_SCALE;
        } else {
          return AFTER_SLAMIN_SCALE.getInterpolation(t);
        }
      }
    };
  }

  @Override
  public Interpolator getXInterpolator() {
    return new Interpolator() {
      @Override
      public float getValue(float t) {
        if (t < START_ZOOM_AT) {
          return START_SHAKE_IP.getXInterpolator().getValue(t);
        } else {
          return END_SHAKE_IP.getXInterpolator().getValue(t);
        }
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new Interpolator() {
      @Override
      public float getValue(float t) {
        if (t < START_ZOOM_AT) {
          return START_SHAKE_IP.getYInterpolator().getValue(t);
        } else {
          return END_SHAKE_IP.getYInterpolator().getValue(t);
        }
      }
    };
  }
}
