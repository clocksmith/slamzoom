package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.interpolation.filter.single.UnsaturateFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.ZoomBlurAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.transform.translate.ShakeInterpolatorProvider;
import com.slamzoom.android.interpolators.LinearInterpolator;
import com.slamzoom.android.interpolators.custom.OutAndInInterpolator;
import com.slamzoom.android.interpolators.custom.SlamHardInterpolator;
import com.slamzoom.android.interpolators.spline.CubicSplineInterpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolators.spline.PointListBuilder;

import java.util.List;

/**
 * Created by clocksmith on 6/4/16.
 */
public class GrayPackProvider {
  public static List<EffectTemplate> getPack() {
    List<EffectTemplate> packModels = Lists.newArrayList();

    packModels.add(EffectTemplate.newBuilder()
        .withName("grayson")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolator(new UnsaturateFilterInterpolator())
            .withEndPauseSeconds(1f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("grayslam")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new SlamHardInterpolator())
            .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(new LinearSplineInterpolator(
                PointListBuilder.create()
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

    packModels.add(EffectTemplate.newBuilder()
        .withName("grayfreeze")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(2f)
            .withScaleInterpolator(new CubicSplineInterpolator(PointListBuilder.create()
                .add(0, 0)
                .add(0.15f, 1f)
                .add(1f, 1f)
                .build()))
            .withFilterInterpolator(new UnsaturateFilterInterpolator(
                new CubicSplineInterpolator(PointListBuilder.create()
                    .add(0, 0)
                    .add(0.5f, 0)
                    .add(1f, 1f)
                    .build())))
            .withEndPauseSeconds(1f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("grayrumble")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new LinearInterpolator())
            .withTranslateInterpolator(new ShakeInterpolatorProvider())
            .withFilterInterpolator(new UnsaturateFilterInterpolator())
            .withEndPauseSeconds(1f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("graytake")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new OutAndInInterpolator())
            .withFilterInterpolator(new UnsaturateFilterInterpolator(new LinearInterpolator()))
            .withEndPauseSeconds(1f)
            .build())
        .build());

    return packModels;
  }
}
