package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.interpolation.filter.single.BulgeFilterInterpolator;
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
            .withFilterInterpolator(new BulgeFilterInterpolator())
            .withEndPauseSeconds(1)
            .build())
        .build());
    return packModels;
  }
}
