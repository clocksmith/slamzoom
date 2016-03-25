package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.interpolaters.combo.translate.MegaShakeInterpolatorProvider;
import com.slamzoom.android.interpolaters.combo.translate.ShakeInterpolatorProvider;
import com.slamzoom.android.interpolaters.combo.translate.SuperShakeInterpolatorProvider;
import com.slamzoom.android.interpolaters.filter.GuassianUnblurFilterInterpolator;
import com.slamzoom.android.interpolaters.single.HalfInAndOutInterpolator;
import com.slamzoom.android.interpolaters.single.IdentityInterpolator;
import com.slamzoom.android.interpolaters.single.SlamHardInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 3/22/16.
 */
public class ShakePackProvider {
  public static List<EffectTemplate> getPack() {
    List<EffectTemplate> packModels = Lists.newArrayList();

    packModels.add(EffectTemplate.newBuilder()
        .withName("quake")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new HalfInAndOutInterpolator())
            .withTranslateInterpolator(new ShakeInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("superquake")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new HalfInAndOutInterpolator())
            .withTranslateInterpolator(new SuperShakeInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("megaquake")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new HalfInAndOutInterpolator())
            .withTranslateInterpolator(new MegaShakeInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("shakeslam")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new SlamHardInterpolator())
            .withTranslateInterpolator(new ShakeInterpolatorProvider())
            .withEndPauseSeconds(1f)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("shakeunblur")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new IdentityInterpolator())
            .withTranslateInterpolator(new ShakeInterpolatorProvider())
            .withFilterInterpolator(new GuassianUnblurFilterInterpolator())
            .withEndPauseSeconds(1f)
            .build())
        .build());

    return packModels;
  }
}
