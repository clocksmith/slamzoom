package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeEyesFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.SwirlEyesFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlOnHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlTurntableOnHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.FlushInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.SpiralInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.ShakeInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlFilterInterpolator;
import com.slamzoom.android.interpolators.effect.IdentityInterpolator;
import com.slamzoom.android.interpolators.effect.InAndOutInterpolator;
import com.slamzoom.android.interpolators.effect.SlamSoftInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 3/22/16.
 */
public class SwirlPackProvider {
  public static List<EffectTemplate> getPack() {
    List<EffectTemplate> packModels = Lists.newArrayList();

    packModels.add(EffectTemplate.newBuilder()
        .withName("swirl")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new IdentityInterpolator())
            .withFilterInterpolator(new UnswirlFilterInterpolator())
            .withEndPauseSeconds(0.5f)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("swirl2")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new IdentityInterpolator())
            .withFilterInterpolator(new UnswirlOnHotspotFilterInterpolator())
            .withDurationSeconds(3f)
            .withEndPauseSeconds(0.5f)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("swirly")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new InAndOutInterpolator())
            .withFilterInterpolator(new UnswirlFilterInterpolator())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlslam")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new SlamSoftInterpolator())
            .withTranslateInterpolator(new ShakeInterpolatorProvider())
            .withFilterInterpolator(new UnswirlFilterInterpolator())
            .withEndPauseSeconds(0.5f)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("swirleyes")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new IdentityInterpolator())
            .withFilterInterpolatorGroup(new SwirlEyesFilterInterpolatorGroup())
            .withEndPauseSeconds(1)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("swirltable")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new IdentityInterpolator())
            .withFilterInterpolator(new UnswirlTurntableOnHotspotFilterInterpolator())
            .withEndPauseSeconds(1)
            .build())
        .build());

    return packModels;
  }
}
