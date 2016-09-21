package com.slamzoom.android.effects;

import android.content.Context;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.slamzoom.android.common.utils.DebugUtils;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.PopInConfig;
import com.slamzoom.android.effects.packs.EffectPack;
import com.slamzoom.android.effects.packs.distort.BlockheadEffectConfig;
import com.slamzoom.android.effects.packs.distort.BulgerinoEffectConfig;
import com.slamzoom.android.effects.packs.distort.BulgeSwapEffectConfig;
import com.slamzoom.android.effects.packs.distort.ShrinkEffectConfig;
import com.slamzoom.android.effects.packs.distort.DoublebulgeEffectConfig;
import com.slamzoom.android.effects.packs.distort.InflateEffectConfig;
import com.slamzoom.android.effects.packs.distort.SmushEfffectConfig;
import com.slamzoom.android.effects.packs.distort.SwirlEffectConfig;
import com.slamzoom.android.effects.packs.slam.CrashEffectConfig;
import com.slamzoom.android.effects.packs.slam.SlamioWithPauseEffectConfig;
import com.slamzoom.android.effects.packs.slam.WhirlEffectConfig;
import com.slamzoom.android.effects.packs.slam.FlashEffectConfig;
import com.slamzoom.android.effects.packs.slam.PounceEffectConfig;
import com.slamzoom.android.effects.packs.slam.RumblestiltskinEffectConfig;
import com.slamzoom.android.effects.packs.slam.SlaminEffectConfig;
import com.slamzoom.android.effects.packs.slam.SlamoutEffectConfig;
import com.slamzoom.android.effects.packs.slam.ThrowbackEffectConfig;
import com.slamzoom.android.ui.create.effectchooser.EffectModel;

import java.util.List;

/**
 * Created by clocksmith on 7/17/16.
 */
public class Effects {
  private static ImmutableList<EffectPack> EFFECT_PACKS_NEW_STRATEGY;
  private static ImmutableList<EffectTemplate> EFFECT_TEMPLATES_FROM_PACKS_NEW_STRATEGY;

  private static ImmutableList<EffectPack> DEBUG_EFFECT_PACKS;
  private static ImmutableList<EffectTemplate> DEBUG_EFFECT_TEMPLATES_FROM_PACKS;

  public enum Pack {
    SLAM, DISTORT, DEBUG
  }

