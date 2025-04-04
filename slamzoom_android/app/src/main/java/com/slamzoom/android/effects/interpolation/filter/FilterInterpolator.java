package com.slamzoom.android.effects.interpolation.filter;

import android.graphics.RectF;

import com.slamzoom.android.interpolators.Interpolator;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 4/25/16.
 *
 * marker abstract class
 */
public abstract class FilterInterpolator {
  private Interpolator mInterpolator;
  private float mInterpolationValue;
  private RectF mNormalizedHotspot;

  public FilterInterpolator() {
    this(null);
  }

  public FilterInterpolator(Interpolator interpolator) {
    mInterpolator = interpolator;
  }

  public synchronized GPUImageFilter getInterpolationFilter(float percent, RectF normalizedHotspot) {
    setInterpolationValue(mInterpolator.getInterpolation(percent));
    setNormalizedHotspot(normalizedHotspot);
    return getFilter();
  }

  public Interpolator getInterpolator() {
    return mInterpolator;
  }

  public boolean hasInterpolator() {
    return mInterpolator != null;
  }

  public void setInterpolator(Interpolator interpolator) {
    mInterpolator = interpolator;
  }

  public synchronized float getInterpolationValue() {
    return mInterpolationValue;
  }

  public synchronized RectF getRelativeHotspot() {
    return mNormalizedHotspot;
  }

  public synchronized void setInterpolationValue(float interpolationValue) {
    mInterpolationValue = interpolationValue;
  }

  public synchronized void setNormalizedHotspot(RectF normalizedHotspot) {
    mNormalizedHotspot = normalizedHotspot;
  }

  protected abstract GPUImageFilter getFilter();
}
