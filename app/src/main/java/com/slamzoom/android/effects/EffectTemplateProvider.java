package com.slamzoom.android.effects;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.packs.DistortionPack1Provider;
import com.slamzoom.android.effects.packs.CrashPackProvider;
import com.slamzoom.android.effects.packs.RumblePackProvider;
import com.slamzoom.android.effects.packs.DistortionPack2Provider;
import com.slamzoom.android.effects.packs.SlamPackProvider;
import com.slamzoom.android.effects.packs.SwirlPackProvider;
import com.slamzoom.android.mediacreation.gif.GifCreator;

import java.util.List;

/**
 * Created by antrob on 2/21/16.
 */
public class EffectTemplateProvider {
  private static List<EffectTemplate> mTemplates = Lists.newArrayList();

  static {
    mTemplates.addAll(SlamPackProvider.getPack());
    mTemplates.addAll(CrashPackProvider.getPack());
    mTemplates.addAll(DistortionPack1Provider.getPack());
    mTemplates.addAll(DistortionPack2Provider.getPack());
    mTemplates.addAll(SwirlPackProvider.getPack());
    mTemplates.addAll(RumblePackProvider.getPack());


//    mTemplates.addAll(GrayPackProvider.getPack());
//    mTemplates.addAll(BlurPackProvider.getPack());
//    mTemplates.addAll(TilePackProvider.getPack());
//
//    mTemplates.addAll(TheLabPackProvider.getPack());
//
//    mTemplates.addAll(DebugPackProvider.getPack());
  }

  public static List<EffectTemplate> getTemplates() {
    return mTemplates;
  }
}
