package com.slamzoom.android.effects.interpolation.filter.group;

import android.graphics.PointF;
import android.graphics.RectF;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;

import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 4/22/16.
 */
public class BulgeEyesFilterInterpolatorGroup implements FilterInterpolatorGroup {
  @Override
  public List<FilterInterpolator> getFilterInterpolators() {
    return ImmutableList.<FilterInterpolator>of(
        new LeftEyeFilterInterpolator(),
        new RightEyeFilterInterpolator()
    );
  }

  private static class LeftEyeFilterInterpolator extends FilterInterpolator {
    @Override
    protected GPUImageFilter getFilter(float interpolationValue, RectF normalizedHotspot, float normalizedScale) {
      return new GPUImageBulgeDistortionFilter(1,
          0.5f * interpolationValue,
          new PointF((normalizedHotspot.left + normalizedHotspot.centerX() / 2), normalizedHotspot.centerY()));
    }
  }

  private static class RightEyeFilterInterpolator extends FilterInterpolator {
    @Override
    protected GPUImageFilter getFilter(float interpolationValue, RectF normalizedHotspot, float normalizedScale) {
      return new GPUImageBulgeDistortionFilter(1,
          0.5f * interpolationValue,
          new PointF((normalizedHotspot.right + normalizedHotspot.centerX() / 2), normalizedHotspot.centerY()));
    }
  }
}
