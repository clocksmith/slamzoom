package com.slamzoom.android.effects.interpolation.filter.calculators;

import android.graphics.PointF;

import com.slamzoom.android.common.data.RectFUtils;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;

/**
 * Created by clocksmith on 6/4/16.
 */
public class CenterCalculator extends BaseCalculator {
  private PointF mBaseValue = RectFUtils.NORMAL_CENTER_POINT;

  public CenterCalculator(FilterInterpolator filterInterpolator) {
    super(filterInterpolator);
  }

  public PointF getBaseValue() {
    return mBaseValue;
  }

  public PointF getHotspotCenter() {
    return new PointF(getRelativeHotspot().centerX(), getRelativeHotspot().centerY());
  }

  public PointF getHotspotPoint(float left, float top) {
    return new PointF(
        getRelativeHotspot().left + getRelativeHotspot().width() * left,
        getRelativeHotspot().top + getRelativeHotspot().height() * top);
  }
}
