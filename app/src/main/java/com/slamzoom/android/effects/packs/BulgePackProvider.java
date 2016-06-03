package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeEyesFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeFaceFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.MultiBulgeSwimFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.TinyBulgesSwimFilterInterpoaltor;
import com.slamzoom.android.effects.interpolation.filter.single.NormalizedBulgeFilterInterpolator;
import com.slamzoom.android.interpolators.effect.IdentityInterpolator;

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
            .withScaleInterpolator(new IdentityInterpolator())
            .withFilterInterpolator(new NormalizedBulgeFilterInterpolator())
            .withEndPauseSeconds(1)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("leftrightbulge")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new IdentityInterpolator())
            .withFilterInterpolatorGroup(new BulgeEyesFilterInterpolatorGroup())
            .withEndPauseSeconds(1)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("facebulge")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new IdentityInterpolator())
            .withFilterInterpolatorGroup(new BulgeFaceFilterInterpolatorGroup())
            .withEndPauseSeconds(1)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("bulgeswap")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new IdentityInterpolator())
            .withFilterInterpolatorGroup(new MultiBulgeSwimFilterInterpolatorGroup())
            .withEndPauseSeconds(1)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("tinybulges")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new IdentityInterpolator())
            .withFilterInterpolatorGroup(new TinyBulgesSwimFilterInterpoaltor())
            .withEndPauseSeconds(1)
            .build())
        .build());

    return packModels;
  }
}
