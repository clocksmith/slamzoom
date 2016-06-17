package com.slamzoom.android.effects;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.group.FilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.transform.ScaleAndTranslateInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.TranslateInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;

import java.util.List;

/**
 * Created by antrob on 2/21/16.
 */
public class EffectTemplate {
  private String mPackName;
  private String mName;
  private List<EffectStep> mEffectSteps;
  private int mNumTilesInRow;

  public static SingleEffectStepBuilder newSingleStepBuilder() {
    return new SingleEffectStepBuilder();
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  private EffectTemplate(String packName, String name, List<EffectStep> effectSteps, int numTilesInRow) {
    mPackName = packName;
    mName = name;
    mEffectSteps = effectSteps;
    mNumTilesInRow = numTilesInRow;
  }

  public String getPackName() {
    return mPackName;
  }

  public void setPackName(String packName) {
    mPackName = packName;
  }

  public String getName() {
    return mName;
  }

  public List<EffectStep> getEffectSteps() {
    return mEffectSteps;
  }

  public int getNumTilesInRow() {
    return mNumTilesInRow;
  }

  public static class SingleEffectStepBuilder {
    private String mPackName;
    private String mName;
    private EffectStep.Builder mEffectStepBuilder;
    private int mNumTilesInRow = 1;

    public SingleEffectStepBuilder() {
      mEffectStepBuilder = EffectStep.newBuilder();
    }

    public SingleEffectStepBuilder withPackName(String packName) {
      mPackName = packName;
      return this;
    }

    public SingleEffectStepBuilder withName(String name) {
      mName = name;
      return this;
    }

    public SingleEffectStepBuilder withScaleInterpolator(Interpolator interpolator) {
      mEffectStepBuilder.withScaleInterpolator(interpolator);
      return this;
    }

    public SingleEffectStepBuilder withXInterpolator(Interpolator interpolator) {
      mEffectStepBuilder.withXInterpolator(interpolator);
      return this;
    }

    public SingleEffectStepBuilder withYInterpolator(Interpolator interpolator) {
      mEffectStepBuilder.withYInterpolator(interpolator);
      return this;
    }

    public SingleEffectStepBuilder withTranslateInterpolator(TranslateInterpolatorProvider interpolatorProvider) {
      mEffectStepBuilder.withTranslateInterpolator(interpolatorProvider);
      return this;
    }

    public SingleEffectStepBuilder withScaleAndTranslateInterpolatorProvider(
        ScaleAndTranslateInterpolatorProvider interpolatorProvider) {
      mEffectStepBuilder.withScaleAndTranslateInterpolatorProvider(interpolatorProvider);
      return this;
    }

    public SingleEffectStepBuilder withFilterInterpolator(FilterInterpolator filterInterpolator) {
      mEffectStepBuilder.withFilterInterpolator(filterInterpolator);
      return this;
    }

    public SingleEffectStepBuilder withFilterInterpolatorGroup(FilterInterpolatorGroup filterInterpolatorGroup) {
      mEffectStepBuilder.withFilterInterpolatorGroup(filterInterpolatorGroup);
      return this;
    }

    public SingleEffectStepBuilder withDurationSeconds(float durationSeconds) {
      mEffectStepBuilder.withDurationSeconds(durationSeconds);
      return this;
    }

    public SingleEffectStepBuilder withStartPauseSeconds(float startPauseSeconds) {
      mEffectStepBuilder.withStartPauseSeconds(startPauseSeconds);
      return this;
    }

    public SingleEffectStepBuilder withEndPauseSeconds(float endPauseSeconds) {
      mEffectStepBuilder.withEndPauseSeconds(endPauseSeconds);
      return this;
    }

    public SingleEffectStepBuilder withStartDurationEndSeconds(float start, float duration, float end) {
      return this
          .withStartPauseSeconds(start)
          .withDurationSeconds(duration)
          .withEndPauseSeconds(end);
    }

    public EffectTemplate build() {
      return new EffectTemplate(
          mPackName,
          mName,
          Lists.newArrayList(mEffectStepBuilder.build()),
          mNumTilesInRow);
    }
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

    public EffectTemplate build() {
      return new EffectTemplate(mPackName, mName, mEffectSteps, mNumTilesInRow);
    }
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
    final EffectTemplate other = (EffectTemplate) obj;
    return Objects.equal(mPackName, other.getPackName())
        && Objects.equal(mName, other.getName())
        && Objects.equal(mEffectSteps, other.getEffectSteps());
  }
}
