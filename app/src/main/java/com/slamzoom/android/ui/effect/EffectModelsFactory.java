package com.slamzoom.android.ui.effect;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.slamzoom.android.interpolate.IdentityInterpolator;
import com.slamzoom.android.interpolate.ReverseIdentityInterpoaltor;
import com.slamzoom.android.interpolate.combo.scaletranslate.BackAndForthInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.scaletranslate.CrashBounceDIagonalInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.scaletranslate.CrashBounceBottomInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.scaletranslate.CrashInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.scaletranslate.FlushInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.scaletranslate.SpiralInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.scaletranslate.WizzyWazzleInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.scaletranslate.ZigZagInterpolatorProvider;
import com.slamzoom.android.interpolate.filter.GuassianUnblurInterpolator;
import com.slamzoom.android.interpolate.single.EaseInSlamHardInterpolator;
import com.slamzoom.android.interpolate.single.InAndOutInterpolator;
import com.slamzoom.android.interpolate.single.SlamHardInterpolator;
import com.slamzoom.android.interpolate.single.SlamSoftInterpolator;
import com.slamzoom.android.interpolate.combo.translate.MegaShakeInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.translate.ShakeInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.translate.SuperShakeInterpolatorProvider;

import java.util.List;

/**
 * Created by antrob on 2/21/16.
 */
