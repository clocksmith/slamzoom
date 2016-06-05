package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.interpolators.base.LinearInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 3/22/16.
 */
public class TilePackProvider {
  public static List<EffectTemplate> getPack() {
    List<EffectTemplate> packModels = Lists.newArrayList();
    packModels.add(EffectTemplate.newBuilder()
        .withName("tile")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new LinearInterpolator())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("tile4")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new LinearInterpolator())
            .build())
        .withNumTilesInRow(2)
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("tile9")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new LinearInterpolator())
            .build())
        .withNumTilesInRow(3)
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("tile16")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new LinearInterpolator())
            .build())
        .withNumTilesInRow(4)
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("tile25")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new LinearInterpolator())
            .build())
        .withNumTilesInRow(5)
        .build());

    return packModels;
  }
}


