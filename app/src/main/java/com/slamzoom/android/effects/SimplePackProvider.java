package com.slamzoom.android.effects;

import com.google.common.collect.Lists;
import com.slamzoom.android.interpolate.single.IdentityInterpolator;
import com.slamzoom.android.ui.main.effect.EffectModel;
import com.slamzoom.android.ui.main.effect.EffectStep;

import java.util.List;

/**
 * Created by clocksmith on 3/22/16.
 */
public class SimplePackProvider {
  public static List<EffectModel> getPack() {
    List<EffectModel> packModels = Lists.newArrayList();
    packModels.add(EffectModel.newBuilder()
        .withName("simple")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new IdentityInterpolator())
            .build())
        .build());
    packModels.add(EffectModel.newBuilder()
        .withName("simple x4")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new IdentityInterpolator())
            .build())
        .withNumTilesInRow(2)
        .build());
    packModels.add(EffectModel.newBuilder()
        .withName("simple x9")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new IdentityInterpolator())
            .build())
        .withNumTilesInRow(3)
        .build());
    packModels.add(EffectModel.newBuilder()
        .withName("simple x9")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new IdentityInterpolator())
            .build())
        .withNumTilesInRow(4)
        .build());
    packModels.add(EffectModel.newBuilder()
        .withName("simple x25")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new IdentityInterpolator())
            .build())
        .withNumTilesInRow(5)
        .build());

    return packModels;
  }
}


