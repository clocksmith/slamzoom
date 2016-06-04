package com.slamzoom.android.effects.interpolation.filter.group;

import android.graphics.PointF;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.BulgeWeightedFilterInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 4/22/16.
 */
public class BulgeEyesFilterInterpolatorGroup implements FilterInterpolatorGroup {
  private static final float RADIUS = 0.5f;
  private static final float SCALE = 0.5f;

  @Override
  public List<FilterInterpolator> getFilterInterpolators() {
    return ImmutableList.<FilterInterpolator>of(
        new LeftEyeFilterInterpolator(),
        new RightEyeFilterInterpolator()
    );
  }

  private static class LeftEyeFilterInterpolator extends BulgeWeightedFilterInterpolator {
    @Override
    public PointF getCenter() {
      return new PointF((
          getNormalizedHotspot().left + getNormalizedHotspot().centerX()) / 2,
          getNormalizedHotspot().centerY());
    }
  }

  private static class RightEyeFilterInterpolator extends BulgeWeightedFilterInterpolator {
    @Override
    public PointF getCenter() {
      return new PointF((
          getNormalizedHotspot().right + getNormalizedHotspot().centerX()) / 2,
          getNormalizedHotspot().centerY());
    }
  }
}
