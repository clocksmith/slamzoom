package com.slamzoom.android.effects.interpolation.filter.group;

import android.graphics.PointF;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.BulgeInAtHotspotFilterInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 5/16/16.
 */
public class BulgeFaceFilterInterpolatorGroup implements FilterInterpolatorGroup {
    @Override
    public List<FilterInterpolator> getFilterInterpolators() {
      return ImmutableList.<FilterInterpolator>of(
          new LeftEyeFilterInterpolator(),
          new RightEyeFilterInterpolator(),
          new MouthFilterInterpolator()
      );
    }

    private static class LeftEyeFilterInterpolator extends BulgeInAtHotspotFilterInterpolator {
      @Override
      public PointF getCenter() {
        return new PointF(
            (getNormalizedHotspot().left + getNormalizedHotspot().centerX()) / 2,
            (getNormalizedHotspot().top + getNormalizedHotspot().centerY()) / 2);
      }
    }

    private static class RightEyeFilterInterpolator extends BulgeInAtHotspotFilterInterpolator {
      @Override
      public PointF getCenter() {
        return new PointF(
            (getNormalizedHotspot().right + getNormalizedHotspot().centerX()) / 2,
            (getNormalizedHotspot().top + getNormalizedHotspot().centerY()) / 2);
      }
    }

  private static class MouthFilterInterpolator extends BulgeInAtHotspotFilterInterpolator {
    @Override
    public PointF getCenter() {
      return new PointF(
          getNormalizedHotspot().centerX(),
          (getNormalizedHotspot().bottom + getNormalizedHotspot().centerY()) / 2);
    }
  }
}