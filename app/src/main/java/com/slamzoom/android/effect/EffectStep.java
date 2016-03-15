package com.slamzoom.android.effect;

import android.graphics.Rect;

import com.google.common.base.Objects;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.interpolate.base.BaseInterpolator;
import com.slamzoom.android.interpolate.scale.AbstractScaleInterpolator;

/**
 * Created by clocksmith on 2/21/16.
 */
public class EffectStep {
  private Rect mHotspot;

  private AbstractScaleInterpolator mScaleInterpolator;
  private AbstractMultiOutputInterpolator mTranslateInterpolator;
  private float mDurationSeconds;
  private float mStartPauseSeconds;
  private float mEndPauseSeconds;

  public static Builder newBuilder() {
    return new Builder();
  }

  public EffectStep(
      AbstractScaleInterpolator scaleInterpolator,
      AbstractMultiOutputInterpolator translateInterpolator,
      float durationSeconds,
      float startPauseSeconds,
      float endPauseSeconds) {
    mScaleInterpolator = scaleInterpolator;
    mTranslateInterpolator = translateInterpolator;
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

  public AbstractScaleInterpolator getScaleInterpolator() {
    return mScaleInterpolator;
  }

  public AbstractMultiOutputInterpolator getTranslateInterpolator() {
    return mTranslateInterpolator;
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

  @Override
  public int hashCode() {
    return Objects.hashCode(
        mHotspot,
        getName(mScaleInterpolator),
        getName(mTranslateInterpolator),
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
        && Objects.equal(getName(mTranslateInterpolator), getName(other.getTranslateInterpolator()))
        && Objects.equal(mDurationSeconds, other.getDurationSeconds())
        && Objects.equal(mStartPauseSeconds, other.getStartPauseSeconds())
        && Objects.equal(mEndPauseSeconds, other.getEndPauseSeconds());
  }

  private String getName(BaseInterpolator interpolator) {
    return interpolator == null ? "null" : interpolator.getClass().getSimpleName();
  }

  public static class Builder {
    private AbstractScaleInterpolator mScaleInterpolator;
    private AbstractMultiOutputInterpolator mTranslateInterpolator;
    private float mDurationSeconds = Constants.DEFAULT_DURATION_SECONDS;
    private float mStartPauseSeconds = Constants.DEFAULT_START_PAUSE_SECONDS;
    private float mEndPauseSeconds = Constants.DEFAULT_END_PAUSE_SECONDS;

    public Builder withScaleInterpolator(AbstractScaleInterpolator interpolator) {
      mScaleInterpolator = interpolator;
      return this;
    }

    public Builder withTranslateInterpolator(AbstractMultiOutputInterpolator interpolator) {
      mTranslateInterpolator = interpolator;
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
      return new EffectStep(
          mScaleInterpolator,
          mTranslateInterpolator,
          mDurationSeconds,
          mStartPauseSeconds,
          mEndPauseSeconds);
    }
  }
}
