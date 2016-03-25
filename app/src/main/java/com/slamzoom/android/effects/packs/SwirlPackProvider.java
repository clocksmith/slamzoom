package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.interpolaters.combo.scaletranslate.FlushInterpolatorProvider;
import com.slamzoom.android.interpolaters.combo.scaletranslate.SpiralInterpolatorProvider;
import com.slamzoom.android.interpolaters.combo.translate.ShakeInterpolatorProvider;
import com.slamzoom.android.interpolaters.filter.UnswirlFilterInterpolator;
import com.slamzoom.android.interpolaters.single.IdentityInterpolator;
import com.slamzoom.android.interpolaters.single.InAndOutInterpolator;
import com.slamzoom.android.interpolaters.single.SlamSoftInterpolator;

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
        .withName("flush")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleAndTranslateInterpolatorProvider(new FlushInterpolatorProvider())
            .withDurationSeconds(3f)
            .withEndPauseSeconds(1f)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("spiral")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleAndTranslateInterpolatorProvider(new SpiralInterpolatorProvider())
            .withDurationSeconds(3f)
            .withEndPauseSeconds(1f)
            .build())
        .build());

    return packModels;
  }
}
