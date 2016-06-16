package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeDoubleLeftRightFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeEyesFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeLeftRightSwapFilterInterpolationGroup;
import com.slamzoom.android.effects.interpolation.filter.group.DeflateFaceFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.SumoBulge2FilterInterpolator;
import com.slamzoom.android.interpolators.LinearInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 6/5/16.
 */
public class DistortionPack2Provider {
  public static List<EffectTemplate> getPack() {
    List<EffectTemplate> packModels = Lists.newArrayList();

    packModels.add(EffectTemplate.newBuilder()
        .withName("doublebulge")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withEndPauseSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new BulgeDoubleLeftRightFilterInterpolatorGroup())
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("magoo")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withEndPauseSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new BulgeEyesFilterInterpolatorGroup())
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("deflate")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new DeflateFaceFilterInterpolatorGroup())
            .withEndPauseSeconds(2)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("bulgeswap")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withEndPauseSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new BulgeLeftRightSwapFilterInterpolationGroup())
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("weirdo")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new SumoBulge2FilterInterpolator())
            .withEndPauseSeconds(2)
            .build())
        .build());

    return packModels;
  }
}
