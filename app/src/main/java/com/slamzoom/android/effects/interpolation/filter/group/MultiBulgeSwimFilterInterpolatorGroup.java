package com.slamzoom.android.effects.interpolation.filter.group;

import android.graphics.PointF;
import android.graphics.RectF;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;
import com.slamzoom.android.interpolators.base.LinearInterpolator;

import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 5/16/16.
 */
public class MultiBulgeSwimFilterInterpolatorGroup implements FilterInterpolatorGroup {
  private static final float RADIUS = 0.5f;
  private static final float SCALE = 0.5f;

  @Override
  public List<FilterInterpolator> getFilterInterpolators() {
    return ImmutableList.<FilterInterpolator>of(
        new LeftToRightBulgeInterpolator(),
        new RightToLeftBulgeInterpolator()
    );
  }

  private static class LeftToRightBulgeInterpolator extends FilterInterpolator {
    @Override
    protected GPUImageFilter getFilter(float interpolationValue, RectF normalizedHotspot) {
      return new GPUImageBulgeDistortionFilter(
          RADIUS * interpolationValue,
          SCALE * interpolationValue,
          new PointF(
              new LinearInterpolator(
                  normalizedHotspot.left,
                  normalizedHotspot.right).getInterpolation(interpolationValue),
              normalizedHotspot.centerY()));
    }
  }

  private static class RightToLeftBulgeInterpolator extends FilterInterpolator {
    @Override
    protected GPUImageFilter getFilter(float interpolationValue, RectF normalizedHotspot) {
      return new GPUImageBulgeDistortionFilter(
          RADIUS * interpolationValue,
          SCALE * interpolationValue,
          new PointF(
              new LinearInterpolator(
                  normalizedHotspot.right,
                  normalizedHotspot.left).getInterpolation(interpolationValue),
              normalizedHotspot.centerY()));
    }
  }
}