public class EffectModelsFactory {
  private static List<EffectModel> mModels = Lists.newArrayList(
      EffectModel.newBuilder()
          .withName("simple")
          .addEffectStep(EffectStep.newBuilder()
              .withEndPauseSeconds(1)
              .withScaleInterpolator(new IdentityInterpolator())
              .build())
          .build(),
      EffectModel.newBuilder()
          .withName("blur")
          .addEffectStep(EffectStep.newBuilder()
              .withEndPauseSeconds(1)
              .withScaleInterpolator(new IdentityInterpolator())
              .withFilterInterpolator(new GuassianUnblurInterpolator())
              .build())
          .build(),
      EffectModel.newBuilder()
          .withName("slamblur")
          .addEffectStep(EffectStep.newBuilder()
              .withEndPauseSeconds(1)
              .withScaleInterpolator(new SlamHardInterpolator())
              .withFilterInterpolator(new GuassianUnblurInterpolator())
              .build())
          .build(),
      EffectModel.newBuilder()
          .withName("simple x4")
          .addEffectStep(EffectStep.newBuilder()
              .withEndPauseSeconds(1)
              .withScaleInterpolator(new IdentityInterpolator())
              .build())
          .withNumTilesInRow(2)
          .build(),
      EffectModel.newBuilder()
          .withName("simple x25")
          .addEffectStep(EffectStep.newBuilder()
              .withEndPauseSeconds(1)
              .withScaleInterpolator(new IdentityInterpolator())
              .build())
          .withNumTilesInRow(5)
          .build(),
      EffectModel.newBuilder()
          .withName("slam x9")
          .addEffectStep(EffectStep.newBuilder()
              .withEndPauseSeconds(1)
              .withScaleInterpolator(new SlamHardInterpolator())
              .build())
          .withNumTilesInRow(3)
          .build(),
      EffectModel.newBuilder()
          .withName("soft")
          .addEffectStep(EffectStep.newBuilder()
              .withEndPauseSeconds(1)
              .withScaleInterpolator(new SlamSoftInterpolator())
              .build())
          .build(),
      EffectModel.newBuilder()
          .withName("slam")
          .addEffectStep(EffectStep.newBuilder()
              .withEndPauseSeconds(1)
              .withScaleInterpolator(new SlamHardInterpolator())
              .build())
          .build(),
      EffectModel.newBuilder()
          .withName("ghist")
          .addEffectStep(EffectStep.newBuilder()
              .withStartPauseSeconds(1)
              .withEndPauseSeconds(1)
              .withScaleInterpolator(new EaseInSlamHardInterpolator())
              .withTranslateInterpolator(new ShakeInterpolatorProvider())
              .build())
          .build(),
      EffectModel.newBuilder()
          .withName("superghist")
          .addEffectStep(EffectStep.newBuilder()
              .withStartPauseSeconds(1)
              .withEndPauseSeconds(1)
              .withScaleInterpolator(new EaseInSlamHardInterpolator())
              .withTranslateInterpolator(new SuperShakeInterpolatorProvider())
              .build())
          .build(),
      EffectModel.newBuilder()
          .withName("megaghist")
          .addEffectStep(EffectStep.newBuilder()
              .withScaleInterpolator(new EaseInSlamHardInterpolator())
              .withTranslateInterpolator(new MegaShakeInterpolatorProvider())
              .build())
          .build(),
      EffectModel.newBuilder()
          .withName("reverse")
          .addEffectStep(EffectStep.newBuilder()
              .withScaleInterpolator(new ReverseIdentityInterpoaltor())
              .build())
          .build(),
      EffectModel.newBuilder()
          .withName("quake")
          .addEffectStep(EffectStep.newBuilder()
              .withScaleInterpolator(new InAndOutInterpolator())
              .withTranslateInterpolator(new ShakeInterpolatorProvider())
              .build())
          .build(),
      EffectModel.newBuilder()
          .withName("quake x9")
          .addEffectStep(EffectStep.newBuilder()
              .withScaleInterpolator(new InAndOutInterpolator())
              .withTranslateInterpolator(new ShakeInterpolatorProvider())
              .build())
          .withNumTilesInRow(3)
          .build(),
      EffectModel.newBuilder()
          .withName("superquake")
          .addEffectStep(EffectStep.newBuilder()
              .withScaleInterpolator(new InAndOutInterpolator())
              .withTranslateInterpolator(new SuperShakeInterpolatorProvider())
              .build())
          .build(),
      EffectModel.newBuilder()
          .withName("megaquake")
          .addEffectStep(EffectStep.newBuilder()
              .withScaleInterpolator(new InAndOutInterpolator())
              .withTranslateInterpolator(new MegaShakeInterpolatorProvider())
              .build())
          .build(),
      EffectModel.newBuilder()
          .withName("flush")
          .addEffectStep(EffectStep.newBuilder()
              .withDurationSeconds(3f)
              .withEndPauseSeconds(1f)
              .withScaleAndTranslateInterpolatorProvider(new FlushInterpolatorProvider())
              .build())
          .build(),
      EffectModel.newBuilder()
          .withName("spiral")
          .addEffectStep(EffectStep.newBuilder()
              .withDurationSeconds(3f)
              .withEndPauseSeconds(1f)
              .withScaleAndTranslateInterpolatorProvider(new SpiralInterpolatorProvider())
              .build())
          .build(),
      EffectModel.newBuilder()
          .withName("zigzag")
          .addEffectStep(EffectStep.newBuilder()
              .withDurationSeconds(3f)
              .withEndPauseSeconds(1f)
              .withScaleAndTranslateInterpolatorProvider(new ZigZagInterpolatorProvider())
              .build())
          .build(),
      EffectModel.newBuilder()
          .withName("methodman")
          .addEffectStep(EffectStep.newBuilder()
              .withDurationSeconds(1f)
              .withScaleAndTranslateInterpolatorProvider(new BackAndForthInterpolatorProvider())
              .build())
          .build(),
      EffectModel.newBuilder()
          .withName("wizzywazzle")
          .addEffectStep(EffectStep.newBuilder()
              .withDurationSeconds(2f)
              .withScaleAndTranslateInterpolatorProvider(new WizzyWazzleInterpolatorProvider())
              .build())
          .build(),
      EffectModel.newBuilder()
          .withName("crash")
          .addEffectStep(EffectStep.newBuilder()
              .withStartPauseSeconds(0.3f)
              .withDurationSeconds(0.7f)
              .withScaleAndTranslateInterpolatorProvider(new CrashInterpolatorProvider())
              .build())
          .build(),
      EffectModel.newBuilder()
          .withName("crashbounce")
          .addEffectStep(EffectStep.newBuilder()
              .withStartPauseSeconds(0.5f)
              .withDurationSeconds(1f)
              .withScaleAndTranslateInterpolatorProvider(new CrashBounceDIagonalInterpolatorProvider())
              .build())
          .build(),
      EffectModel.newBuilder()
          .withName("crashbounce x9")
          .addEffectStep(EffectStep.newBuilder()
              .withStartPauseSeconds(0.3f)
              .withDurationSeconds(0.7f)
              .withScaleAndTranslateInterpolatorProvider(new CrashBounceDIagonalInterpolatorProvider())
              .build())
          .withNumTilesInRow(3)
          .build(),
      EffectModel.newBuilder()
          .withName("crashbounce2")
          .addEffectStep(EffectStep.newBuilder()
              .withStartPauseSeconds(0.5f)
              .withDurationSeconds(1f)
              .withScaleAndTranslateInterpolatorProvider(new CrashBounceBottomInterpolatorProvider())
              .build())
          .build());

  private static List<String> mEffectNames = Lists.newArrayList(
      "simple",
      "blur",
      "slamblur"
  );

  public static List<EffectModel> getTemplates() {
    return Lists.newArrayList(Iterables.filter(mModels, new Predicate<EffectModel>() {
      @Override
      public boolean apply(EffectModel input) {
        return mEffectNames.contains(input.getName());
      }
    }));
  }
}
