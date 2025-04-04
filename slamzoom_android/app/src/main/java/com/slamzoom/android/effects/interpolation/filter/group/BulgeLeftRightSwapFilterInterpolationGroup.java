package com.slamzoom.android.effects.interpolation.filter.group;

import android.graphics.PointF;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.BulgeInAtHotspotFilterInterpolator;
import com.slamzoom.android.interpolators.LinearInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 6/7/16.
 */
public class BulgeLeftRightSwapFilterInterpolationGroup implements FilterInterpolatorsProvider {
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
          new LinearInterpolator(getRelativeHotspot().left, getRelativeHotspot().right)
              .getInterpolation(getInterpolationValue()),
          getRelativeHotspot().centerY());
    }
  }

  private static class RightToLeftBulgeInterpolator extends BulgeInAtHotspotFilterInterpolator {
    @Override
    public PointF getCenter() {
      return new PointF(
          new LinearInterpolator(getRelativeHotspot().right, getRelativeHotspot().left)
              .getInterpolation(getInterpolationValue()),
          getRelativeHotspot().centerY());
    }
  }
}
