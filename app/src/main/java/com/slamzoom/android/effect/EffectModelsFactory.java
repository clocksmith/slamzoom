package com.slamzoom.android.effect;

import com.google.common.collect.Lists;
import com.slamzoom.android.interpolate.combo.Crash2InterpolatorProvider;
import com.slamzoom.android.interpolate.combo.Crash3InterpolatorProvider;
import com.slamzoom.android.interpolate.combo.CrashInterpolatorProvider;

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
//              .withScaleInterpolatorProvider(new LinearInterpolator())
//              .build())
//          .build());
//      mModels.add(EffectModel.newBuilder()
//          .withName("soft")
//          .addEffectStep(EffectStep.newBuilder()
//              .withEndPauseSeconds(1)
//              .withScaleInterpolatorProvider(new SlamSoftInterpolator())
//              .build())
//          .build());
//      mModels.add(EffectModel.newBuilder()
//          .withName("slam")
//          .addEffectStep(EffectStep.newBuilder()
//              .withEndPauseSeconds(1)
//              .withScaleInterpolatorProvider(new SlamHardInterpolatorProvider())
//              .build())
//          .build());
//      mModels.add(EffectModel.newBuilder()
//          .withName("ghist")
//          .addEffectStep(EffectStep.newBuilder()
//              .withStartPauseSeconds(1)
//              .withEndPauseSeconds(1)
//              .withScaleInterpolatorProvider(new EaseInSlamHardInterpolator())
//              .withTranslateInterpolatorProvider(new ShakeInterpolator())
//              .build())
//          .build());
//      mModels.add(EffectModel.newBuilder()
//          .withName("superghist")
//          .addEffectStep(EffectStep.newBuilder()
//              .withStartPauseSeconds(1)
//              .withEndPauseSeconds(1)
//              .withScaleInterpolatorProvider(new EaseInSlamHardInterpolator())
//              .withTranslateInterpolatorProvider(new SuperShakeInterpolator())
//              .build())
//          .build());
//      mModels.add(EffectModel.newBuilder()
//          .withName("megaghist")
//          .addEffectStep(EffectStep.newBuilder()
//              .withScaleInterpolatorProvider(new EaseInSlamHardInterpolator())
//              .withTranslateInterpolatorProvider(new MegaShakeInterpolator())
//              .build())
//          .build());
//      mModels.add(EffectModel.newBuilder()
//          .withName("reverse")
//          .addEffectStep(EffectStep.newBuilder()
//              .withScaleInterpolatorProvider(new ReverseLinearInterpolator())
//              .build())
//          .build());
//      mModels.add(EffectModel.newBuilder()
//          .withName("quake")
//          .addEffectStep(EffectStep.newBuilder()
//              .withScaleInterpolatorProvider(new ReverseLinearInterpolator())
//              .withTranslateInterpolatorProvider(new ShakeInterpolator())
//              .build())
//          .build());
//      mModels.add(EffectModel.newBuilder()
//          .withName("superquake")
//          .addEffectStep(EffectStep.newBuilder()
//              .withScaleInterpolatorProvider(new ReverseLinearInterpolator())
//              .withTranslateInterpolatorProvider(new SuperShakeInterpolator())
//              .build())
//          .build());
//      mModels.add(EffectModel.newBuilder()
//          .withName("megaquake")
//          .addEffectStep(EffectStep.newBuilder()
//              .withScaleInterpolatorProvider(new ReverseLinearInterpolator())
//              .withTranslateInterpolatorProvider(new MegaShakeInterpolator())
//              .build())
//          .build());
      mModels.add(EffectModel.newBuilder()
          .withName("crash")
          .addEffectStep(EffectStep.newBuilder()
              .withDurationSeconds(1f)
              .withScaleAndTranslateInterpolatorProvider(new CrashInterpolatorProvider())
              .build())
          .build());
      mModels.add(EffectModel.newBuilder()
          .withName("crash2")
          .addEffectStep(EffectStep.newBuilder()
              .withDurationSeconds(1f)
              .withScaleAndTranslateInterpolatorProvider(new Crash2InterpolatorProvider())
              .build())
          .build());
      mModels.add(EffectModel.newBuilder()
          .withName("crash3")
          .addEffectStep(EffectStep.newBuilder()
              .withDurationSeconds(1f)
              .withScaleAndTranslateInterpolatorProvider(new Crash3InterpolatorProvider())
              .build())
          .build());
    }

    return mModels;
  }
}
