package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeDoubleLeftRightFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeEyesFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeFaceFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.InflateFaceFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.SumoBulgeFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.BulgeInAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.ShrinkInAtHotspotFilterInterpolator;
import com.slamzoom.android.interpolators.LinearInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 4/1/16.
 */
public class DistortionPack1Provider {
  public static List<EffectTemplate> getPack() {
    List<EffectTemplate> packModels = Lists.newArrayList();

    packModels.add(EffectTemplate.newBuilder()
        .withName("bulger")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withEndPauseSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolator(new BulgeInAtHotspotFilterInterpolator())
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("shrinkydink")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolator(new ShrinkInAtHotspotFilterInterpolator())
            .withEndPauseSeconds(2)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("smush")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withEndPauseSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new BulgeFaceFilterInterpolatorGroup())
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("inflate")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new InflateFaceFilterInterpolatorGroup())
            .withEndPauseSeconds(2)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("sumo")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new SumoBulgeFilterInterpolator())
            .withEndPauseSeconds(2)
            .build())
        .build());

    return packModels;
  }
}
