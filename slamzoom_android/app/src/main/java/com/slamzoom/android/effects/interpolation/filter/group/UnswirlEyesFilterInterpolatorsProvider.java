package com.slamzoom.android.effects.interpolation.filter.group;

import android.graphics.PointF;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlAtHotspotFilterInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 6/2/16.
 */
public class UnswirlEyesFilterInterpolatorsProvider implements FilterInterpolatorsProvider {
  private static final float BASE_EYE_RADIUS = 0.2f;

  @Override
  public List<FilterInterpolator> getFilterInterpolators() {
    return ImmutableList.<FilterInterpolator>of(
        new LeftFilterInterpolator(),
        new RightFilterInterpolator()
    );
  }

  private static class LeftFilterInterpolator extends UnswirlAtHotspotFilterInterpolator {
    public LeftFilterInterpolator() {
      mRadiusCalculator.setBaseValue(BASE_EYE_RADIUS);
    }

    @Override
    public float getRadius() {
      return mRadiusCalculator.getValueFromMinHotspotDimen();
    }

    @Override
    public float getRotation() {
      return -2 * super.getRotation();
    }

    @Override
    public PointF getCenter() {
      return mCenterCalculator.getHotspotPoint(0.25f, 0.5f);
    }
  }

  private static class RightFilterInterpolator extends UnswirlAtHotspotFilterInterpolator {
    public RightFilterInterpolator() {
      mRadiusCalculator.setBaseValue(BASE_EYE_RADIUS);
    }

    @Override
    public float getRadius() {
      return mRadiusCalculator.getValueFromMinHotspotDimen();
    }

    @Override
    public float getRotation() {
      return 2 * super.getRotation();
    }

    @Override
    public PointF getCenter() {
      return mCenterCalculator.getHotspotPoint(0.75f, 0.5f);
    }
  }
}
