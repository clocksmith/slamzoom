package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.PointF;
import android.graphics.RectF;

import com.slamzoom.android.common.utils.RectFUtils;
import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSwirlFilter;

/**
 * Created by clocksmith on 6/2/16.
 */
public class UnswirlHalfOnHotspotFilterInterpolator extends UnswirlFilterInterpolator {
  public UnswirlHalfOnHotspotFilterInterpolator() {
    this(null);
  }

  public UnswirlHalfOnHotspotFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  public float getRadius() {
    return BASE_RADIUS * getMinDimenOfHotspot();
  }

  @Override
  public PointF getCenter() {
    return getCenterOfHotspot();
  }
}
