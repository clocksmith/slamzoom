package com.slamzoom.android.effects;

import android.content.Context;
import android.support.annotation.NonNull;

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
public class EffectPacks {
  private static ImmutableList<EffectPack> EFFECT_PACKS;
  private static ImmutableList<EffectPack> DEBUG_EFFECT_PACKS;
  private static ImmutableList<EffectTemplate> EFFECT_TEMPLATES_FROM_PACKS;
  private static ImmutableList<EffectTemplate> DEBUG_EFFECT_TEMPLATES_FROM_PACKS;

  public static void init(Context context) {
    EffectColors.init(context);

    EFFECT_PACKS = ImmutableList.<EffectPack>builder()
        .add(EffectPack.newBuilder()
            .withName("teal pack")
            .withColorGroup("teal")
            .withEffectTemplate("slamin")
            .withEffectTemplate("smush")
            .withEffectTemplate("sumo")
            .withEffectTemplate("overcrash")
            .withEffectTemplate("crashrumble")
            .withEffectTemplate("crashdiag")
            .withEffectTemplate("mctwisty")
            .withEffectTemplate("doublebulge")
            .withEffectTemplate("earthquake")
            .withEffectTemplate("swirlspot")
            .build())
        .add(EffectPack.newBuilder()
            .withName("yellow pack")
            .withColorGroup("yellow")
            .withEffectTemplate("rumblestiltskin")
            .withEffectTemplate("inflate")
            .withEffectTemplate("blurcrash")
            .withEffectTemplate("blursmith")
            .withEffectTemplate("magoo")
            .withEffectTemplate("djswirls")
            .withEffectTemplate("slamout")
            .withEffectTemplate("flush")
            .withEffectTemplate("swirleyes")
            .withEffectTemplate("graytake")
            .build())
        .add(EffectPack.newBuilder()
            .withName("purple pack")
            .withColorGroup("deeppurple")
            .withEffectTemplate("crashmiss")
            .withEffectTemplate("rumbletease")
            .withEffectTemplate("shrinkydink")
            .withEffectTemplate("blurshake")
            .withEffectTemplate("grayfreeze")
            .withEffectTemplate("slamio")
            .withEffectTemplate("blurslam")
            .withEffectTemplate("rumble")
            .withEffectTemplate("swirlio")
            .withEffectTemplate("spiral")
            .build())
        .add(EffectPack.newBuilder()
            .withName("orange pack")
            .withColorGroup("deeporange")
            .withEffectTemplate("crashin")
            .withEffectTemplate("bulger")
            .withEffectTemplate("blurtease")
            .withEffectTemplate("whitetease")
            .withEffectTemplate("shakezilla")
            .withEffectTemplate("bulgeswap")
            .withEffectTemplate("grayslam")
            .withEffectTemplate("swirlout")
            .withEffectTemplate("twistyfroggy")
            .withEffectTemplate("swirlyeyes")
            .build())
        .add(EffectPack.newBuilder()
            .withName("blue pack")
            .withColorGroup("lightblue")
            .withEffectTemplate("crashblur")
            .withEffectTemplate("weirdo")
            .withEffectTemplate("blurmagic")
            .withEffectTemplate("blacktease")
            .withEffectTemplate("deflate")
            .withEffectTemplate("rumbleslam")
            .withEffectTemplate("grayrumble")
            .withEffectTemplate("swirlin")
            .withEffectTemplate("swirlslam")
            .withEffectTemplate("swirlyspot")
            .build())
        .build();

    DEBUG_EFFECT_PACKS = ImmutableList.<EffectPack>builder()
        .add(EffectPack.newBuilder()
            .withName("debug")
            .build())
        .build();

    EFFECT_TEMPLATES_FROM_PACKS =
        ImmutableList.copyOf(Iterables.concat(Lists.transform(EFFECT_PACKS,
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
    for (EffectPack effectPack : EFFECT_PACKS) {
      if (effectPack.getName().equals(packName)) {
        return effectPack;
      }
    }
    return null;
  }

  public static ImmutableList<EffectTemplate> listEffectTemplatesByPack() {
    return DebugUtils.DEBUG_USE_DEBUG_EFFECTS ?
        DEBUG_EFFECT_TEMPLATES_FROM_PACKS : EFFECT_TEMPLATES_FROM_PACKS;
  }

  public static ImmutableList<EffectModel> listEffectModelsByPack(@NonNull final List<String> unlockedPackNames) {
    return ImmutableList.copyOf(Lists.transform(EffectPacks.listEffectTemplatesByPack(),
        new Function<EffectTemplate, EffectModel>() {
          @Override
          public EffectModel apply(EffectTemplate input) {
            return new EffectModel(input, unlockedPackNames.contains(input.getPackName()));
          }
        }));
  }
}
