package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.interpolators.spline.CubicSplineInterpolator;
import com.slamzoom.android.interpolators.spline.PointListBuilder;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashBounceBottomInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashBounceDiagonalInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.filter.single.GuassianUnblurFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnsaturateFilterInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 3/22/16.
 */
public class CrashPackProvider {
  public static List<EffectTemplate> getPack() {
    List<EffectTemplate> packModels = Lists.newArrayList();
    packModels.add(EffectTemplate.newBuilder()
        .withName("crash")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.75f)
            .withDurationSeconds(0.75f)
            .withScaleAndTranslateInterpolatorProvider(new CrashInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("crashmiss")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(1f)
            .withScaleAndTranslateInterpolatorProvider(new CrashBounceDiagonalInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("crashin")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(1f)
            .withScaleAndTranslateInterpolatorProvider(new CrashBounceBottomInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("crashblur")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(0.8f)
            .withScaleInterpolator(new CubicSplineInterpolator(PointListBuilder.newPointListBuilder()
                .add(0, 0)
                .add(0.3f, 1f)
                .add(1f, 1f)
                .build()))
            .withFilterInterpolator(new GuassianUnblurFilterInterpolator(
                new CubicSplineInterpolator(PointListBuilder.newPointListBuilder()
                    .add(0, 0f)
                    .add(0.8f, 0)
                    .add(1, 1)
                    .build())))
            .withEndPauseSeconds(0.5f)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("crashfreeze")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(2f)
            .withScaleInterpolator(new CubicSplineInterpolator(PointListBuilder.newPointListBuilder()
                .add(0, 0)
                .add(0.15f, 1f)
                .add(1f, 1f)
                .build()))
            .withFilterInterpolator(new UnsaturateFilterInterpolator(
                new CubicSplineInterpolator(PointListBuilder.newPointListBuilder()
                    .add(0, 0)
                    .add(0.5f, 0)
                    .add(1f, 1f)
                    .build())))
            .withEndPauseSeconds(0.5f)
            .build())
        .build());

    return packModels;
  }
}
