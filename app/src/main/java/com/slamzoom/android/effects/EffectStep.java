package com.slamzoom.android.effects;

import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.transform.TransformInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.TranslateInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.NoTranslateInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.ZeroInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 2/21/16.
 */
public class EffectStep {
  private static final String TAG = EffectStep.class.getSimpleName();

  private RectF mHotspot;
  private String mEndText;

  private Interpolator mScaleInterpolator;
  private Interpolator mXInterpolator;
  private Interpolator mYInterpolator;
  private ImmutableList<FilterInterpolator> mFilterInterpolators;
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
    mFilterInterpolators = ImmutableList.copyOf(filterInterpolators);
    mDurationSeconds = durationSeconds;
    mStartPauseSeconds = startPauseSeconds;
    mEndPauseSeconds = endPauseSeconds;
  }

  public RectF getHotspot() {
    return mHotspot;
  }

  public void setHotspot(RectF hotspot) {
    mHotspot = hotspot;
  }

  public String getEndText() {
    return mEndText;
  }

  public void setEndText(String endText) {
    mEndText = endText;
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

  public ImmutableList<FilterInterpolator> getFilterInterpolators() {
    return mFilterInterpolators;
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

    public Builder withTransformInterpolatorProvider(
        TransformInterpolatorProvider interpolatorProvider) {
      return this
          .withScaleInterpolator(interpolatorProvider.getScaleInterpolator())
          .withXInterpolator(interpolatorProvider.getXInterpolator())
          .withYInterpolator(interpolatorProvider.getYInterpolator());
    }

    public Builder withFilterInterpolator(FilterInterpolator filterInterpolator) {
      mFilterInterpolators.add(filterInterpolator);
      return this;
    }

    public Builder withFilterInterpolators(List<FilterInterpolator> filterInterpolators) {
      mFilterInterpolators.addAll(filterInterpolators);
      return this;
    }

    public Builder withEffectConfig(EffectConfig interpolatorProvider) {
      return this
//          .withStartPauseSeconds(interpolatorProvider.getStartPauseSeconds())
//          .withDurationSeconds(interpolatorProvider.getDurationSeconds())
//          .withEndPauseSeconds(interpolatorProvider.getEndPauseSeconds())
          .withScaleInterpolator(interpolatorProvider.getScaleInterpolator())
          .withXInterpolator(interpolatorProvider.getXInterpolator())
          .withYInterpolator(interpolatorProvider.getYInterpolator())
          .withFilterInterpolators(interpolatorProvider.getFilterInterpolators());
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
        this.withScaleInterpolator(new ZeroInterpolator());
      }
      if (mXInterpolator == null || mYInterpolator == null) {
        NoTranslateInterpolatorProvider noTranslateInterpolatorProvider = new NoTranslateInterpolatorProvider();
        if (mXInterpolator == null) {
          this.withXInterpolator(noTranslateInterpolatorProvider.getXInterpolator());
        }
        if (mYInterpolator == null) {
          this.withYInterpolator(noTranslateInterpolatorProvider.getYInterpolator());
        }
      }

      // If any filter interpolators do not have internal interpolators, match them with scale.
      for (FilterInterpolator filterInterpolator : mFilterInterpolators) {
        if (!filterInterpolator.hasInterpolator()) {
          try {
            Interpolator interpolator = mScaleInterpolator.clone();
            interpolator.setRange(0, 1);
            filterInterpolator.setInterpolator(interpolator);
          } catch (CloneNotSupportedException e) {
            Log.e(TAG, "Could not clone scale interpolator", e);
          }
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
        && Objects.equal(mEndText, other.getEndText())
        && Objects.equal(getName(mScaleInterpolator), getName(other.getScaleInterpolator()))
        && Objects.equal(getName(mXInterpolator), getName(other.getXInterpolator()))
        && Objects.equal(getName(mYInterpolator), getName(other.getYInterpolator()))
        && Objects.equal(mDurationSeconds, other.getDurationSeconds())
        && Objects.equal(mStartPauseSeconds, other.getStartPauseSeconds())
        && Objects.equal(mEndPauseSeconds, other.getEndPauseSeconds());
  }
}
