package com.slamzoom.android.effects.packs;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectColors;
import com.slamzoom.android.effects.EffectConfig;
import com.slamzoom.android.effects.EffectTemplate;

import java.util.List;

/**
 * Created by clocksmith on 6/16/16.
 */
public class EffectPack {
  private String mName;
  private String mColorGroup = "darkerRainbow";
  private int mColor;
  private ImmutableList<EffectTemplate> mEffectTemplates;

  public EffectPack(String name, String colorGroup, int color, ImmutableList<EffectTemplate> effectTemplates) {
    mName = name;
    mColorGroup = colorGroup;
    mColor = color;
    mEffectTemplates = effectTemplates;
  }

  public String getName() {
    return mName;
  }

  public String getColorGroup() {
    return mColorGroup;
  }

  public int getColor() {
    return mColor;
  }

  public ImmutableList<EffectTemplate> getEffectTemplates() {
    return mEffectTemplates;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private String mName;
    private String mColorGroup = "darkerRainbow";
    private int mColor;
    private List<EffectTemplate> mEffectTemplates;

    public Builder() {
      mEffectTemplates = Lists.newArrayList();
    }

    public Builder withName(String name) {
      mName = name;
      return this;
    }

    public Builder withColorGroup(String colorGroup) {
      mColorGroup = colorGroup;
      return this;
    }

    public Builder withEffectTemplate(EffectTemplate effectTemplate) {
      mEffectTemplates.add(effectTemplate);
      return this;
    }

    public Builder withEffectConfig(EffectConfig effectConfig) {
      mEffectTemplates.add(EffectTemplate.create(effectConfig));
      return this;
    }

    public EffectPack build() {
      mColor = EffectColors.getColorGroup(mColorGroup).get(0);
      int count = 0;
      for (EffectTemplate effectTemplate : mEffectTemplates) {
        effectTemplate.setPackName(mName);
        effectTemplate.setColor(EffectColors.getColorGroup(mColorGroup).get(
            count % EffectColors.getColorGroup(mColorGroup).size()));
        count++;
      }

      return new EffectPack(mName, mColorGroup, mColor, ImmutableList.copyOf(mEffectTemplates));
    }
  }
}
