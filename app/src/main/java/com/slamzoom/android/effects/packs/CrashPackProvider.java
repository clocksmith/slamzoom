package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.templates.EffectTemplate;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.effects.interpolation.filter.single.GaussianBlurFilterInterpolator;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashRumbleInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashTaranInterpolatorProvider;
import com.slamzoom.android.interpolators.custom.OvershootInterpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolators.spline.PointListBuilder;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashBounceBottomInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashMissInterpolatorProvider;

import java.util.List;

/**
 * Created by clocksmith on 3/22/16.
 */
public class CrashPackProvider {
  public static List<EffectTemplate> getPack() {
    List<EffectTemplate> packModels = Lists.newArrayList();
    packModels.add(EffectTemplate.newBuilder()
        .withName("crashmiss")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.75f)
            .withDurationSeconds(0.75f)
            .withEndPauseSeconds(2f)
            .withScaleAndTranslateInterpolatorProvider(new CrashMissInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("crashin")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(1f)
            .withEndPauseSeconds(2f)
            .withScaleAndTranslateInterpolatorProvider(new CrashBounceBottomInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("overcrash")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(1f)
            .withEndPauseSeconds(2f)
            .withScaleInterpolator(new OvershootInterpolator())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("crashblur")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.75f)
            .withDurationSeconds(0.75f)
            .withEndPauseSeconds(2f)
            .withScaleAndTranslateInterpolatorProvider(new CrashTaranInterpolatorProvider())
            .withFilterInterpolator(new GaussianBlurFilterInterpolator(
                new LinearSplineInterpolator(PointListBuilder.create()
                    .add(0, 0)
                    .add(0.3f, 1)
                    .add(0.6f, 1)
                    .add(0.9f, 0)
                    .add(1, 0)
                    .build())) {
              @Override
              public float getBlurSize() {
                return super.getBlurSize() * 2;
              }
            })
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("crashrumble")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(1.5f)
            .withScaleAndTranslateInterpolatorProvider(new CrashRumbleInterpolatorProvider())
            .build())
        .build());

    return packModels;
  }
}
