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
public class TinyBulgesSwimFilterInterpoaltor implements FilterInterpolatorGroup {
  private static final float RADIUS = 0.2f;
  private static final float SCALE = 0.5f;

  @Override
  public List<FilterInterpolator> getFilterInterpolators() {
    return ImmutableList.<FilterInterpolator>of(
        new LeftToRightBulgeInterpolator(),
        new TopLeftToBottomRightBulgeInterpolator(),
        new BottomLeftToTopRightBulgeInterpolator(),
        new RightToLeftBulgeInterpolator(),
        new TopRightToBottomLeftBulgeInterpolator(),
        new BottomRightToTopLeftBulgeInterpolator()
    );
  }

  private static class LeftToRightBulgeInterpolator extends FilterInterpolator {
    @Override
    protected GPUImageFilter getFilter(float interpolationValue, RectF normalizedHotspot) {
      return new GPUImageBulgeDistortionFilter(
          RADIUS,
          SCALE,
          new PointF(
              new LinearInterpolator(
                  normalizedHotspot.left,
                  normalizedHotspot.right).getInterpolation(interpolationValue * 0.5f),
              normalizedHotspot.centerY()));
    }
  }

  private static class TopLeftToBottomRightBulgeInterpolator extends FilterInterpolator {
    @Override
    protected GPUImageFilter getFilter(float interpolationValue, RectF normalizedHotspot) {
      return new GPUImageBulgeDistortionFilter(
          RADIUS,
          SCALE,
          new PointF(
              new LinearInterpolator(
                  normalizedHotspot.left,
                  normalizedHotspot.right).getInterpolation(interpolationValue* (float) Math.random()),
              new LinearInterpolator(
                  normalizedHotspot.top,
                  normalizedHotspot.bottom).getInterpolation(interpolationValue * (float) Math.random())));
    }
  }

  private static class BottomLeftToTopRightBulgeInterpolator extends FilterInterpolator {
    @Override
    protected GPUImageFilter getFilter(float interpolationValue, RectF normalizedHotspot) {
      return new GPUImageBulgeDistortionFilter(
          RADIUS,
          SCALE,
          new PointF(
              new LinearInterpolator(
                  normalizedHotspot.left,
                  normalizedHotspot.right).getInterpolation(interpolationValue * (float) Math.random()),
              new LinearInterpolator(
                  normalizedHotspot.bottom,
                  normalizedHotspot.top).getInterpolation(interpolationValue * (float) Math.random())));
    }
  }

  private static class RightToLeftBulgeInterpolator extends FilterInterpolator {
    @Override
    protected GPUImageFilter getFilter(float interpolationValue, RectF normalizedHotspot) {
      return new GPUImageBulgeDistortionFilter(
          RADIUS,
          SCALE,
          new PointF(
              new LinearInterpolator(
                  normalizedHotspot.right,
                  normalizedHotspot.left).getInterpolation(interpolationValue * (float) Math.random()),
              normalizedHotspot.centerY()));
    }
  }

  private static class TopRightToBottomLeftBulgeInterpolator extends FilterInterpolator {
    @Override
    protected GPUImageFilter getFilter(float interpolationValue, RectF normalizedHotspot) {
      return new GPUImageBulgeDistortionFilter(
          RADIUS,
          SCALE,
          new PointF(
              new LinearInterpolator(
                  normalizedHotspot.right,
                  normalizedHotspot.left).getInterpolation(interpolationValue * (float) Math.random()),
              new LinearInterpolator(
                  normalizedHotspot.top,
                  normalizedHotspot.bottom).getInterpolation(interpolationValue * (float) Math.random())));
    }
  }

  private static class BottomRightToTopLeftBulgeInterpolator extends FilterInterpolator {
    @Override
    protected GPUImageFilter getFilter(float interpolationValue, RectF normalizedHotspot) {
      return new GPUImageBulgeDistortionFilter(
          RADIUS,
          SCALE,
          new PointF(
              new LinearInterpolator(
                  normalizedHotspot.right,
                  normalizedHotspot.left).getInterpolation(interpolationValue * (float) Math.random()),
              new LinearInterpolator(
                  normalizedHotspot.bottom,
                  normalizedHotspot.top).getInterpolation(interpolationValue) * (float) Math.random()));
    }
  }
}