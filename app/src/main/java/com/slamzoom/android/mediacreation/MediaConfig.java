package com.slamzoom.android.mediacreation;

import android.graphics.Rect;
import android.graphics.RectF;

import com.google.common.base.Objects;
import com.slamzoom.android.effects.EffectTemplate;

/**
 * Created by clocksmith on 4/14/16.
 */
public class MediaConfig {
  public RectF hotspot;
  public BitmapSet bitmapSet;
  public EffectTemplate effectTemplate;
  public String endText;
  public int size;
  public int fps;

  public static Builder newBuilder() {
    return new Builder();
  }

  public MediaConfig(
      RectF hotspot, BitmapSet bitmapSet, EffectTemplate effectTemplate, String endText, int fps, int size) {
    this.hotspot = hotspot;
    this.bitmapSet = bitmapSet;
    this.effectTemplate = effectTemplate;
    this.endText = endText;
    this.size = size;
    this.fps = fps;
  }

  public static class Builder {
    private RectF mHotspot;
    private BitmapSet mBitmapSet;
    private EffectTemplate mEffectTemplate;
    private String mEndText;
    private int mFps;
    private int mSize;

    public Builder() {}

    public Builder withHotspot(RectF hotspot) {
      mHotspot = hotspot;
      return this;
    }

    public Builder withBitmapSet(BitmapSet bitmapSet) {
      mBitmapSet = bitmapSet;
      return this;
    }

    public Builder withEffectTemplate(EffectTemplate effectTemplate) {
      mEffectTemplate = effectTemplate;
      return this;
    }

    public Builder withEndText(String endText) {
      mEndText = endText;
      return this;
    }

    public Builder withFps(int fps) {
      mFps = fps;
      return this;
    }

    public Builder withSize(int size) {
      mSize = size;
      return this;
    }

    public MediaConfig build() {
      return new MediaConfig(mHotspot, mBitmapSet, mEffectTemplate, mEndText, mFps, mSize);
    }
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(hotspot, hotspot, effectTemplate, endText, fps, size);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final MediaConfig other = (MediaConfig) obj;
    return Objects.equal(hotspot, other.hotspot) &&
        Objects.equal(hotspot, other.hotspot) &&
        Objects.equal(effectTemplate, other.effectTemplate) &&
        Objects.equal(endText, other.endText) &&
        fps == other.fps &&
        size == other.size;
  }
}
