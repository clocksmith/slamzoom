package com.slamzoom.android.effect;

import com.google.common.collect.Lists;
import com.slamzoom.android.interpolate.multi.MegaShakeInterpolator;
import com.slamzoom.android.interpolate.multi.SuperShakeInterpolator;
import com.slamzoom.android.interpolate.single.EaseInSlamHardInterpolator;
import com.slamzoom.android.interpolate.single.EaseInSlamSoftInterpolator;
import com.slamzoom.android.interpolate.single.LinearInterpolator;
import com.slamzoom.android.interpolate.single.ReverseLinearInterpolator;
import com.slamzoom.android.interpolate.single.SlamHardInterpolator;
import com.slamzoom.android.interpolate.multi.ShakeInterpolator;
import com.slamzoom.android.interpolate.single.SlamSoftInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antrob on 2/21/16.
 */
public class EffectModelsFactory {
  public static List<EffectModel> getTemplates() {
    return Lists.newArrayList(
//        EffectModel.newBuilder()
//            .withName("simple")
//            .addEffectStep(EffectStep.newBuilder()
//                .withEndPauseSeconds(1)
//                .withScaleInterpolator(new LinearInterpolator())
//                .build()
//            )
//            .build(),
//        EffectModel.newBuilder()
//            .withName("soft")
//            .addEffectStep(EffectStep.newBuilder()
//                .withEndPauseSeconds(1)
//                .withScaleInterpolator(new SlamSoftInterpolator())
//                .build()
//            )
//            .build(),
//        EffectModel.newBuilder()
//            .withName("slam")
//            .addEffectStep(EffectStep.newBuilder()
//                .withEndPauseSeconds(1)
//                .withScaleInterpolator(new SlamHardInterpolator())
//                .build()
//            )
//            .build(),
//        EffectModel.newBuilder()
//            .withName("reverse")
//            .addEffectStep(EffectStep.newBuilder()
//                .withScaleInterpolator(new ReverseLinearInterpolator())
//                .build()
//            )
//            .build(),
//        EffectModel.newBuilder()
//            .withName("ghist")
//            .addEffectStep(EffectStep.newBuilder()
//                .withStartPauseSeconds(1)
//                .withEndPauseSeconds(1)
//                .withScaleInterpolator(new EaseInSlamHardInterpolator())
//                .withTranslateInterpolator(new ShakeInterpolator())
//                .build()
//            )
//            .build(),
        EffectModel.newBuilder()
            .withName("quake")
            .addEffectStep(EffectStep.newBuilder()
                .withScaleInterpolator(new ReverseLinearInterpolator())
                .withTranslateInterpolator(new ShakeInterpolator())
                .build()
            )
            .build(),
        EffectModel.newBuilder()
            .withName("superquake")
            .addEffectStep(EffectStep.newBuilder()
                .withScaleInterpolator(new ReverseLinearInterpolator())
                .withTranslateInterpolator(new SuperShakeInterpolator())
                .build()
            )
            .build(),
        EffectModel.newBuilder()
            .withName("megaquake")
            .addEffectStep(EffectStep.newBuilder()
                .withScaleInterpolator(new ReverseLinearInterpolator())
                .withTranslateInterpolator(new MegaShakeInterpolator())
                .build()
            )
            .build()
    );
  }
}
