package com.slamzoom.android.effect;

import android.graphics.Rect;

import com.google.common.base.Objects;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.interpolate.combo.ScaleAndTranslateInterpolatorProvider;
import com.slamzoom.android.interpolate.scale.OneScaleInterpolatorProvider;
import com.slamzoom.android.interpolate.scale.ScaleInterpolatorProvider;
import com.slamzoom.android.interpolate.translate.TranslateInterpolatorProvider;
import com.slamzoom.android.interpolate.translate.ZeroTranslateInterpolatorProvider;

/**
 * Created by clocksmith on 2/21/16.
 */
public class EffectStep {
  private Rect mHotspot;

  private ScaleInterpolatorProvider mScaleInterpolatorProvider;
  private TranslateInterpolatorProvider mTranslateInterpolatorProvider;
  private float mDurationSeconds;
  private float mStartPauseSeconds;
  private float mEndPauseSeconds;

  public static Builder newBuilder() {
    return new Builder();
  }

  public EffectStep(
      ScaleInterpolatorProvider scaleInterpolatorProvider,
      TranslateInterpolatorProvider translateInterpolatorProvider,
      float durationSeconds,
      float startPauseSeconds,
      float endPauseSeconds) {
    mScaleInterpolatorProvider = scaleInterpolatorProvider;
    mTranslateInterpolatorProvider = translateInterpolatorProvider;
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

  public ScaleInterpolatorProvider getScaleInterpolatorProvider() {
    return mScaleInterpolatorProvider;
  }

  public TranslateInterpolatorProvider getTranslateInterpolatorProvider() {
    return mTranslateInterpolatorProvider;
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
        getName(mScaleInterpolatorProvider),
        getName(mTranslateInterpolatorProvider),
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
        && Objects.equal(getName(mScaleInterpolatorProvider), getName(other.getScaleInterpolatorProvider()))
        && Objects.equal(getName(mTranslateInterpolatorProvider), getName(other.getTranslateInterpolatorProvider()))
        && Objects.equal(mDurationSeconds, other.getDurationSeconds())
        && Objects.equal(mStartPauseSeconds, other.getStartPauseSeconds())
        && Objects.equal(mEndPauseSeconds, other.getEndPauseSeconds());
  }

  // TODO(clocksmith): should be getClassNameOrNull in utility class.
  private String getName(Object object) {
    return object == null ? "null" : object.getClass().getSimpleName();
  }

  public static class Builder {
    private ScaleInterpolatorProvider mScaleInterpolatorProvider;
    private TranslateInterpolatorProvider mTranslateInterpolatorProvider;
    private float mDurationSeconds = Constants.DEFAULT_DURATION_SECONDS;
    private float mStartPauseSeconds = Constants.DEFAULT_START_PAUSE_SECONDS;
    private float mEndPauseSeconds = Constants.DEFAULT_END_PAUSE_SECONDS;
    private int mNumTilesInRow = 1;

    public Builder withScaleInterpolatorProvider(ScaleInterpolatorProvider interpolatorProvider) {
      mScaleInterpolatorProvider = interpolatorProvider;
      return this;
    }

    public Builder withTranslateInterpolatorProvider(TranslateInterpolatorProvider interpolatorProvider) {
      mTranslateInterpolatorProvider = interpolatorProvider;
      return this;
    }

    public Builder withScaleAndTranslateInterpolatorProvider(
        ScaleAndTranslateInterpolatorProvider interpolatorsProvider) {
      mScaleInterpolatorProvider = interpolatorsProvider;
      mTranslateInterpolatorProvider = interpolatorsProvider;
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
          mScaleInterpolatorProvider == null ?
              new OneScaleInterpolatorProvider() :
              mScaleInterpolatorProvider,
          mTranslateInterpolatorProvider == null ?
              new ZeroTranslateInterpolatorProvider() :
              mTranslateInterpolatorProvider,
          mDurationSeconds,
          mStartPauseSeconds,
          mEndPauseSeconds);
    }
  }
}