  public static void init(Context context) {
    EffectColors.init(context);

    EFFECT_PACKS_NEW_STRATEGY = ImmutableList.<EffectPack>builder()
        .add(EffectPack.newBuilder()
            .withName(Pack.SLAM.name())
            .withEffectConfig(new SlaminEffectConfig())
            .withEffectConfig(new SlamoutEffectConfig())
            .withEffectConfig(new CrashEffectConfig())
            .withEffectConfig(new RumblestiltskinEffectConfig())
            .withEffectConfig(new ThrowbackEffectConfig())
            .withEffectConfig(new FlashEffectConfig())
            .withEffectConfig(new PounceEffectConfig())
            .withEffectConfig(new WhirlEffectConfig())
            .build())
        .add(EffectPack.newBuilder()
            .withName(Pack.DISTORT.name())
            .withEffectConfig(new BulgerinoEffectConfig())
            .withEffectConfig(new ShrinkEffectConfig())
            .withEffectConfig(new BlockheadEffectConfig())
            .withEffectConfig(new InflateEffectConfig())
            .withEffectConfig(new SmushEfffectConfig())
            .withEffectConfig(new SwirlEffectConfig())
            .withEffectConfig(new DoublebulgeEffectConfig())
            .withEffectConfig(new BulgeSwapEffectConfig())
            .build())
        .build();

    DEBUG_EFFECT_PACKS = ImmutableList.<EffectPack>builder()
        .add(EffectPack.newBuilder()
            .withName(Pack.DEBUG.name())
            .withEffectConfig(new SlamioWithPauseEffectConfig())
//            .withEffectConfig(new CrashEffectConfig() {
//              @Override
//              public float getDurationSeconds() {
//                return 1;
//              }
//            })
//            .withEffectConfig(new PopInConfig())
//            .withEffectTemplate(EffectTemplate.newSingleStepBuilder()
//                .withName("popout")
//                .withStartDurationEndSeconds(0, 0.175f, 0)
//                .withTransformInterpolatorProvider(new PopOutInterpolatorProvider())
//                .build())
//            .withEffectTemplate(EffectTemplate.newSingleStepBuilder()
//                .withName("slamin_no_end")
//                .withEffectConfig(new SlaminEffectConfig())
//                .withEndPauseSeconds(0)
//                .build())
//            .withEffectTemplate(EffectTemplate.newSingleStepBuilder()
//                .withName("slamio")
//                .withEffectConfig(new SlamioWithPauseEffectConfig())
//                .build())
//            .withEffectTemplate(EffectTemplate.newSingleStepBuilder()
//                .withName("crashblur-show")
//                .withStartDurationEndSeconds(0.25f, 0.75f, 1)
//                .withTransformInterpolatorProvider(new CrashTaranInterpolatorProvider())
//                .withFilterInterpolator(new GaussianBlurFilterInterpolator(LinearSplineInterpolator.newBuilder()
//                    .withPoint(0, 0)
//                    .withPoint(0.3f, 1)
//                    .withPoint(0.6f, 1)
//                    .withPoint(0.9f, 0)
//                    .withPoint(1, 0)
//                    .build()))
//                .build())
//            .withEffectTemplate(EffectTemplate.newSingleStepBuilder()
//                .withName("deflate-show")
//                .withStartDurationEndSeconds(0, 2, 0)
//                .withScaleInterpolator(new LinearInterpolator())
//                .withFilterInterpolators(new DeflateFaceFilterInterpolatorsProvider())
//                .build())
//            .withEffectTemplate(EffectTemplate.newSingleStepBuilder()
//                .withName("inflate-show")
//                .withStartDurationEndSeconds(0, 2, 0)
//                .withScaleInterpolator(new LinearInterpolator())
//                .withFilterInterpolators(new InflateFaceFilterInterpolatorsProvider())
//                .build())
//            .withEffectTemplate(EffectTemplate.newSingleStepBuilder()
//                .withName("rumblestiltskin-show")
//                .withStartDurationEndSeconds(0, 3, 0)
//                .withEffectConfig(new RumblestiltskinEffectConfig())
//                .build())
//            .withEffectTemplate(EffectTemplate.newSingleStepBuilder()
//                .withName("slamin-show")
//                .withStartDurationEndSeconds(0.4f, 0.6f, 1)
//                .withEffectConfig(new SlaminEffectConfig())
//                .build())
            .build())
        .build();


    EFFECT_TEMPLATES_FROM_PACKS_NEW_STRATEGY =
        ImmutableList.copyOf(Iterables.concat(Lists.transform(EFFECT_PACKS_NEW_STRATEGY,
            new Function<EffectPack, List<EffectTemplate>>() {
              @Override
              public List<EffectTemplate> apply(EffectPack input) {
                return input.getEffectTemplates();
              }
            })));

    DEBUG_EFFECT_TEMPLATES_FROM_PACKS =
        ImmutableList.copyOf(Iterables.concat(Lists.transform(DEBUG_EFFECT_PACKS,
            new Function<EffectPack, List<EffectTemplate>>() {
              @Override
              public List<EffectTemplate> apply(EffectPack input) {
                return input.getEffectTemplates();
              }
            })));
  }

  public static EffectPack getPack(String packName) {
    for (EffectPack effectPack : EFFECT_PACKS_NEW_STRATEGY) {
      if (effectPack.getName().equals(packName)) {
        return effectPack;
      }
    }
    return null;
  }

  public static ImmutableList<EffectTemplate> listEffectTemplates() {
    return DebugUtils.USE_DEBUG_EFFECTS ?
        DEBUG_EFFECT_TEMPLATES_FROM_PACKS : EFFECT_TEMPLATES_FROM_PACKS_NEW_STRATEGY;
  }

  // TODO(clocksmith): use more efficeint method.
  public static EffectTemplate getEffectTemplate(String effectName) {
    for (EffectTemplate effectTemplate : listEffectTemplates()) {
      if (effectName.equals(effectTemplate.getName())) {
        return effectTemplate;
      }
    }
    return null;
  }


  public static ImmutableList<EffectModel> createEffectModels() {
    return ImmutableList.copyOf(Lists.transform(Effects.listEffectTemplates(),
        new Function<EffectTemplate, EffectModel>() {
          @Override
          public EffectModel apply(EffectTemplate input) {
            return new EffectModel(input);
          }
        }));
  }
}
