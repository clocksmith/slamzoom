package com.slamzoom.android.ui.effect.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.interpolate.base.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolate.base.spline.PointListBuilder;
import com.slamzoom.android.interpolate.filter.GuassianUnblurFilterInterpolator;
import com.slamzoom.android.interpolate.filter.UnsaturateFilterInterpolator;
import com.slamzoom.android.interpolate.filter.ZoomBlurFilterInterpolator;
import com.slamzoom.android.interpolate.single.IdentityInterpolator;
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
        .withName("slamin")
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
//    packModels.add(EffectModel.newBuilder()
//        .withPackName("slam pack")
//        .withName("slamout")
//        .addEffectStep(EffectStep.newBuilder()
//            .withScaleInterpolator(new SlamSoftOutInterpolator())
//            .withFilterInterpolator(new ZoomBlurFilterInterpolator(new LinearSplineInterpolator(
//                PointListBuilder.newPointListBuilder()
//                    .add(0, 0)
//                    .add(0.7f, 0)
//                    .add(0.9f, 1f)
//                    .add(0.9999f, 1f)
//                    .add(1f, 0)
//                    .build())))
//            .withEndPauseSeconds(1f)
//            .build())
//        .build());
//    packModels.add(EffectModel.newBuilder()
//        .withPackName("slam pack")
//        .withName("slamio")
//        .addEffectStep(EffectStep.newBuilder()
//            .withScaleInterpolator(new SlamHardInAndOutInterpolator())
//            .withFilterInterpolator(new ZoomBlurFilterInterpolator(new LinearSplineInterpolator(
//                PointListBuilder.newPointListBuilder()
//                    .add(0, 0)
//                    .add(0.35f, 0)
//                    .add(0.45f, 1f)
//                    .add(0.4999f, 1f)
//                    .add(0.5f, 0)
//                    .add(0.85f, 0)
//                    .add(0.95f, 1f)
//                    .add(0.9999f, 1f)
//                    .add(1f, 0)
//                    .build())))
//            .withDurationSeconds(1f)
//            .build())
//        .build());
//    packModels.add(EffectModel.newBuilder()
//        .withName("blurslam")
//        .addEffectStep(EffectStep.newBuilder()
//            .withScaleInterpolator(new SlamHardInterpolator())
//            .withFilterInterpolator(new GuassianUnblurFilterInterpolator())
//            .withEndPauseSeconds(1f)
//            .build())
//        .build());
//    packModels.add(EffectModel.newBuilder()
//        .withName("grayslam ")
//        .addEffectStep(EffectStep.newBuilder()
//            .withScaleInterpolator(new SlamHardInterpolator())
//            .withFilterInterpolator(new ZoomBlurFilterInterpolator(new LinearSplineInterpolator(
//                PointListBuilder.newPointListBuilder()
//                    .add(0, 0)
//                    .add(0.7f, 0)
//                    .add(0.9f, 1f)
//                    .add(0.9999f, 1f)
//                    .add(1f, 0)
//                    .build())))
//            .withFilterInterpolator(new UnsaturateFilterInterpolator())
//            .withEndPauseSeconds(1f)
//            .build())
//        .build());

    return packModels;
  }
}
