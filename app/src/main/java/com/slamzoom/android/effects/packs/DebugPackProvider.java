package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.interpolation.filter.single.BulgeFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.ZoomBlurFilterInterpolator;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashInterpolatorProvider;
import com.slamzoom.android.interpolators.effect.IdentityInterpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolators.spline.PointListBuilder;

import java.util.List;

/**
 * Created by clocksmith on 4/8/16.
 */
public class DebugPackProvider {
  public static List<EffectTemplate> getPack() {
    List<EffectTemplate> packModels = Lists.newArrayList();

    packModels.add(EffectTemplate.newBuilder()
        .withPackName("debug pack")
        .withName("test")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(1)
            .withEndPauseSeconds(1)
            .withDurationSeconds(3f)
            .withScaleInterpolator(new IdentityInterpolator())
            .withFilterInterpolator(new BulgeFilterInterpolator())
            .build())
        .build());

    return packModels;
  }
}
