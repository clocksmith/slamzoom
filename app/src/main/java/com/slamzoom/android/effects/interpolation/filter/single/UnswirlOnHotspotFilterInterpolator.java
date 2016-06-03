package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.PointF;
import android.graphics.RectF;

import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSwirlFilter;

/**
 * Created by clocksmith on 6/2/16.
 */
public class UnswirlOnHotspotFilterInterpolator extends FilterInterpolator {
  public UnswirlOnHotspotFilterInterpolator() {
    super();
  }

  @Override
  public GPUImageFilter getFilter(float interpolationValue, RectF normalizedHotspot) {
    return new GPUImageSwirlFilter(0.5f * Math.min(normalizedHotspot.width(), normalizedHotspot.height()),
        0.5f * (1 - interpolationValue),
        new PointF(normalizedHotspot.centerX(), normalizedHotspot.centerY()));
  }
}
