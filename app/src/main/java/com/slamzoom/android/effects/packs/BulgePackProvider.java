package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeDoubleLeftRightFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeEyesFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeFaceFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeLeftRightSwapFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.single.BulgeInAtHotspotFilterInterpolator;
import com.slamzoom.android.interpolators.base.LinearInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 4/1/16.
 */
public class BulgePackProvider {
  public static List<EffectTemplate> getPack() {
    List<EffectTemplate> packModels = Lists.newArrayList();

    packModels.add(EffectTemplate.newBuilder()
        .withName("bulgein")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolator(new BulgeInAtHotspotFilterInterpolator())
            .withEndPauseSeconds(1)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("bulgeeyes")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new BulgeEyesFilterInterpolatorGroup())
            .withEndPauseSeconds(1)
            .build())
        .build());


    packModels.add(EffectTemplate.newBuilder()
        .withName("bulgeface")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new BulgeFaceFilterInterpolatorGroup())
            .withEndPauseSeconds(1)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("bulgeleftright")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new BulgeDoubleLeftRightFilterInterpolatorGroup())
            .withEndPauseSeconds(1)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("bulgeswap")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new BulgeLeftRightSwapFilterInterpolatorGroup())
            .withEndPauseSeconds(1)
            .build())
        .build());

    return packModels;
  }
}
