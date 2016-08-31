package com.slamzoom.android.effects.interpolation.filter.group;

import android.graphics.PointF;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.ShrinkInAtHotspotFilterInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 6/7/16.
 */
public class DeflateFaceFilterInterpolatorsProvider implements FilterInterpolatorsProvider {
  @Override
  public List<FilterInterpolator> getFilterInterpolators() {
    return ImmutableList.<FilterInterpolator>of(
        new Shrink(),
        new Shrink()
    );
  }

  private static class Shrink extends ShrinkInAtHotspotFilterInterpolator {
    private static final float BASE_RADIUS = 0.49f;
    private static final float BASE_SCALE = -0.5f;

    public Shrink() {
      mRadiusCalculator.setBaseValue(BASE_RADIUS);
      mScaleCalculator.setBaseValue(BASE_SCALE);
    }

    @Override
    public PointF getCenter() {
      return mCenterCalculator.getHotspotCenter();
    }
  }
}
