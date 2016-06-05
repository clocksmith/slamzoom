package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.interpolation.filter.single.GuassianUnblurFilterInterpolator;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashBounceBottomInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashBounceDiagonalInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.ShakeInterpolatorProvider;
import com.slamzoom.android.interpolators.base.LinearInterpolator;
import com.slamzoom.android.interpolators.effect.SlamHardInterpolator;
import com.slamzoom.android.interpolators.spline.CubicSplineInterpolator;
import com.slamzoom.android.interpolators.spline.PointListBuilder;

import java.util.List;

/**
 * Created by clocksmith on 6/4/16.
 */
public class BlurPackProvider {
  public static List<EffectTemplate> getPack() {
    List<EffectTemplate> packModels = Lists.newArrayList();

    packModels.add(EffectTemplate.newBuilder()
        .withName("blurslam")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new SlamHardInterpolator())
            .withFilterInterpolator(new GuassianUnblurFilterInterpolator())
            .withEndPauseSeconds(1f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("blurcrash")
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
        .withName("blurshake")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new LinearInterpolator())
            .withTranslateInterpolator(new ShakeInterpolatorProvider())
            .withFilterInterpolator(new GuassianUnblurFilterInterpolator())
            .withEndPauseSeconds(1f)
            .build())
        .build());

    return packModels;
  }
}
