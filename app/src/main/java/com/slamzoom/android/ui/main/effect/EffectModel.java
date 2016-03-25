package com.slamzoom.android.ui.main.effect;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by antrob on 2/21/16.
 */
public class EffectModel {
  private String mPackName;
  private String mName;
  private List<EffectStep> mEffectSteps;
  private byte[] mGifBytes;
  private int mNumTilesInRow;

  public static Builder newBuilder() {
    return new Builder();
  }

  private EffectModel(String packName, String name, List<EffectStep> effectSteps, int numTilesInRow) {
    mPackName = packName;
    mName = name;
    mEffectSteps = effectSteps;
    mNumTilesInRow = numTilesInRow;
  }

  public String getPackName() {
    return mPackName;
  }

  public String getName() {
    return mName;
  }

  public List<EffectStep> getEffectSteps() {
    return mEffectSteps;
  }

  public byte[] getGifBytes() {
    return mGifBytes;
  }

  public void setGifBytes(byte[] gifBytes) {
    mGifBytes = gifBytes;
  }

  public int getNumTilesInRow() {
    return mNumTilesInRow;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(mPackName, mName, mEffectSteps);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final EffectModel other = (EffectModel) obj;
    return Objects.equal(mPackName, other.getPackName())
        && Objects.equal(mName, other.getName())
        && Objects.equal(mEffectSteps, other.getEffectSteps());
  }

  public static class Builder {
    private String mPackName;
    private String mName;
    private List<EffectStep> mEffectSteps;
    private int mNumTilesInRow = 1;

    public Builder() {
      mEffectSteps = Lists.newArrayList();
    }

    public Builder withPackName(String packName) {
      mPackName = packName;
      return this;
    }

    public Builder withName(String name) {
      mName = name;
      return this;
    }

    public Builder addEffectStep(EffectStep effectStep) {
      mEffectSteps.add(effectStep);
      return this;
    }

    public Builder withNumTilesInRow(int numTilesInRow) {
      mNumTilesInRow = numTilesInRow;
      return this;
    }

    public EffectModel build() {
      return new EffectModel(mPackName, mName, mEffectSteps, mNumTilesInRow);
    }
  }
}
