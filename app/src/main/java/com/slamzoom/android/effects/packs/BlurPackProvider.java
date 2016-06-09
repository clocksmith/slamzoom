package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.interpolation.filter.single.GaussianBlurFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.GaussianUnblurFilterInterpolator;
import com.slamzoom.android.effects.interpolation.transform.translate.ShakeInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.LinearInterpolator;
import com.slamzoom.android.interpolators.custom.SlamHardInterpolator;
import com.slamzoom.android.interpolators.spline.CubicSplineInterpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;
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
            .withFilterInterpolator(new GaussianUnblurFilterInterpolator())
            .withEndPauseSeconds(1f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("blurcrash")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(0.8f)
            .withScaleInterpolator(new CubicSplineInterpolator(PointListBuilder.create()
                .add(0, 0)
                .add(0.3f, 1f)
                .add(1f, 1f)
                .build()))
            .withFilterInterpolator(new GaussianUnblurFilterInterpolator(
                new CubicSplineInterpolator(PointListBuilder.create()
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
            .withFilterInterpolator(new GaussianUnblurFilterInterpolator())
            .withEndPauseSeconds(1f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("blurtease")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new LinearSplineInterpolator(PointListBuilder.create()
                .add(0, 0)
                .add(0.4f, 0)
                .add(0.6f, 1)
                .add(1, 1)
                .build()))
            .withFilterInterpolator(new GaussianBlurFilterInterpolator(new LinearSplineInterpolator(
                PointListBuilder.create()
                    .add(0, 0)
                    .add(0.4f, 1)
                    .add(0.6f, 1)
                    .add(1, 0)
                    .build())))
            .withEndPauseSeconds(1f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("blurtease2")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(1)
            .withDurationSeconds(4f)
            .withScaleInterpolator(new Interpolator() {
              @Override
              protected float getRangePercent(float t) {
                if (t < 0.1667) {
                  return 0;
                } else if (t < 0.5) {
                  return 0.15f;
                } else if (t < 0.8333) {
                  return 0.50f;
                } else {
                  return 1;
                }
              }
            })
            .withFilterInterpolator(new GaussianBlurFilterInterpolator(
                new Interpolator() {
                  @Override
                  protected float getRangePercent(float t) {
                    return (float) Math.pow(Math.sin(3 * Math.PI * t), 6);
                  }
                }))
            .withEndPauseSeconds(1)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("blurmagic")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new LinearSplineInterpolator(PointListBuilder.create()
                .add(0, 0)
                .add(0.4999f, 0)
                .add(0.5f, 1)
                .add(1, 1)
                .build()))
//            .withFilterInterpolator(new GuassianBlurFilterInterpolator(new LinearSplineInterpolator(
//                PointListBuilder.create()
//                    .add(0, 0)
//                    .add(0.5f, 1)
//                    .add(1, 0)
//                    .build())) {
//              @Override
//              public float getBlurSize() {
//                mBlurCalculator.setBaseValue(10);
//                return mBlurCalculator.getValueFromInterpolation();
//              }
//            })
            .withEndPauseSeconds(1f)
            .build())
        .build());

    return packModels;
  }
}
