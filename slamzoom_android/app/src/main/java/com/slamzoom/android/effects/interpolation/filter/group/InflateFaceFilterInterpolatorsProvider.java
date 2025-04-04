package com.slamzoom.android.effects.interpolation.filter.group;

import android.graphics.PointF;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.BulgeInAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.ShrinkInAtHotspotFilterInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 5/16/16.
 */
public class InflateFaceFilterInterpolatorsProvider implements FilterInterpolatorsProvider {
  private static final float BASE_RADIUS = 0.49f;

  @Override
  public List<FilterInterpolator> getFilterInterpolators() {
    return ImmutableList.<FilterInterpolator>of(
        new LargeBulgeFI(),
        new SmallerShrinkFI()
    );
  }

  private static class LargeBulgeFI extends BulgeInAtHotspotFilterInterpolator {
    public LargeBulgeFI() {
      mRadiusCalculator.setBaseValue(BASE_RADIUS * 2);
    }

    @Override
    public PointF getCenter() {
      return mCenterCalculator.getHotspotCenter();
    }
  }

  private static class SmallerShrinkFI extends ShrinkInAtHotspotFilterInterpolator {
    private static final float BASE_SCALE = -0.5f;

    public SmallerShrinkFI() {
      mRadiusCalculator.setBaseValue(BASE_RADIUS);
      mScaleCalculator.setBaseValue(BASE_SCALE);
    }

    @Override
    public PointF getCenter() {
      return mCenterCalculator.getHotspotCenter();
    }
  }
}