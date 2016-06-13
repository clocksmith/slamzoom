package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.interpolators.ConstantInterpolator;
import com.slamzoom.android.interpolators.LinearInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 4/8/16.
 */
public class DebugPackProvider {
  public static List<EffectTemplate> getPack() {
    List<EffectTemplate> packModels = Lists.newArrayList();

//    packModels.add(EffectTemplate.newBuilder()
//        .withPackName("debug pack")
//        .withName("debug-translate")
//        .addEffectStep(EffectStep.newBuilder()
//            .withStartPauseSeconds(1)
//            .withEndPauseSeconds(1)
//            .withDurationSeconds(3)
//            .withScaleInterpolator(new LinearInterpolator())
//            .withXInterpolator(new ConstantInterpolator(1))
//            .build())
//        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withPackName("debug pack")
        .withName("debug-quality")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(0.5f)
            .withScaleInterpolator(new LinearInterpolator())
            .build())
        .build());

    return packModels;
  }
}
