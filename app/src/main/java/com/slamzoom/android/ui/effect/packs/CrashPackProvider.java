package com.slamzoom.android.ui.effect.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.interpolate.combo.scaletranslate.CrashBounceBottomInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.scaletranslate.CrashBounceDiagonalInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.scaletranslate.CrashInterpolatorProvider;
import com.slamzoom.android.ui.effect.EffectModel;
import com.slamzoom.android.ui.effect.EffectStep;

import java.util.List;

/**
 * Created by clocksmith on 3/22/16.
 */
public class CrashPackProvider {
  public static List<EffectModel> getPack() {
    List<EffectModel> packModels = Lists.newArrayList();
    packModels.add(EffectModel.newBuilder()
        .withName("crash")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.3f)
            .withDurationSeconds(0.7f)
            .withScaleAndTranslateInterpolatorProvider(new CrashInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectModel.newBuilder()
        .withName("crashbounce")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(1f)
            .withScaleAndTranslateInterpolatorProvider(new CrashBounceDiagonalInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectModel.newBuilder()
        .withName("crashbounce2")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(1f)
            .withScaleAndTranslateInterpolatorProvider(new CrashBounceBottomInterpolatorProvider())
            .build())
        .build());

    return packModels;
  }
}
