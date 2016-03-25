package com.slamzoom.android.effects;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.packs.CrashPackProvider;
import com.slamzoom.android.effects.packs.ShakePackProvider;
import com.slamzoom.android.effects.packs.SimplePackProvider;
import com.slamzoom.android.effects.packs.SlamPackProvider;
import com.slamzoom.android.effects.packs.SwirlPackProvider;

import java.util.List;

/**
 * Created by antrob on 2/21/16.
 */
public class EffectModelProvider {
  private static List<EffectModel> mModels = Lists.newArrayList();
  static {
    mModels.addAll(SlamPackProvider.getPack());
    mModels.addAll(CrashPackProvider.getPack());
    mModels.addAll(SwirlPackProvider.getPack());
    mModels.addAll(ShakePackProvider.getPack());
    mModels.addAll(SimplePackProvider.getPack());
  }

  public static List<EffectModel> getModels() {
    return mModels;
  }
}
