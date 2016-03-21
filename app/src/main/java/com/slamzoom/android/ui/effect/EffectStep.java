package com.slamzoom.android.ui.effect;

import android.graphics.Rect;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.interpolate.Interpolator;
import com.slamzoom.android.interpolate.combo.ScaleAndTranslateInterpolatorProvider;
import com.slamzoom.android.interpolate.filter.FilterInterpolator;
import com.slamzoom.android.interpolate.single.NoScaleInterpolator;
import com.slamzoom.android.interpolate.combo.TranslateInterpolatorProvider;
import com.slamzoom.android.interpolate.combo.translate.NoTranslateInterpolatorProvider;

import java.util.List;

/**
 * Created by clocksmith on 2/21/16.
 */
public class EffectStep {
  private Rect mHotspot;

  private Interpolator mScaleInterpolator;
  private Interpolator mXInterpolator;
  private Interpolator mYInterpolator;
  private List<FilterInterpolator> mFilternterpolators;
  private float mDurationSeconds;
  private float mStartPauseSeconds;
  private float mEndPauseSeconds;

  public static Builder newBuilder() {
    return new Builder();
  }

  public EffectStep(
      Interpolator scaleInterpolator,
      Interpolator xInterpolator,
      Interpolator yInterpolator,
      List<FilterInterpolator> filterInterpolators,
      float durationSeconds,
      float startPauseSeconds,
      float endPauseSeconds) {
    mScaleInterpolator = scaleInterpolator;
    mXInterpolator = xInterpolator;
    mYInterpolator = yInterpolator;
    mFilternterpolators = filterInterpolators;
    mDurationSeconds = durationSeconds;
    mStartPauseSeconds = startPauseSeconds;
    mEndPauseSeconds = endPauseSeconds;
  }

  public Rect getHotspot() {
    return mHotspot;
  }

  public void setHotspot(Rect hotspot) {
    mHotspot = hotspot;
  }

  public Interpolator getScaleInterpolator() {
    return mScaleInterpolator;
  }

  public Interpolator getXInterpolator() {
    return mXInterpolator;
  }

  public Interpolator getYInterpolator() {
    return mYInterpolator;
  }

  public List<FilterInterpolator> getFilterInterpolators() {
    return mFilternterpolators;
  }

  public float getDurationSeconds() {
    return mDurationSeconds;
  }

  public float getStartPauseSeconds() {
    return mStartPauseSeconds;
  }

  public float getEndPauseSeconds() {
    return mEndPauseSeconds;
  }

  // TODO(clocksmith): should be getClassNameOrNull in utility class.
  private String getName(Object object) {
    return object == null ? "null" : object.getClass().getSimpleName();
  }

  public static class Builder {
    private Interpolator mScaleInterpolator;
    private Interpolator mXInterpolator;
    private Interpolator mYInterpolator;
    private List<FilterInterpolator> mFilterInterpolators = Lists.newArrayList();
    private float mDurationSeconds = Constants.DEFAULT_DURATION_SECONDS;
    private float mStartPauseSeconds = Constants.DEFAULT_START_PAUSE_SECONDS;
    private float mEndPauseSeconds = Constants.DEFAULT_END_PAUSE_SECONDS;

    public Builder withScaleInterpolator(Interpolator interpolator) {
      mScaleInterpolator = interpolator;
      return this;
    }

    public Builder withXInterpolator(Interpolator interpolator) {
      mXInterpolator = interpolator;
      return this;
    }

    public Builder withYInterpolator(Interpolator interpolator) {
      mYInterpolator = interpolator;
      return this;
    }

    public Builder withTranslateInterpolator(TranslateInterpolatorProvider interpolatorProvider) {
      return this
          .withXInterpolator(interpolatorProvider.getXInterpolator())
          .withYInterpolator(interpolatorProvider.getYInterpolator());
    }

    public Builder withScaleAndTranslateInterpolatorProvider(
        ScaleAndTranslateInterpolatorProvider interpolatorProvider) {
      return this
          .withScaleInterpolator(interpolatorProvider.getScaleInterpolator())
          .withXInterpolator(interpolatorProvider.getXInterpolator())
          .withYInterpolator(interpolatorProvider.getYInterpolator());
    }

    public Builder withFilterInterpolator(FilterInterpolator filterInterpolator) {
      mFilterInterpolators.add(filterInterpolator);
      return this;
    }

    public Builder withDurationSeconds(float durationSeconds) {
      mDurationSeconds = durationSeconds;
      return this;
    }

    public Builder withStartPauseSeconds(float startPauseSeconds) {
      mStartPauseSeconds = startPauseSeconds;
      return this;
    }

    public Builder withEndPauseSeconds(float endPauseSeconds) {
      mEndPauseSeconds = endPauseSeconds;
      return this;
    }

    public EffectStep build() {
      // Make sure we don't have any null transformation interpolators.
      if (mScaleInterpolator == null) {
        this.withScaleInterpolator(new NoScaleInterpolator());
      }
      NoTranslateInterpolatorProvider noTranslateInterpolatorProvider = new NoTranslateInterpolatorProvider();
      if (mXInterpolator == null) {
        noTranslateInterpolatorProvider.getXInterpolator();
      }
      if (mYInterpolator == null) {
        noTranslateInterpolatorProvider.getYInterpolator();
      }

      // If any filter interpolators do not have internal interpolators, match them with scale.
      for (FilterInterpolator filterInterpolator : mFilterInterpolators) {
        if (!filterInterpolator.hasInterpoolator()) {
          filterInterpolator.setInterpolator(mScaleInterpolator);
        }
      }

      return new EffectStep(
          mScaleInterpolator,
          mXInterpolator,
          mYInterpolator,
          mFilterInterpolators,
          mDurationSeconds,
          mStartPauseSeconds,
          mEndPauseSeconds);
    }
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(
        mHotspot,
        getName(mScaleInterpolator),
        getName(mXInterpolator),
        getName(mYInterpolator),
        mDurationSeconds,
        mStartPauseSeconds,
        mEndPauseSeconds);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }

    if (getClass() != obj.getClass()) {
      return false;
    }

    final EffectStep other = (EffectStep) obj;
    return Objects.equal(mHotspot, other.getHotspot())
        && Objects.equal(getName(mScaleInterpolator), getName(other.getScaleInterpolator()))
        && Objects.equal(getName(mXInterpolator), getName(other.getXInterpolator()))
        && Objects.equal(getName(mYInterpolator), getName(other.getYInterpolator()))
        && Objects.equal(mDurationSeconds, other.getDurationSeconds())
        && Objects.equal(mStartPauseSeconds, other.getStartPauseSeconds())
        && Objects.equal(mEndPauseSeconds, other.getEndPauseSeconds());
  }
}
