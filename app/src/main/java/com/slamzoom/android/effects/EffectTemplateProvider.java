package com.slamzoom.android.effects;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.slamzoom.android.effects.packs.BulgePackProvider;
import com.slamzoom.android.effects.packs.CrashPackProvider;
import com.slamzoom.android.effects.packs.DebugPackProvider;
import com.slamzoom.android.effects.packs.ShakePackProvider;
import com.slamzoom.android.effects.packs.SimplePackProvider;
import com.slamzoom.android.effects.packs.SlamPackProvider;
import com.slamzoom.android.effects.packs.SwirlPackProvider;
import com.slamzoom.android.effects.packs.UncategorizedPackProvider;
import com.slamzoom.android.ui.create.effectchooser.EffectModel;

import java.util.List;

/**
 * Created by antrob on 2/21/16.
 */
public class EffectTemplateProvider {
  private static List<EffectTemplate> mTemplates = Lists.newArrayList();

  static {
    mTemplates.addAll(SlamPackProvider.getPack());
    mTemplates.addAll(CrashPackProvider.getPack());
    mTemplates.addAll(SwirlPackProvider.getPack());
    mTemplates.addAll(ShakePackProvider.getPack());
    mTemplates.addAll(SimplePackProvider.getPack());
    mTemplates.addAll(BulgePackProvider.getPack());
    mTemplates.addAll(UncategorizedPackProvider.getPack());

//    mTemplates.addAll(DebugPackProvider.getPack());
  }

  public static List<EffectTemplate> getTemplates() {
    return mTemplates;
  }
}
