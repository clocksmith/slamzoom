package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.effects.interpolation.filter.group.UnswirlEyesFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlAtHotspotOnHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlTurntableAtHotspotOnHotspotFilterInterpolator;
import com.slamzoom.android.interpolators.LinearInterpolator;
import com.slamzoom.android.interpolators.ReverseLinearInterpolator;
import com.slamzoom.android.interpolators.custom.InAndOutInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 3/22/16.
 */
public class SwirlPackProvider {
  public static List<EffectTemplate> getPack() {
    List<EffectTemplate> packModels = Lists.newArrayList();

    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlin")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolator(new UnswirlFilterInterpolator())
            .withEndPauseSeconds(0.5f)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlout")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new ReverseLinearInterpolator())
            .withFilterInterpolator(new UnswirlFilterInterpolator())
            .withEndPauseSeconds(0.5f)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlinout")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new InAndOutInterpolator())
            .withFilterInterpolator(new UnswirlFilterInterpolator())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlspot")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolator(new UnswirlAtHotspotOnHotspotFilterInterpolator())
            .withDurationSeconds(3f)
            .withEndPauseSeconds(0.5f)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("djswirls")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolator(new UnswirlTurntableAtHotspotOnHotspotFilterInterpolator())
            .withEndPauseSeconds(1)
            .build())
        .build());

    return packModels;
  }
}
