package com.slamzoom.android.mediacreation.gif;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.google.common.base.Objects;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.ui.create.effectchooser.EffectModel;

/**
 * Created by clocksmith on 4/14/16.
 */
public class GifConfig {
  public Rect hotspot;
  public Bitmap bitmap;
  public EffectTemplate effectTemplate;
  public String endText;
  public int fps;

  public static Builder newBuilder() {
    return new Builder();
  }

  public GifConfig(Rect hotspot, Bitmap bitmap, EffectTemplate effectTemplate, String endText, int fps) {
    this.hotspot = hotspot;
    this.bitmap = bitmap;
    this.effectTemplate = effectTemplate;
    this.endText = endText;
    this.fps = fps;
  }

  public static class Builder {
    private Rect mHotspot;
    private Bitmap mBitmap;
    private EffectTemplate mEffectTemplate;
    private String mEndText;
    private int mFps;

    public Builder() {
    }

    public Builder withHotspot(Rect hotspot) {
      mHotspot = hotspot;
      return this;
    }

    public Builder withBitmap(Bitmap bitmap) {
      mBitmap = bitmap;
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

    public GifConfig build() {
      return new GifConfig(mHotspot, mBitmap, mEffectTemplate, mEndText, mFps);
    }
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(hotspot, bitmap, effectTemplate, endText, fps);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final GifConfig other = (GifConfig) obj;
    return Objects.equal(hotspot, other.hotspot) &&
        Objects.equal(bitmap, other.bitmap) &&
        Objects.equal(effectTemplate, other.effectTemplate) &&
        Objects.equal(endText, other.endText) &&
        fps == other.fps;
  }
}
