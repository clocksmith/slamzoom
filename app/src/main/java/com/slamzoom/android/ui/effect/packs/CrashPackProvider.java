package com.slamzoom.android.ui.effect.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.interpolate.base.spline.CubicSplineInterpolator;
import com.slamzoom.android.interpolate.base.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolate.base.spline.PointListBuilder;
import com.slamzoom.android.interpolate.combo.scaletranslate.CrashBounceBottomInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.scaletranslate.CrashBounceDiagonalInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.scaletranslate.CrashInterpolatorProvider;
import com.slamzoom.android.interpolate.filter.GuassianUnblurFilterInterpolator;
import com.slamzoom.android.interpolate.filter.UnsaturateFilterInterpolator;
import com.slamzoom.android.interpolate.single.OvershootInterpolator;
import com.slamzoom.android.ui.effect.EffectModel;
import com.slamzoom.android.ui.effect.EffectStep;

import java.util.List;

/**
 * Created by clocksmith on 3/22/16.
 */
public class CrashPackProvider {
  public static List<EffectModel> getPack() {
    List<EffectModel> packModels = Lists.newArrayList();
    packModels.add(EffectModel.newBuilder()
        .withName("crash")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.3f)
            .withDurationSeconds(0.7f)
            .withScaleAndTranslateInterpolatorProvider(new CrashInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectModel.newBuilder()
        .withName("crashmiss")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(1f)
            .withScaleAndTranslateInterpolatorProvider(new CrashBounceDiagonalInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectModel.newBuilder()
        .withName("crashin")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(1f)
            .withScaleAndTranslateInterpolatorProvider(new CrashBounceBottomInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectModel.newBuilder()
        .withName("crashblur")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(1f)
            .withScaleInterpolator(new LinearSplineInterpolator(PointListBuilder.newPointListBuilder()
                .add(0, 0)
                .add(0.3f, 1f)
                .add(1f, 1f)
                .build()))
            .withFilterInterpolator(new GuassianUnblurFilterInterpolator(
                new CubicSplineInterpolator(PointListBuilder.newPointListBuilder()
                    .add(0, 1f)
                    .add(0.2f, 0)
                    .add(0.8f, 0)
                    .add(1, 1)
                    .build())))
            .withEndPauseSeconds(0.5f)
            .build())
        .build());
    packModels.add(EffectModel.newBuilder()
        .withName("crashfreeze")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(2f)
            .withScaleInterpolator(new CubicSplineInterpolator(PointListBuilder.newPointListBuilder()
                .add(0, 0)
                .add(0.3f, 1)
                .add(0.4f, 0)
                .add(1f, 0)
                .build()))
            .withFilterInterpolator(new UnsaturateFilterInterpolator(
                new CubicSplineInterpolator(PointListBuilder.newPointListBuilder()
                    .add(0, 1f)
                    .add(0.4f, 1)
                    .add(1, 1)
                    .build())))
            .withEndPauseSeconds(0.5f)
            .build())
        .build());

    return packModels;
  }
}
