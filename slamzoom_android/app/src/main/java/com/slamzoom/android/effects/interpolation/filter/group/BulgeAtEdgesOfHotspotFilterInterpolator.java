package com.slamzoom.android.effects.interpolation.filter.group;

import android.graphics.PointF;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.BulgeInAtHotspotFilterInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 6/7/16.
 */
public class BulgeAtEdgesOfHotspotFilterInterpolator implements FilterInterpolatorsProvider {
  private static final float BASE_RADIUS = 0.99f;

  @Override
  public List<FilterInterpolator> getFilterInterpolators() {
    return ImmutableList.<FilterInterpolator>of(
        new BaseBulge() {
          @Override
          public PointF getCenter() {
            return mCenterCalculator.getHotspotPoint(0.5f, 0);
          }
        },
        new BaseBulge() {
          @Override
          public PointF getCenter() {
            return mCenterCalculator.getHotspotPoint(1, 0.5f);
          }
        },
        new BaseBulge() {
          @Override
          public PointF getCenter() {
            return mCenterCalculator.getHotspotPoint(0.5f, 1);
          }
        },
        new BaseBulge() {
          @Override
          public PointF getCenter() {
            return mCenterCalculator.getHotspotPoint(0, 0.5f);
          }
        }
    );
  }

  private static class BaseBulge extends BulgeInAtHotspotFilterInterpolator {
    public BaseBulge() {
      mRadiusCalculator.setBaseValue(BASE_RADIUS);
    }
  }

}
