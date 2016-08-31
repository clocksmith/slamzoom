package com.slamzoom.android.effects.interpolation.filter.group;

import android.graphics.PointF;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.BulgeInAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.ShrinkInAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlAtHotspotFilterInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 6/7/16.
 */
public class SumoBulge2FilterInterpolator implements FilterInterpolatorsProvider {
  private static final float BASE_RADIUS = 0.49f;

  @Override
  public List<FilterInterpolator> getFilterInterpolators() {
    return ImmutableList.<FilterInterpolator>of(
        new BaseBulge() {
          @Override
          public PointF getCenter() {
            return new PointF(0.5f, 0);
          }
        },
        new BaseBulge() {
          @Override
          public PointF getCenter() {
            return new PointF(1, 0.5f);
          }
        },
        new BaseBulge() {
          @Override
          public PointF getCenter() {
            return new PointF(0.5f, 1);
          }
        },
        new BaseBulge() {
          @Override
          public PointF getCenter() {
            return new PointF(0, 0.5f);
          }
        },
        new BaseBulge() {
          @Override
          public PointF getCenter() {
            return new PointF(0.5f, 0.5f);
          }
        },
        new BaseSwirl() {
          @Override
          public PointF getCenter() {
            return mCenterCalculator.getHotspotCenter();
          }

          @Override
          public float getRotation() {
            return mRotationCalculator.getValueFromInterpolation();
          }
        }
    );
  }

  private static class BaseBulge extends BulgeInAtHotspotFilterInterpolator {
    public BaseBulge() {
      mRadiusCalculator.setBaseValue(BASE_RADIUS);
    }
  }

  private static class BaseShrink extends ShrinkInAtHotspotFilterInterpolator {
    public BaseShrink() {
      mRadiusCalculator.setBaseValue(BASE_RADIUS);
      mScaleCalculator.setBaseValue(-0.5f);
    }
  }

  private static class BaseSwirl extends UnswirlAtHotspotFilterInterpolator {
    public BaseSwirl() {
      mRadiusCalculator.setBaseValue(BASE_RADIUS);
      mRotationCalculator.setBaseValue(-0.3f);
    }
  }

}
