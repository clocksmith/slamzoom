package com.slamzoom.android.effects.interpolation.filter.group;

import android.graphics.PointF;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.BulgeInAtHotspotFilterInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 5/16/16.
 */
public class BulgeFaceFilterInterpolatorsProvider implements FilterInterpolatorsProvider {
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
      return mCenterCalculator.getHotspotPoint(0.25f, 0.25f);
    }
  }

  private static class RightEyeFilterInterpolator extends BulgeInAtHotspotFilterInterpolator {
    @Override
    public PointF getCenter() {
      return mCenterCalculator.getHotspotPoint(0.75f, 0.25f);
    }
  }

  private static class MouthFilterInterpolator extends BulgeInAtHotspotFilterInterpolator {
    @Override
    public PointF getCenter() {
      return mCenterCalculator.getHotspotPoint(0.5f, 0.75f);
    }
  }
}