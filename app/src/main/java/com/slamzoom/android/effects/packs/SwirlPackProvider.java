package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.effects.interpolation.filter.group.UnswirlEyesFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlAtHotspotOnHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlTurntableAtHotspotOnHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.transform.translate.ShakeInterpolatorProvider;
import com.slamzoom.android.interpolators.base.LinearInterpolator;
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
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolator(new UnswirlFilterInterpolator())
            .withEndPauseSeconds(0.5f)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlface")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolator(new UnswirlAtHotspotOnHotspotFilterInterpolator())
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
        .withName("swirlyface")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new InAndOutInterpolator())
            .withFilterInterpolator(new UnswirlAtHotspotOnHotspotFilterInterpolator())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlslam")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new SlamSoftInterpolator())
            .withTranslateInterpolator(new ShakeInterpolatorProvider())
            .withFilterInterpolator(new UnswirlAtHotspotFilterInterpolator())
            .withEndPauseSeconds(0.5f)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlaway")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolator(new UnswirlAtHotspotFilterInterpolator())
            .withDurationSeconds(3f)
            .withEndPauseSeconds(0.5f)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("swirleyes")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new UnswirlEyesFilterInterpolatorGroup())
            .withEndPauseSeconds(1)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("swirldj")
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
