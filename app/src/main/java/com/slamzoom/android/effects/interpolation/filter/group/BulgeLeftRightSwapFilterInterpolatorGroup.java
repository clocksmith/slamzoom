package com.slamzoom.android.effects.interpolation.filter.group;

import android.graphics.PointF;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.BulgeInAtHotspotFilterInterpolator;
import com.slamzoom.android.interpolators.base.LinearInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 5/16/16.
 */
public class BulgeLeftRightSwapFilterInterpolatorGroup implements FilterInterpolatorGroup {
  @Override
  public List<FilterInterpolator> getFilterInterpolators() {
    return ImmutableList.<FilterInterpolator>of(
        new LeftToRightBulgeInterpolator(),
        new RightToLeftBulgeInterpolator()
    );
  }

  private static class LeftToRightBulgeInterpolator extends BulgeInAtHotspotFilterInterpolator {
    @Override
    public PointF getCenter() {
      return new PointF(
          new LinearInterpolator(
              getNormalizedHotspot().left,
              getNormalizedHotspot().right).getInterpolation(getInterpolationValue()),
          getNormalizedHotspot().centerY());
    }
  }

  private static class RightToLeftBulgeInterpolator extends BulgeInAtHotspotFilterInterpolator {
    @Override
    public PointF getCenter() {
      return new PointF(
          new LinearInterpolator(
              getNormalizedHotspot().right,
              getNormalizedHotspot().left).getInterpolation(getInterpolationValue()),
          getNormalizedHotspot().centerY());
    }
  }
}