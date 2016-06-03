package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.interpolation.filter.single.ZoomBlurFilterInterpolator;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.FlushInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.SpiralInterpolatorProvider;
import com.slamzoom.android.interpolators.base.Interpolator;
import com.slamzoom.android.effects.interpolation.filter.single.ExposureFilterInterpolator;
import com.slamzoom.android.interpolators.effect.IdentityInterpolator;
import com.slamzoom.android.interpolators.effect.SlamHardNoPauseInterpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolators.spline.PointListBuilder;

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

    packModels.add(EffectTemplate.newBuilder()
        .withName("slamfinity")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0)
            .withDurationSeconds(0.6f)
            .withScaleInterpolator(new SlamHardNoPauseInterpolator())
            .withFilterInterpolator(new ZoomBlurFilterInterpolator(new LinearSplineInterpolator(
                PointListBuilder.newPointListBuilder()
                    .add(0, 0)
                    .add(0.9f, 1f)
                    .add(0.9999f, 1f)
                    .add(1f, 0)
                    .build())))
            .withEndPauseSeconds(0)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("flush")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleAndTranslateInterpolatorProvider(new FlushInterpolatorProvider())
            .withDurationSeconds(3f)
            .withEndPauseSeconds(1f)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("spiral")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleAndTranslateInterpolatorProvider(new SpiralInterpolatorProvider())
            .withDurationSeconds(3f)
            .withEndPauseSeconds(1f)
            .build())
        .build());
    return packModels;
  }
}
