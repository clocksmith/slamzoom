package com.slamzoom.android.effects.interpolation.filter.group;

import android.graphics.PointF;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.BulgeInAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.ShrinkInAtHotspotFilterInterpolator;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.LinearInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 5/16/16.
 */
public class ShrinkFaceFilterInterpolatorGroup implements FilterInterpolatorGroup {
  private static final float BASE_RADIUS = 0.25f;

  @Override
  public List<FilterInterpolator> getFilterInterpolators() {
    return ImmutableList.<FilterInterpolator>of(
        new LeftEyeFilterInterpolator(),
        new RightEyeFilterInterpolator(),
        new MouthFilterInterpolator()
    );
  }

  private static class LeftEyeFilterInterpolator extends ShrinkInAtHotspotFilterInterpolator {
    public LeftEyeFilterInterpolator() {
      super();
      mRadiusCalculator.setBaseValue(BASE_RADIUS);
    }

    @Override
    public PointF getCenter() {
      return mCenterCalculator.getHotspotPoint(0.25f, 0.25f);
    }
  }

  private static class RightEyeFilterInterpolator extends ShrinkInAtHotspotFilterInterpolator {
    public RightEyeFilterInterpolator() {
      super();
      mRadiusCalculator.setBaseValue(BASE_RADIUS);
    }

    @Override
    public PointF getCenter() {
      return mCenterCalculator.getHotspotPoint(0.75f, 0.25f);
    }
  }

  private static class MouthFilterInterpolator extends ShrinkInAtHotspotFilterInterpolator {
    public MouthFilterInterpolator() {
      super();
      mRadiusCalculator.setBaseValue(BASE_RADIUS);
    }

    @Override
    public PointF getCenter() {
      return mCenterCalculator.getHotspotPoint(0.5f, 0.75f);
    }
  }
}