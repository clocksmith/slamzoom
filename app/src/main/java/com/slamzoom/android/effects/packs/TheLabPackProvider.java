package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeEyesSwirlMouthFilterInterpoaltor;
import com.slamzoom.android.effects.interpolation.filter.group.UnswirlEyesFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlAtHotspotOnHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlFilterInterpolator;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashBounceDiagonalInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.FlushInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CircleCenterInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.ShakeInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnderExposeFilterInterpolator;
import com.slamzoom.android.interpolators.LinearInterpolator;
import com.slamzoom.android.interpolators.ReverseLinearInterpolator;
import com.slamzoom.android.interpolators.custom.InAndOutInterpolator;
import com.slamzoom.android.interpolators.custom.SlamSoftInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 3/31/16.
 */
public class TheLabPackProvider {
  public static List<EffectTemplate> getPack() {
    List<EffectTemplate> packModels = Lists.newArrayList();

    packModels.add(EffectTemplate.newBuilder()
        .withName("crashdiag")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(1f)
            .withScaleAndTranslateInterpolatorProvider(new CrashBounceDiagonalInterpolatorProvider())
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("lol")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(1)
            .withDurationSeconds(4f)
            .withScaleInterpolator(new Interpolator() {
              @Override
              protected float getRangePercent(float t) {
                if (t < 0.1667) {
                  return 0;
                } else if (t < 0.5) {
                  return 0.15f;
                } else if (t < 0.8333) {
                  return 0.50f;
                } else {
                  return 1;
                }
              }
            })
            .withFilterInterpolator(new UnderExposeFilterInterpolator(
                new Interpolator() {
                  @Override
                  protected float getRangePercent(float t) {
                    return (float) Math.pow(Math.sin(3 * Math.PI * t), 10);
                  }
                }))
            .withEndPauseSeconds(1)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("flush")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleAndTranslateInterpolatorProvider(new FlushInterpolatorProvider())
            .withDurationSeconds(3f)
            .withEndPauseSeconds(1f)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("spiral")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleAndTranslateInterpolatorProvider(new CircleCenterInterpolatorProvider())
            .withDurationSeconds(3f)
            .withEndPauseSeconds(1f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlout")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new ReverseLinearInterpolator())
            .withFilterInterpolator(new UnswirlFilterInterpolator())
            .withEndPauseSeconds(0.5f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("twistyfrog")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new BulgeEyesSwirlMouthFilterInterpoaltor())
            .withEndPauseSeconds(1)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("twistyfroggy")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new InAndOutInterpolator())
            .withFilterInterpolatorGroup(new BulgeEyesSwirlMouthFilterInterpoaltor())
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlslam")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new SlamSoftInterpolator())
            .withTranslateInterpolator(new ShakeInterpolatorProvider())
            .withFilterInterpolator(new UnswirlAtHotspotFilterInterpolator())
            .withEndPauseSeconds(0.5f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlyhead")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new InAndOutInterpolator())
            .withFilterInterpolator(new UnswirlAtHotspotOnHotspotFilterInterpolator())
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("swirleyes")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new UnswirlEyesFilterInterpolatorGroup())
            .withEndPauseSeconds(1)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlyeyes")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new InAndOutInterpolator())
            .withFilterInterpolatorGroup(new UnswirlEyesFilterInterpolatorGroup())
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlaway")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolator(new UnswirlAtHotspotFilterInterpolator())
            .withDurationSeconds(3f)
            .withEndPauseSeconds(0.5f)
            .build())
        .build());

    return packModels;
  }
}
