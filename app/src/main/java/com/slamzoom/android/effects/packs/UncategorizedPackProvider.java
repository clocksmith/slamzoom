package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.interpolators.base.Interpolator;
import com.slamzoom.android.effects.interpolation.filter.single.ExposureFilterInterpolator;
import com.slamzoom.android.interpolators.effect.IdentityInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 3/31/16.
 */
public class UncategorizedPackProvider {
  public static List<EffectTemplate> getPack() {
    List<EffectTemplate> packModels = Lists.newArrayList();
    packModels.add(EffectTemplate.newBuilder()
        .withName("lol")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(1)
            .withDurationSeconds(4f)
            .withScaleInterpolator(new Interpolator() {
              @Override
              protected float getValue(float percent) {
                if (percent < 0.1667) {
                  return 0;
                } else if (percent < 0.5) {
                  return 0.15f;
                } else if (percent < 0.8333) {
                  return 0.50f;
                } else {
                  return 1;
                }
              }
            })
            .withFilterInterpolator(new ExposureFilterInterpolator(
                new Interpolator() {
                  @Override
                  protected float getValue(float percent) {
                    return (float) Math.pow(Math.sin(3 * Math.PI * percent), 10);
                  }
                }))
            .withEndPauseSeconds(1)
            .build())
        .build());
    return packModels;
  }
}
