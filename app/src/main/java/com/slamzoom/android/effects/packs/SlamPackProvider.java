package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.templates.EffectTemplate;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.interpolators.custom.SlamSoftOutInterpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolators.spline.PointListBuilder;
import com.slamzoom.android.effects.interpolation.filter.single.ZoomBlurAtHotspotFilterInterpolator;
import com.slamzoom.android.interpolators.custom.SlamHardInAndOutInterpolator;
import com.slamzoom.android.interpolators.custom.SlamHardNoPauseInterpolator;
import com.slamzoom.android.interpolators.custom.SlamSoftOutNoPauseInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 3/22/16.
 */
public class SlamPackProvider {
  public static List<EffectTemplate> getPack() {
    List<EffectTemplate> packModels = Lists.newArrayList();

    packModels.add(EffectTemplate.newBuilder()
        .withPackName("slam pack")
        .withName("slamin")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(1.4f)
            .withDurationSeconds(0.6f)
            .withEndPauseSeconds(2)
            .withScaleInterpolator(new SlamHardNoPauseInterpolator())
            .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(new LinearSplineInterpolator(
                PointListBuilder.create()
                    .add(0, 0)
                    .add(0.9f, 1)
                    .add(0.9999f, 1)
                    .add(1, 0)
                    .build())))
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withPackName("slam pack")
        .withName("slamout")
        .addEffectStep(EffectStep.newBuilder()
//            .withStartPauseSeconds(1.2f)
//            .withDurationSeconds(0.8f)
            .withEndPauseSeconds(2)
//            .withScaleInterpolator(new SlamSoftOutNoPauseInterpolator())
            .withScaleInterpolator(new SlamSoftOutInterpolator())
            .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(new LinearSplineInterpolator(
                PointListBuilder.create()
                    .add(0, 0)
                    .add(0.8f, 1)
                    .add(1, 0)
                    .build())))
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withPackName("slam pack")
        .withName("slamio")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(1f)
            .withScaleInterpolator(new SlamHardInAndOutInterpolator())
            .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(new LinearSplineInterpolator(
                PointListBuilder.create()
                    .add(0, 0)
                    .add(0.35f, 0)
                    .add(0.45f, 1f)
                    .add(0.4999f, 1f)
                    .add(0.5f, 0)
                    .add(0.85f, 0)
                    .add(0.95f, 1f)
                    .add(0.9999f, 1f)
                    .add(1f, 0)
                    .build())))
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("slamfinity")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0)
            .withDurationSeconds(0.5f)
            .withScaleInterpolator(new SlamHardNoPauseInterpolator())
            .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(new LinearSplineInterpolator(
                PointListBuilder.create()
                    .add(0, 0)
                    .add(0.9f, 1f)
                    .add(0.9999f, 1f)
                    .add(1f, 0)
                    .build())))
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("slamfunity")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0)
            .withDurationSeconds(0.5f)
            .withScaleInterpolator(new SlamSoftOutNoPauseInterpolator())
            .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(new LinearSplineInterpolator(
                PointListBuilder.create()
                    .add(0, 0)
                    .add(0.9f, 1f)
                    .add(0.9999f, 1f)
                    .add(1f, 0)
                    .build())))
            .build())
        .build());

    return packModels;
  }
}
