package com.slamzoom.android.effects.interpolation.filter.group;

import android.graphics.PointF;
import android.graphics.RectF;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlFilterInterpolator;

import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSwirlFilter;

/**
 * Created by clocksmith on 6/2/16.
 */
public class UnswirlEyesOnHotspotFilterInterpolatorGroup implements FilterInterpolatorGroup {
  private static final float BASE_RADIUS = 0.3f;

  @Override
  public List<FilterInterpolator> getFilterInterpolators() {
    return ImmutableList.<FilterInterpolator>of(
        new LeftFilterInterpolator(),
        new RightFilterInterpolator()
    );
  }

  private static class LeftFilterInterpolator extends UnswirlFilterInterpolator {
    @Override
    public float getRadius() {
      return BASE_RADIUS * getMinDimenOfHotspot();
    }

    @Override
    public PointF getCenter() {
      return new PointF(
          (getNormalizedHotspot().left + getNormalizedHotspot().centerX()) / 2,
          getNormalizedHotspot().centerY());
    }
  }

  private static class RightFilterInterpolator extends UnswirlFilterInterpolator {
    @Override
    public float getRadius() {
      return BASE_RADIUS * getMinDimenOfHotspot();
    }

    @Override
    public PointF getCenter() {
      return new PointF(
          (getNormalizedHotspot().right + getNormalizedHotspot().centerX()) / 2,
          getNormalizedHotspot().centerY());
    }
  }
}
