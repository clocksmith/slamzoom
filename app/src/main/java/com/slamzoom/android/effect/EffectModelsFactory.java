package com.slamzoom.android.effect;

import com.google.common.collect.Lists;
import com.slamzoom.android.interpolate.combo.BackAndForthInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.CrashBounceDIagonalInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.CrashBounceBottomInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.CrashInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.FlushInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.SpiralInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.WizzyWazzleInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.ZigZagInterpolatorProvider;
import com.slamzoom.android.interpolate.scale.EaseInSlamHardInterpolatorProvider;
import com.slamzoom.android.interpolate.scale.ReverseSmoothInterpolatorProvider;
import com.slamzoom.android.interpolate.scale.SlamHardInterpolatorProvider;
import com.slamzoom.android.interpolate.scale.SlamSoftInterpolatorProvider;
import com.slamzoom.android.interpolate.scale.SmoothInterpolatorProvider;
import com.slamzoom.android.interpolate.translate.MegaShakeInterpolatorProvider;
import com.slamzoom.android.interpolate.translate.ShakeInterpolatorProvider;
import com.slamzoom.android.interpolate.translate.SuperShakeInterpolatorProvider;

import java.util.List;

/**
 * Created by antrob on 2/21/16.
 */
public class EffectModelsFactory {
  private static List<EffectModel> mModels;

  public static List<EffectModel> getTemplates() {
    if (mModels == null) {
      mModels = Lists.newArrayList();
      mModels.add(EffectModel.newBuilder()
          .withName("simple")
          .addEffectStep(EffectStep.newBuilder()
              .withEndPauseSeconds(1)
              .withScaleInterpolatorProvider(new SmoothInterpolatorProvider())
              .build())
          .build());
      mModels.add(EffectModel.newBuilder()
          .withName("soft")
          .addEffectStep(EffectStep.newBuilder()
              .withEndPauseSeconds(1)
              .withScaleInterpolatorProvider(new SlamSoftInterpolatorProvider())
              .build())
          .build());
      mModels.add(EffectModel.newBuilder()
          .withName("slam")
          .addEffectStep(EffectStep.newBuilder()
              .withEndPauseSeconds(1)
              .withScaleInterpolatorProvider(new SlamHardInterpolatorProvider())
              .build())
          .build());
      mModels.add(EffectModel.newBuilder()
          .withName("ghist")
          .addEffectStep(EffectStep.newBuilder()
              .withStartPauseSeconds(1)
              .withEndPauseSeconds(1)
              .withScaleInterpolatorProvider(new EaseInSlamHardInterpolatorProvider())
              .withTranslateInterpolatorProvider(new ShakeInterpolatorProvider())
              .build())
          .build());
      mModels.add(EffectModel.newBuilder()
          .withName("superghist")
          .addEffectStep(EffectStep.newBuilder()
              .withStartPauseSeconds(1)
              .withEndPauseSeconds(1)
              .withScaleInterpolatorProvider(new EaseInSlamHardInterpolatorProvider())
              .withTranslateInterpolatorProvider(new SuperShakeInterpolatorProvider())
              .build())
          .build());
      mModels.add(EffectModel.newBuilder()
          .withName("megaghist")
          .addEffectStep(EffectStep.newBuilder()
              .withScaleInterpolatorProvider(new EaseInSlamHardInterpolatorProvider())
              .withTranslateInterpolatorProvider(new MegaShakeInterpolatorProvider())
              .build())
          .build());
      mModels.add(EffectModel.newBuilder()
          .withName("reverse")
          .addEffectStep(EffectStep.newBuilder()
              .withScaleInterpolatorProvider(new ReverseSmoothInterpolatorProvider())
              .build())
          .build());
      mModels.add(EffectModel.newBuilder()
          .withName("quake")
          .addEffectStep(EffectStep.newBuilder()
              .withScaleInterpolatorProvider(new ReverseSmoothInterpolatorProvider())
              .withTranslateInterpolatorProvider(new ShakeInterpolatorProvider())
              .build())
          .build());
      mModels.add(EffectModel.newBuilder()
          .withName("superquake")
          .addEffectStep(EffectStep.newBuilder()
              .withScaleInterpolatorProvider(new ReverseSmoothInterpolatorProvider())
              .withTranslateInterpolatorProvider(new SuperShakeInterpolatorProvider())
              .build())
          .build());
      mModels.add(EffectModel.newBuilder()
          .withName("megaquake")
          .addEffectStep(EffectStep.newBuilder()
              .withScaleInterpolatorProvider(new ReverseSmoothInterpolatorProvider())
              .withTranslateInterpolatorProvider(new MegaShakeInterpolatorProvider())
              .build())
          .build());
      mModels.add(EffectModel.newBuilder()
          .withName("flush")
          .addEffectStep(EffectStep.newBuilder()
              .withDurationSeconds(3f)
              .withEndPauseSeconds(1f)
              .withScaleAndTranslateInterpolatorProvider(new FlushInterpolatorProvider())
              .build())
          .build());
      mModels.add(EffectModel.newBuilder()
          .withName("spiral")
          .addEffectStep(EffectStep.newBuilder()
              .withDurationSeconds(3f)
              .withEndPauseSeconds(1f)
              .withScaleAndTranslateInterpolatorProvider(new SpiralInterpolatorProvider())
              .build())
          .build());
      mModels.add(EffectModel.newBuilder()
          .withName("zigzag")
          .addEffectStep(EffectStep.newBuilder()
              .withDurationSeconds(3f)
              .withEndPauseSeconds(1f)
              .withScaleAndTranslateInterpolatorProvider(new ZigZagInterpolatorProvider())
              .build())
          .build());
      mModels.add(EffectModel.newBuilder()
          .withName("methodman")
          .addEffectStep(EffectStep.newBuilder()
              .withDurationSeconds(1f)
              .withScaleAndTranslateInterpolatorProvider(new BackAndForthInterpolatorProvider())
              .build())
          .build());
      mModels.add(EffectModel.newBuilder()
          .withName("wizzywazzle")
          .addEffectStep(EffectStep.newBuilder()
              .withDurationSeconds(2f)
              .withScaleAndTranslateInterpolatorProvider(new WizzyWazzleInterpolatorProvider())
              .build())
          .build());
      mModels.add(EffectModel.newBuilder()
          .withName("crash")
          .addEffectStep(EffectStep.newBuilder()
              .withStartPauseSeconds(0.3f)
              .withDurationSeconds(0.7f)
              .withScaleAndTranslateInterpolatorProvider(new CrashInterpolatorProvider())
              .build())
          .build());
      mModels.add(EffectModel.newBuilder()
          .withName("crashbounce")
          .addEffectStep(EffectStep.newBuilder()
              .withStartPauseSeconds(0.5f)
              .withDurationSeconds(1f)
              .withScaleAndTranslateInterpolatorProvider(new CrashBounceDIagonalInterpolatorProvider())
              .build())
          .build());
      mModels.add(EffectModel.newBuilder()
          .withName("crashbounce2")
          .addEffectStep(EffectStep.newBuilder()
              .withStartPauseSeconds(0.5f)
              .withDurationSeconds(1f)
              .withScaleAndTranslateInterpolatorProvider(new CrashBounceBottomInterpolatorProvider())
              .build())
          .build());
    }

    return mModels;
  }
}
