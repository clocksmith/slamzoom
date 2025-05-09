package com.slamzoom.android.effects.interpolation.filter.group;

import android.graphics.PointF;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.effects.interpolation.filter.BaseSwirlFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.BulgeInAtHotspotFilterInterpolator;
import com.slamzoom.android.interpolators.Interpolator;

import java.util.List;

/**
 * Created by clocksmith on 6/4/16.
 */
public class BulgeEyesSwirlMouthFilterInterpoaltor implements FilterInterpolatorsProvider {
  private static final float BASE_MOUTH_RADIUS = 0.2f;
  private static final float BASE_MOUTH_ROTATION = 0.3f;

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

  private static class MouthFilterInterpolator extends BaseSwirlFilterInterpolator {
    private Interpolator mMouthRotationIterpolator = new Interpolator() {
      @Override
      public float getValue(float t) {
        return (float) (Math.cos(6 * Math.PI * t));
      }
    };

    public MouthFilterInterpolator() {
      mRadiusCalculator.setBaseValue(BASE_MOUTH_RADIUS);
      mRotationCalculator.setBaseValue(BASE_MOUTH_ROTATION);
    }


    @Override
    public float getRadius() {
      return mRadiusCalculator.getValueFromMinHotspotDimen();
    }

    @Override
    public float getRotation() {
      return mRotationCalculator.getValueFromSubInterpolationOfInterpolationCompliment(mMouthRotationIterpolator);
    }

    @Override
    public PointF getCenter() {
      return mCenterCalculator.getHotspotPoint(0.5f, 0.75f);
    }
  }
}
