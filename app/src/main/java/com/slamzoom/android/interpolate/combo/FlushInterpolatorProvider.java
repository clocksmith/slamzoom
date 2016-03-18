package com.slamzoom.android.interpolate.combo;

import com.slamzoom.android.interpolate.base.IdentityInterpolator;
import com.slamzoom.android.interpolate.base.Interpolator;
import com.slamzoom.android.interpolate.base.LinearInterpolator;
import com.slamzoom.android.interpolate.scale.EaseInSlamHardInterpolatorProvider;
import com.slamzoom.android.interpolate.scale.EaseInSlamSoftInterpolatorProvider;
import com.slamzoom.android.interpolate.scale.SlamSoftInterpolatorProvider;

/**
 * Created by clocksmith on 3/16/16.
 */
public class FlushInterpolatorProvider implements ScaleAndTranslateInterpolatorProvider {
  @Override
  public Interpolator getScaleInterpolator() {
    return new EaseInSlamHardInterpolatorProvider().getScaleInterpolator();
  }

  @Override
  public Interpolator getXInterpolator() {
    return new Interpolator() {
      @Override
      protected float getValue(float input) {
        float newInput = (float) Math.pow(1 - input, 0.5);
        return  (float) (4 * newInput * Math.cos(32 * Math.PI * newInput));
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new Interpolator() {
      @Override
      protected float getValue(float input) {
        float newInput = (float) Math.pow(1 - input, 0.5);
        return (float) (4 * newInput * Math.sin(32 * Math.PI * newInput));
      }
    };
  }
}
