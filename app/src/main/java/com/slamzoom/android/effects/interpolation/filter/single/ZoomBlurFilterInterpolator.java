package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.PointF;
import android.graphics.RectF;

import com.slamzoom.android.effects.imagefilters.GPUImageZoomBlurFilter;
import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 3/21/16.
 */
public class ZoomBlurFilterInterpolator extends FilterInterpolator {
  public ZoomBlurFilterInterpolator(Interpolator interpolator) {
    super((interpolator));
  }

  @Override
  public GPUImageFilter getFilter(float interpolationValue, RectF normalizedHotspot) {
    PointF derivedFocus = new PointF(normalizedHotspot.centerX(), normalizedHotspot.centerY());
//    Log.wtf("HERE", "interpolationValue: " + interpolationValue +
//        "\n" + "relativeHotspot: " + relativeHotspot.toString() +
//        "\n" + "derivedFocus: " + derivedFocus.toString());
    return new GPUImageZoomBlurFilter(5 * interpolationValue, derivedFocus);
  }
}