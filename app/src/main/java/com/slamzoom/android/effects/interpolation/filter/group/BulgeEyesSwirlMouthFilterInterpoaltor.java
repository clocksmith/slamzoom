package com.slamzoom.android.effects.interpolation.filter.group;

import android.graphics.PointF;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.effects.interpolation.filter.base.BaseSwirlFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.BulgeInAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlAtHotspotOnHotspotFilterInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;

import java.util.List;

/**
 * Created by clocksmith on 6/4/16.
 */
public class BulgeEyesSwirlMouthFilterInterpoaltor implements FilterInterpolatorGroup {
  private static final float MOUTH_RADIUS_MULTIPLIER = 0.2f;
  private static final float MOUTH_ROTATION_MULTIPLIER = 0.3f;
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


  private static class MouthFilterInterpolator extends BaseSwirlFilterInterpolator {
    private Interpolator mMouthRotationIterpolator = new Interpolator() {
      @Override
      protected float getRangePercent(float t) {
        return (float) (t * Math.cos(6 * Math.PI * t));
      }
    };

    @Override
    public float getRadius() {
      return MOUTH_RADIUS_MULTIPLIER * getMinDimenOfHotspot();
    }

    @Override
    public float getRotation() {
      return MOUTH_ROTATION_MULTIPLIER * mMouthRotationIterpolator.getInterpolation(getInterpolationValueCompliment());
    }

    @Override
    public PointF getCenter() {
      return new PointF(
          getNormalizedHotspot().centerX(),
          (getNormalizedHotspot().bottom + getNormalizedHotspot().centerY()) / 2);
    }

  }
}
