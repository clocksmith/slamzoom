package com.slamzoom.android.effects;

import android.content.Context;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.slamzoom.android.common.utils.DebugUtils;
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
    STARTER, DISTORT, DEBUG
  }

  public static void init(Context context) {
    EffectColors.init(context);

    EFFECT_PACKS_NEW_STRATEGY = ImmutableList.<EffectPack>builder()
        .add(EffectPack.newBuilder()
            .withName(Pack.STARTER.name())
            .withColorGroup("rainbow")
            .withEffectTemplate("slamin")
            .withEffectTemplate("slamout")
            .withEffectTemplate("crashblur")
            .withEffectTemplate("rumblestiltskin")
            .withEffectTemplate("throwback")
            .withEffectTemplate("flashreveal")
            .withEffectTemplate("rumbleslam")
            .withEffectTemplate("flushslam")
            .build())
        .add(EffectPack.newBuilder()
            .withName(Pack.DISTORT.name())
            .withColorGroup("rainbow")
            .withEffectTemplate("bulge")
            .withEffectTemplate("blockhead")
            .withEffectTemplate("inflate")
            .withEffectTemplate("deflate")
            .withEffectTemplate("swirlspot")
            .withEffectTemplate("doublebulge")
            .withEffectTemplate("bulgeswap")
            .withEffectTemplate("smush")
            .build())
        .build();

    DEBUG_EFFECT_PACKS = ImmutableList.<EffectPack>builder()
        .add(EffectPack.newBuilder()
            .withName(Pack.DEBUG.name())
            .withColorGroup("rainbow")
//            .withEffectTemplate("debug1")
//            .withEffectTemplate("popin")
//            .withEffectTemplate("popout")
            .withEffectTemplate("crashblur-show")
            .withEffectTemplate("deflate-show")
            .withEffectTemplate("inflate-show")
            .withEffectTemplate("rumblestiltskin-show")
            .withEffectTemplate("slamin-show")
            .build())
        .build();

    EFFECT_TEMPLATES_FROM_PACKS_NEW_STRATEGY   =
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

  public static ImmutableList<EffectTemplate> listEffectTemplatesByPack() {
    return DebugUtils.USE_DEBUG_EFFECTS ?
        DEBUG_EFFECT_TEMPLATES_FROM_PACKS : EFFECT_TEMPLATES_FROM_PACKS_NEW_STRATEGY;
  }

  public static ImmutableList<EffectModel> createEffectModels() {
    return ImmutableList.copyOf(Lists.transform(Effects.listEffectTemplatesByPack(),
        new Function<EffectTemplate, EffectModel>() {
          @Override
          public EffectModel apply(EffectTemplate input) {
            return new EffectModel(input);
          }
        }));
  }
}
