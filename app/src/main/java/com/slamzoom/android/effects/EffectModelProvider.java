package com.slamzoom.android.effects;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.slamzoom.android.ui.create.effectchooser.EffectModel;

import java.util.List;

/**
 * Created by clocksmith on 4/14/16.
 */
public class EffectModelProvider {
  private static List<EffectModel> mModels;

  public static List<EffectModel> getEffectModels() {
    if (mModels == null) {
      mModels = Lists.newArrayList(Lists.transform(EffectTemplateProvider.getTemplates(),
          new Function<EffectTemplate, EffectModel>() {
            @Override
            public EffectModel apply(EffectTemplate input) {
              return new EffectModel(input);
            }
          }));
    }
    return mModels;
  }

  public static EffectModel getEffectModel(String effectName) {
    for (EffectModel effectModel : mModels) {
      if (effectModel.getEffectTemplate().getName().equals(effectName)) {
        return effectModel;
      }
    }
    return null;
  }
}
