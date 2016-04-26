package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.interpolators.effect.IdentityInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 3/22/16.
 */
public class SimplePackProvider {
  public static List<EffectTemplate> getPack() {
    List<EffectTemplate> packModels = Lists.newArrayList();
    packModels.add(EffectTemplate.newBuilder()
        .withName("simple")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new IdentityInterpolator())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("simple4x")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new IdentityInterpolator())
            .build())
        .withNumTilesInRow(2)
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("simple9x")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new IdentityInterpolator())
            .build())
        .withNumTilesInRow(3)
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("simple16x")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new IdentityInterpolator())
            .build())
        .withNumTilesInRow(4)
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("simple25x")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new IdentityInterpolator())
            .build())
        .withNumTilesInRow(5)
        .build());

    return packModels;
  }
}


