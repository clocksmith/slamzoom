package com.slamzoom.android.ui.effect.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.interpolate.base.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolate.base.spline.PointListBuilder;
import com.slamzoom.android.interpolate.filter.GuassianUnblurFilterInterpolator;
import com.slamzoom.android.interpolate.filter.UnsaturateFilterInterpolator;
import com.slamzoom.android.interpolate.filter.ZoomBlurFilterInterpolator;
import com.slamzoom.android.interpolate.single.SlamHardInAndOut2Interpolator;
import com.slamzoom.android.interpolate.single.SlamHardInAndOutInterpolator;
import com.slamzoom.android.interpolate.single.SlamHardInterpolator;
import com.slamzoom.android.interpolate.single.SlamSoftOutInterpolator;
import com.slamzoom.android.ui.effect.EffectModel;
import com.slamzoom.android.ui.effect.EffectStep;

import java.util.List;

/**
 * Created by clocksmith on 3/22/16.
 */
public class SlamPackProvider {
  public static List<EffectModel> getPack() {
    List<EffectModel> packModels = Lists.newArrayList();

    packModels.add(EffectModel.newBuilder()
        .withPackName("slam pack")
        .withName("slamzoom")
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
            .withEndPauseSeconds(1f)
            .build())
        .build());
    packModels.add(EffectModel.newBuilder()
        .withPackName("slam pack")
        .withName("sz out")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new SlamSoftOutInterpolator())
            .withFilterInterpolator(new ZoomBlurFilterInterpolator(new LinearSplineInterpolator(
                PointListBuilder.newPointListBuilder()
                    .add(0, 0)
                    .add(0.7f, 0)
                    .add(0.9f, 1f)
                    .add(0.9999f, 1f)
                    .add(1f, 0)
                    .build())))
            .withEndPauseSeconds(1f)
            .build())
        .build());
    packModels.add(EffectModel.newBuilder()
        .withPackName("slam pack")
        .withName("slamioio")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new SlamHardInAndOut2Interpolator())
            .withFilterInterpolator(new ZoomBlurFilterInterpolator(new LinearSplineInterpolator(
                PointListBuilder.newPointListBuilder()
                    .add(0, 0)
                    .add(0.175f, 0)
                    .add(0.225f, 1f)
                    .add(0.2499f, 1f)
                    .add(0.25f, 0)

                    .add(0.425f, 0)
                    .add(0.475f, 1f)
                    .add(0.4999f, 1f)
                    .add(0.5f, 0)

                    .add(0.675f, 0)
                    .add(0.725f, 1f)
                    .add(0.7499f, 1f)
                    .add(0.75f, 0f)

                    .add(0.925f, 0)
                    .add(0.975f, 1f)
                    .add(0.9999f, 1f)
                    .add(1f, 0)
                    .build())))
            .build())
        .build());
    packModels.add(EffectModel.newBuilder()
        .withName("slamblur")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new SlamHardInterpolator())
            .withFilterInterpolator(new GuassianUnblurFilterInterpolator())
            .build())
        .build());
    packModels.add(EffectModel.newBuilder()
        .withName("slamsaturate")
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
