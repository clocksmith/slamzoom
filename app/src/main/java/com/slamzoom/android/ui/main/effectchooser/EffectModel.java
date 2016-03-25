package com.slamzoom.android.ui.main.effectchooser;

import android.graphics.Rect;

import com.slamzoom.android.effects.EffectTemplate;

/**
 * Created by clocksmith on 3/25/16.
 *
 * TODO(clocksmith): Add instance specific config stuff
 */
public class EffectModel {
  private EffectTemplate mEffectTemplate;
  private byte[] mGifPreviewBytes;

  public EffectModel(EffectTemplate effectTemplate) {
    mEffectTemplate = effectTemplate;
  }

  public EffectTemplate getEffectTemplate() {
    return mEffectTemplate;
  }

  public void setEffectTemplate(EffectTemplate effectTemplate) {
    mEffectTemplate = effectTemplate;
  }

  public byte[] getGifPreviewBytes() {
    return mGifPreviewBytes;
  }

  public void setGifPreviewBytes(byte[] gifPreviewBytes) {
    mGifPreviewBytes = gifPreviewBytes;
  }
}
