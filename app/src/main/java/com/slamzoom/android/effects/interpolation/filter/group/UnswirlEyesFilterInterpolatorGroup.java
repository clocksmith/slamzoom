package com.slamzoom.android.effects.interpolation.filter.group;

import android.graphics.PointF;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlAtHotspotFilterInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 6/2/16.
 */
public class UnswirlEyesFilterInterpolatorGroup implements FilterInterpolatorGroup {
  private static final float EYE_RADIUS_MULTIPLIER = 0.3f;

  @Override
  public List<FilterInterpolator> getFilterInterpolators() {
    return ImmutableList.<FilterInterpolator>of(
        new LeftFilterInterpolator(),
        new RightFilterInterpolator()
    );
  }

  private static class LeftFilterInterpolator extends UnswirlAtHotspotFilterInterpolator {
    @Override
    public float getRadius() {
      return EYE_RADIUS_MULTIPLIER * getMinDimenOfHotspot();
    }

    @Override
    public PointF getCenter() {
      return new PointF(
          (getNormalizedHotspot().left + getNormalizedHotspot().centerX()) / 2,
          getNormalizedHotspot().centerY());
    }
  }

  private static class RightFilterInterpolator extends UnswirlAtHotspotFilterInterpolator {
    @Override
    public float getRadius() {
      return EYE_RADIUS_MULTIPLIER * getMinDimenOfHotspot();
    }

//    @Override
//    public float getRadius() {
//      return 0.1f;
//    }

    @Override
    public PointF getCenter() {
      return new PointF(
          (getNormalizedHotspot().right + getNormalizedHotspot().centerX()) / 2,
          getNormalizedHotspot().centerY());
    }
  }
}
