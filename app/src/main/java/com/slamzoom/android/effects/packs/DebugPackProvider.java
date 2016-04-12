package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.interpolaters.base.ConstantInterpolator;
import com.slamzoom.android.interpolaters.base.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolaters.base.spline.PointListBuilder;
import com.slamzoom.android.interpolaters.combo.scaletranslate.CrashInterpolatorProvider;
import com.slamzoom.android.interpolaters.combo.translate.NoTranslateInterpolatorProvider;
import com.slamzoom.android.interpolaters.filter.GuassianUnblurFilterInterpolator;
import com.slamzoom.android.interpolaters.filter.UnsaturateFilterInterpolator;
import com.slamzoom.android.interpolaters.filter.ZoomBlurFilterInterpolator;
import com.slamzoom.android.interpolaters.single.IdentityInterpolator;
import com.slamzoom.android.interpolaters.single.NoScaleInterpolator;
import com.slamzoom.android.interpolaters.single.SlamHardInAndOutInterpolator;
import com.slamzoom.android.interpolaters.single.SlamHardInterpolator;
import com.slamzoom.android.interpolaters.single.SlamHardNoPauseInterpolator;
import com.slamzoom.android.interpolaters.single.SlamSoftOutNoPauseInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 4/8/16.
 */
public class DebugPackProvider {
  public static List<EffectTemplate> getPack() {
    List<EffectTemplate> packModels = Lists.newArrayList();

    packModels.add(EffectTemplate.newBuilder()
        .withPackName("debug pack")
        .withName("stationary")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new IdentityInterpolator())
            .withStartPauseSeconds(3f)
            .withDurationSeconds(0.2f)
            .withEndPauseSeconds(3f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withPackName("debug pack")
        .withName("crash")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.3f)
            .withDurationSeconds(0.7f)
            .withScaleAndTranslateInterpolatorProvider(new CrashInterpolatorProvider())
            .build())
        .build());


    return packModels;
  }
}
