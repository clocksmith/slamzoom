package com.slamzoom.android.effects;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by clocksmith on 6/16/16.
 */
public class EffectPack {
  private String mName;
  private ImmutableList<EffectTemplate> mEffectTemplates;

  public EffectPack(String name, ImmutableList<EffectTemplate> effectTemplates) {
    mName = name;
    mEffectTemplates = effectTemplates;
  }

  public String getName() {
    return mName;
  }

  public ImmutableList<EffectTemplate> getEffectTemplates() {
    return mEffectTemplates;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private String mName;
    private List<EffectTemplate> mEffectTemplates;

    public Builder() {
      mEffectTemplates = Lists.newArrayList();
    }

    public Builder withName(String name) {
      mName = name;
      return this;
    }

    public Builder withEffectTemplate(String effectName) {
      mEffectTemplates.add(EffectTemplates.consume(effectName));
      return this;
    }

    public EffectPack build() {
      for (EffectTemplate effectTemplate : mEffectTemplates) {
        effectTemplate.setPackName(mName);
      }
      return new EffectPack(mName, ImmutableList.copyOf(mEffectTemplates));
    }
  }
}
