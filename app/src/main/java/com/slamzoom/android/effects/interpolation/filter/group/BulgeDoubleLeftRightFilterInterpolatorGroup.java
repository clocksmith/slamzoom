package com.slamzoom.android.effects.interpolation.filter.group;

import android.graphics.PointF;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.BulgeInAtHotspotFilterInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 4/22/16.
 */
public class BulgeDoubleLeftRightFilterInterpolatorGroup implements FilterInterpolatorGroup {
  private static final float RADIUS = 0.5f;
  private static final float SCALE = 0.5f;

  @Override
  public List<FilterInterpolator> getFilterInterpolators() {
    return ImmutableList.<FilterInterpolator>of(
        new LeftEyeFilterInterpolator(),
        new RightEyeFilterInterpolator()
    );
  }

  private static class LeftEyeFilterInterpolator extends BulgeInAtHotspotFilterInterpolator {
    @Override
    public PointF getCenter() {
      return new PointF((
          getNormalizedHotspot().left + getNormalizedHotspot().centerX()) / 2,
          getNormalizedHotspot().centerY());
    }
  }

  private static class RightEyeFilterInterpolator extends BulgeInAtHotspotFilterInterpolator {
    @Override
    public PointF getCenter() {
      return new PointF((
          getNormalizedHotspot().right + getNormalizedHotspot().centerX()) / 2,
          getNormalizedHotspot().centerY());
    }
  }
}
