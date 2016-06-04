package com.slamzoom.android.effects.interpolation.filter.group;

import android.graphics.PointF;
import android.graphics.RectF;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.BulgeWeightedFilterInterpolator;

import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 5/16/16.
 */
public class BulgeFaceFilterInterpolatorGroup implements FilterInterpolatorGroup {
    private static final float RADIUS = 0.5f;
    private static final float SCALE = 0.5f;

    @Override
    public List<FilterInterpolator> getFilterInterpolators() {
      return ImmutableList.<FilterInterpolator>of(
          new LeftEyeFilterInterpolator(),
          new RightEyeFilterInterpolator(),
          new MouthFilterInterpolator()
      );
    }

    private static class LeftEyeFilterInterpolator extends BulgeWeightedFilterInterpolator {
      @Override
      public PointF getCenter() {
        return new PointF(
            (getNormalizedHotspot().left + getNormalizedHotspot().centerX()) / 2,
            (getNormalizedHotspot().top + getNormalizedHotspot().centerY()) / 2);
      }
    }

    private static class RightEyeFilterInterpolator extends BulgeWeightedFilterInterpolator {
      @Override
      public PointF getCenter() {
        return new PointF(
            (getNormalizedHotspot().right + getNormalizedHotspot().centerX()) / 2,
            (getNormalizedHotspot().top + getNormalizedHotspot().centerY()) / 2);
      }
    }

  private static class MouthFilterInterpolator extends BulgeWeightedFilterInterpolator {
    @Override
    public PointF getCenter() {
      return new PointF(
          getNormalizedHotspot().centerX(),
          (getNormalizedHotspot().bottom + getNormalizedHotspot().centerY()) / 2);
    }
  }
}