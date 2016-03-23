package com.slamzoom.android.ui.effect.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.interpolate.combo.scaletranslate.FlushInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.scaletranslate.SpiralInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.translate.ShakeInterpolatorProvider;
import com.slamzoom.android.interpolate.filter.UnswirlFilterInterpolator;
import com.slamzoom.android.interpolate.single.IdentityInterpolator;
import com.slamzoom.android.interpolate.single.InAndOutInterpolator;
import com.slamzoom.android.interpolate.single.SlamSoftInterpolator;
import com.slamzoom.android.ui.effect.EffectModel;
import com.slamzoom.android.ui.effect.EffectStep;

import java.util.List;

/**
 * Created by clocksmith on 3/22/16.
 */
public class SwirlPackProvider {
  public static List<EffectModel> getPack() {
    List<EffectModel> packModels = Lists.newArrayList();

    packModels.add(EffectModel.newBuilder()
        .withName("swirl")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new IdentityInterpolator())
            .withFilterInterpolator(new UnswirlFilterInterpolator())
            .withEndPauseSeconds(0.5f)
            .build())
        .build());
    packModels.add(EffectModel.newBuilder()
        .withName("swirly")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new InAndOutInterpolator())
            .withFilterInterpolator(new UnswirlFilterInterpolator())
            .withEndPauseSeconds(0.5f)
            .build())
        .build());
    packModels.add(EffectModel.newBuilder()
        .withName("swirlslam")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new SlamSoftInterpolator())
            .withTranslateInterpolator(new ShakeInterpolatorProvider())
            .withFilterInterpolator(new UnswirlFilterInterpolator())
            .withEndPauseSeconds(0.5f)
            .build())
        .build());
    packModels.add(EffectModel.newBuilder()
        .withName("flush")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(3f)
            .withEndPauseSeconds(1f)
            .withScaleAndTranslateInterpolatorProvider(new FlushInterpolatorProvider())
            .withEndPauseSeconds(1f)
            .build())
        .build());
    packModels.add(EffectModel.newBuilder()
        .withName("spiral")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(3f)
            .withEndPauseSeconds(1f)
            .withScaleAndTranslateInterpolatorProvider(new SpiralInterpolatorProvider())
            .withEndPauseSeconds(1f)
            .build())
        .build());

    return packModels;
  }
}
