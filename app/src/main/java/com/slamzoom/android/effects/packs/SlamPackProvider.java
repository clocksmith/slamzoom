package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolators.spline.PointListBuilder;
import com.slamzoom.android.effects.interpolation.filter.single.GuassianUnblurFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnsaturateFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.ZoomBlurFilterInterpolator;
import com.slamzoom.android.interpolators.effect.SlamHardInAndOutInterpolator;
import com.slamzoom.android.interpolators.effect.SlamHardInterpolator;
import com.slamzoom.android.interpolators.effect.SlamHardNoPauseInterpolator;
import com.slamzoom.android.interpolators.effect.SlamSoftOutNoPauseInterpolator;

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
            .withScaleInterpolator(new SlamHardNoPauseInterpolator())
            .withFilterInterpolator(new ZoomBlurFilterInterpolator(new LinearSplineInterpolator(
                PointListBuilder.newPointListBuilder()
                    .add(0, 0)
                    .add(0.9f, 1f)
                    .add(0.9999f, 1f)
                    .add(1f, 0)
                    .build())))
            .withStartPauseSeconds(1.4f)
            .withDurationSeconds(0.6f)
//            .withDurationSeconds(6f)
            .withEndPauseSeconds(1f)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withPackName("slam pack")
        .withName("slamout")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new SlamSoftOutNoPauseInterpolator())
            .withFilterInterpolator(new ZoomBlurFilterInterpolator(new LinearSplineInterpolator(
                PointListBuilder.newPointListBuilder()
                    .add(0, 0)
                    .add(0.8f, 1f)
                    .add(1f, 0)
                    .build())))
            .withStartPauseSeconds(1.2f)
            .withDurationSeconds(0.8f)
            .withEndPauseSeconds(1f)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withPackName("slam pack")
        .withName("slamio")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new SlamHardInAndOutInterpolator())
            .withFilterInterpolator(new ZoomBlurFilterInterpolator(new LinearSplineInterpolator(
                PointListBuilder.newPointListBuilder()
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
            .withDurationSeconds(1f)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("blurslam")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new SlamHardInterpolator())
            .withFilterInterpolator(new GuassianUnblurFilterInterpolator())
            .withEndPauseSeconds(1f)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("grayslam ")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new SlamHardInterpolator())
            .withFilterInterpolator(new ZoomBlurFilterInterpolator(new LinearSplineInterpolator(
                PointListBuilder.newPointListBuilder()
                    .add(0, 0)
                    .add(0.7f, 0)
                    .add(0.9f, 1f)
                    .add(0.9999f, 1f)
                    .add(1f, 0)
                    .build())))
            .withFilterInterpolator(new UnsaturateFilterInterpolator())
            .withEndPauseSeconds(1f)
            .build())
        .build());

    return packModels;
  }
}
