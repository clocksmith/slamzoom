package com.slamzoom.android.effects.interpolation.filter.calculators;

import android.graphics.PointF;
import android.graphics.RectF;

import com.slamzoom.android.common.data.RectFUtils;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;

/**
 * Created by clocksmith on 6/4/16.
 */
public class BaseCalculator {
  private FilterInterpolator mFilterInterpolator;

  public BaseCalculator(FilterInterpolator filterInterpolator) {
    mFilterInterpolator = filterInterpolator;
  }

  public float getInterpolationValue() {
    return mFilterInterpolator.getInterpolationValue();
  }

  public RectF getNormalizedHotspot() {
    return mFilterInterpolator.getRelativeHotspot();
  }

  protected float getInterpolationValueCompliment() {
    return 1 - getInterpolationValue();
  }

  protected PointF getCenterOfHotspot() {
    return RectFUtils.getCenterPointF(getNormalizedHotspot());
  }

  protected float getMinHotspotDimen() {
    return RectFUtils.getMinDimen(getNormalizedHotspot());
  }
}
