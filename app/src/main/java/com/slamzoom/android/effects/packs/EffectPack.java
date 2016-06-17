package com.slamzoom.android.effects.packs;

import com.google.common.collect.ImmutableList;

/**
 * Created by clocksmith on 6/16/16.
 */
public class EffectPack {
  private String mName;
  private ImmutableList<String> mEffectNames;

  public EffectPack(String name, ImmutableList<String> effectNames) {
    mName = name;
    mEffectNames = effectNames;
  }

  public String getName() {
    return mName;
  }

  public ImmutableList<String> getEffectNames() {
    return mEffectNames;
  }
}
