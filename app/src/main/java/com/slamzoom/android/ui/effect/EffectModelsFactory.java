package com.slamzoom.android.ui.effect;

import com.google.common.collect.Lists;
import com.slamzoom.android.interpolate.providers.combo.BackAndForthInterpolatorProvider;
import com.slamzoom.android.interpolate.providers.combo.CrashBounceDIagonalInterpolatorProvider;
import com.slamzoom.android.interpolate.providers.combo.CrashBounceBottomInterpolatorProvider;
import com.slamzoom.android.interpolate.providers.combo.CrashInterpolatorProvider;
import com.slamzoom.android.interpolate.providers.combo.FlushInterpolatorProvider;
import com.slamzoom.android.interpolate.providers.combo.SpiralInterpolatorProvider;
import com.slamzoom.android.interpolate.providers.combo.WizzyWazzleInterpolatorProvider;
import com.slamzoom.android.interpolate.providers.combo.ZigZagInterpolatorProvider;
import com.slamzoom.android.interpolate.providers.scale.EaseInSlamHardInterpolatorProvider;
import com.slamzoom.android.interpolate.providers.scale.ReverseSmoothInterpolatorProvider;
import com.slamzoom.android.interpolate.providers.scale.SlamHardInterpolatorProvider;
import com.slamzoom.android.interpolate.providers.scale.SlamSoftInterpolatorProvider;
import com.slamzoom.android.interpolate.providers.scale.SmoothInterpolatorProvider;
import com.slamzoom.android.interpolate.providers.translate.MegaShakeInterpolatorProvider;
import com.slamzoom.android.interpolate.providers.translate.ShakeInterpolatorProvider;
import com.slamzoom.android.interpolate.providers.translate.SuperShakeInterpolatorProvider;

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
          .withName("simple x4")
          .addEffectStep(EffectStep.newBuilder()
              .withEndPauseSeconds(1)
              .withScaleInterpolatorProvider(new SmoothInterpolatorProvider())
              .build())
          .withNumTilesInRow(2)
          .build());
      mModels.add(EffectModel.newBuilder()
          .withName("simple x25")
          .addEffectStep(EffectStep.newBuilder()
              .withEndPauseSeconds(1)
              .withScaleInterpolatorProvider(new SmoothInterpolatorProvider())
              .build())
          .withNumTilesInRow(5)
          .build());
      mModels.add(EffectModel.newBuilder()
          .withName("slam x9")
          .addEffectStep(EffectStep.newBuilder()
              .withEndPauseSeconds(1)
              .withScaleInterpolatorProvider(new SlamHardInterpolatorProvider())
              .build())
          .withNumTilesInRow(3)
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
          .withName("quake x9")
          .addEffectStep(EffectStep.newBuilder()
              .withScaleInterpolatorProvider(new ReverseSmoothInterpolatorProvider())
              .withTranslateInterpolatorProvider(new ShakeInterpolatorProvider())
              .build())
          .withNumTilesInRow(3)
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
          .withName("crashbounce x9")
          .addEffectStep(EffectStep.newBuilder()
              .withStartPauseSeconds(0.3f)
              .withDurationSeconds(0.7f)
              .withScaleAndTranslateInterpolatorProvider(new CrashBounceDIagonalInterpolatorProvider())
              .build())
          .withNumTilesInRow(3)
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
