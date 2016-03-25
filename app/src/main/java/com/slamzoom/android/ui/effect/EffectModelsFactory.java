package com.slamzoom.android.ui.effect;

import com.google.common.collect.Lists;
import com.slamzoom.android.ui.effect.packs.CrashPackProvider;
import com.slamzoom.android.ui.effect.packs.ShakePackProvider;
import com.slamzoom.android.ui.effect.packs.SimplePackProvider;
import com.slamzoom.android.ui.effect.packs.SlamPackProvider;
import com.slamzoom.android.ui.effect.packs.SwirlPackProvider;

import java.util.List;

/**
 * Created by antrob on 2/21/16.
 */
public class EffectModelsFactory {
  private static List<EffectModel> mModels = Lists.newArrayList();
  static {
//    mModels.addAll(SlamPackProvider.getPack());
//    mModels.addAll(SwirlPackProvider.getPack());
//    mModels.addAll(ShakePackProvider.getPack());
//    mModels.addAll(SimplePackProvider.getPack());
    mModels.addAll(CrashPackProvider.getPack());
  }

  public static List<EffectModel> getTemplates() {
    return mModels;
  }
}
