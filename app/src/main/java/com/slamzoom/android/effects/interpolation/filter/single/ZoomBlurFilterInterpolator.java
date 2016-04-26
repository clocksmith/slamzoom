package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import com.slamzoom.android.effects.imagefilters.GPUImageZoomBlurFilter;
import com.slamzoom.android.effects.interpolation.filter.base.AdjustedFilterInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 3/21/16.
 */
public class ZoomBlurFilterInterpolator extends AdjustedFilterInterpolator {
  public ZoomBlurFilterInterpolator(Interpolator interpolator) {
    super((interpolator));
  }

  @Override
  protected GPUImageFilter getFilter(float interpolationValue, PointF relativeFocus, RectF relativeHotspot) {
    Log.wtf("HERE", "relativeFocus: " + relativeFocus.toString());
    Log.wtf("HERE", "relativeHotspot: " + relativeHotspot.toString());
    PointF testFocus = new PointF(relativeHotspot.centerX(), relativeHotspot.centerY());
    Log.wtf("HERE", "testFocus: " + testFocus.toString());
//    return new GPUImageZoomBlurFilter(5 * interpolationValue, testFocus);
    return new GPUImageZoomBlurFilter(5 * interpolationValue, relativeFocus);
  }
}