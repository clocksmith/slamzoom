package com.slamzoom.android.effects.interpolation.filter.group;

import android.graphics.PointF;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.effects.interpolation.filter.BaseSwirlFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.BulgeInAtHotspotFilterInterpolator;
import com.slamzoom.android.interpolators.Interpolator;

import java.util.List;

/**
 * Created by clocksmith on 6/15/16.
 */
public class SwirlEyesTwistyMouthFilterInterpolator implements FilterInterpolatorGroup {
  private static final float BASE_EYE_RADIUS = 0.2f;
  private static final float BASE_EYE_ROTATION = 1.5f;
  private static final float BASE_MOUTH_RADIUS = 0.2f;
  private static final float BASE_MOUTH_ROTATION = 0.4f;

  @Override
  public List<FilterInterpolator> getFilterInterpolators() {
    return ImmutableList.<FilterInterpolator>of(
        new LeftEyeFilterInterpolator(),
        new RightEyeFilterInterpolator(),
        new MouthFilterInterpolator()
    );
  }

  private static class SwirlEyeFilterInterpolator extends BaseSwirlFilterInterpolator {
    public SwirlEyeFilterInterpolator() {
      mRadiusCalculator.setBaseValue(BASE_EYE_RADIUS);
      mRotationCalculator.setBaseValue(BASE_EYE_ROTATION);
    }

    @Override
    public float getRadius() {
      return mRadiusCalculator.getValueFromInterpolation();
    }
  }

  private static class LeftEyeFilterInterpolator extends SwirlEyeFilterInterpolator {
    @Override
    public PointF getCenter() {
      return mCenterCalculator.getHotspotPoint(0.25f, 0.3f);
    }
  }

  private static class RightEyeFilterInterpolator extends SwirlEyeFilterInterpolator {
    @Override
    public PointF getCenter() {
      return mCenterCalculator.getHotspotPoint(0.75f, 0.3f);
    }

  }

  private static class MouthFilterInterpolator extends BaseSwirlFilterInterpolator {
    private Interpolator mMouthRotationIterpolator = new Interpolator() {
      @Override
      protected float getRangePercent(float t) {
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
      return mCenterCalculator.getHotspotPoint(0.5f, 0.72f);
    }
  }
}

