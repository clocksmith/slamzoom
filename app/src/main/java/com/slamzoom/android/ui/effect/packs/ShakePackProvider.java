package com.slamzoom.android.ui.effect.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.interpolate.combo.scaletranslate.FlushInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.scaletranslate.SpiralInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.translate.MegaShakeInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.translate.ShakeInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.translate.SuperShakeInterpolatorProvider;
import com.slamzoom.android.interpolate.filter.GuassianUnblurFilterInterpolator;
import com.slamzoom.android.interpolate.filter.UnswirlFilterInterpolator;
import com.slamzoom.android.interpolate.single.HalfInAndOutInterpolator;
import com.slamzoom.android.interpolate.single.IdentityInterpolator;
import com.slamzoom.android.interpolate.single.InAndOutInterpolator;
import com.slamzoom.android.interpolate.single.SlamHardInterpolator;
import com.slamzoom.android.ui.effect.EffectModel;
import com.slamzoom.android.ui.effect.EffectStep;

import java.util.List;

/**
 * Created by clocksmith on 3/22/16.
 */
public class ShakePackProvider {
  public static List<EffectModel> getPack() {
    List<EffectModel> packModels = Lists.newArrayList();

    packModels.add(EffectModel.newBuilder()
        .withName("quake")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new HalfInAndOutInterpolator())
            .withTranslateInterpolator(new ShakeInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectModel.newBuilder()
        .withName("superquake")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new HalfInAndOutInterpolator())
            .withTranslateInterpolator(new SuperShakeInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectModel.newBuilder()
        .withName("megaquake")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new HalfInAndOutInterpolator())
            .withTranslateInterpolator(new MegaShakeInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectModel.newBuilder()
        .withName("shakeslam")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new SlamHardInterpolator())
            .withTranslateInterpolator(new ShakeInterpolatorProvider())
            .withEndPauseSeconds(1f)
            .build())
        .build());
    packModels.add(EffectModel.newBuilder()
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
