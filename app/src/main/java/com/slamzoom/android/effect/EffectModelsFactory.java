package com.slamzoom.android.effect;

import com.google.common.collect.Lists;
import com.slamzoom.android.interpolate.scale.SlamHardInterpolator;

import java.util.List;

/**
 * Created by antrob on 2/21/16.
 */
public class EffectModelsFactory {
  private static List<EffectModel> mModels;

  public static List<EffectModel> getTemplates() {
    if (mModels == null) {
      mModels = Lists.newArrayList();
//      mModels.add(EffectModel.newBuilder()
//          .withName("simple")
//          .addEffectStep(EffectStep.newBuilder()
//              .withEndPauseSeconds(1)
//              .withScaleInterpolator(new LinearInterpolator())
//              .build())
//          .build());
//      mModels.add(EffectModel.newBuilder()
//          .withName("soft")
//          .addEffectStep(EffectStep.newBuilder()
//              .withEndPauseSeconds(1)
//              .withScaleInterpolator(new SlamSoftInterpolator())
//              .build())
//          .build());
      mModels.add(EffectModel.newBuilder()
          .withName("slam")
          .addEffectStep(EffectStep.newBuilder()
              .withEndPauseSeconds(1)
              .withScaleInterpolator(new SlamHardInterpolator())
              .build())
          .build());
//      mModels.add(EffectModel.newBuilder()
//          .withName("ghist")
//          .addEffectStep(EffectStep.newBuilder()
//              .withStartPauseSeconds(1)
//              .withEndPauseSeconds(1)
//              .withScaleInterpolator(new EaseInSlamHardInterpolator())
//              .withTranslateInterpolator(new ShakeInterpolator())
//              .build())
//          .build());
//      mModels.add(EffectModel.newBuilder()
//          .withName("superghist")
//          .addEffectStep(EffectStep.newBuilder()
//              .withStartPauseSeconds(1)
//              .withEndPauseSeconds(1)
//              .withScaleInterpolator(new EaseInSlamHardInterpolator())
//              .withTranslateInterpolator(new SuperShakeInterpolator())
//              .build())
//          .build());
//      mModels.add(EffectModel.newBuilder()
//          .withName("megaghist")
//          .addEffectStep(EffectStep.newBuilder()
//              .withScaleInterpolator(new EaseInSlamHardInterpolator())
//              .withTranslateInterpolator(new MegaShakeInterpolator())
//              .build())
//          .build());
//      mModels.add(EffectModel.newBuilder()
//          .withName("reverse")
//          .addEffectStep(EffectStep.newBuilder()
//              .withScaleInterpolator(new ReverseLinearInterpolator())
//              .build())
//          .build());
//      mModels.add(EffectModel.newBuilder()
//          .withName("quake")
//          .addEffectStep(EffectStep.newBuilder()
//              .withScaleInterpolator(new ReverseLinearInterpolator())
//              .withTranslateInterpolator(new ShakeInterpolator())
//              .build())
//          .build());
//      mModels.add(EffectModel.newBuilder()
//          .withName("superquake")
//          .addEffectStep(EffectStep.newBuilder()
//              .withScaleInterpolator(new ReverseLinearInterpolator())
//              .withTranslateInterpolator(new SuperShakeInterpolator())
//              .build())
//          .build());
//      mModels.add(EffectModel.newBuilder()
//          .withName("megaquake")
//          .addEffectStep(EffectStep.newBuilder()
//              .withScaleInterpolator(new ReverseLinearInterpolator())
//              .withTranslateInterpolator(new MegaShakeInterpolator())
//              .build())
//          .build());
    }

    return mModels;
  }
}
