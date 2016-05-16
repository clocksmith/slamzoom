package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.PointF;
import android.graphics.RectF;

import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 4/2/16.
 */
public class BulgeFilterInterpolator extends FilterInterpolator {
  private static final float RADIUS = 1f;
  private static final float SCALE = 0.5f;
  public BulgeFilterInterpolator() {
    super();
  }

  public BulgeFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  public GPUImageFilter getFilter(float interpolationValue, RectF normalizedHotspot, float normalizedScale) {
    return new GPUImageBulgeDistortionFilter(
        RADIUS * interpolationValue,
        SCALE * interpolationValue,
        new PointF(normalizedHotspot.centerX(), normalizedHotspot.centerY()));
  }
}
