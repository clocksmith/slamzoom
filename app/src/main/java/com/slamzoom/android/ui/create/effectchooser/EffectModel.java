package com.slamzoom.android.ui.create.effectchooser;

import com.google.common.base.Objects;
import com.slamzoom.android.effects.EffectTemplate;

/**
 * Created by clocksmith on 3/25/16.
 *
 * TODO(clocksmith): Add instance specific config stuff. Right now its just a useless wrapper.
 */
public class EffectModel {
  private EffectTemplate mEffectTemplate;

  public EffectModel(EffectTemplate effectTemplate) {
    mEffectTemplate = effectTemplate;
  }

  public EffectTemplate getEffectTemplate() {
    return mEffectTemplate;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(mEffectTemplate);
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
    return Objects.equal(mEffectTemplate, other.getEffectTemplate());
  }
}
