package com.slamzoom.android.effects.interpolation.filter.calculators;

import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.interpolators.Interpolator;

/**
 * Created by clocksmith on 6/4/16.
 */
public class FloatCalculator extends BaseCalculator {
  private float mBaseValue;
  private boolean mAlwaysNegateBaseValue = false;

  public FloatCalculator(FilterInterpolator filterInterpolator, float baseValue) {
    this(filterInterpolator, baseValue, false);
  }

  public FloatCalculator(FilterInterpolator filterInterpolator, float baseValue, boolean alwaysNegateBaseValue) {
    super(filterInterpolator);
    mAlwaysNegateBaseValue = alwaysNegateBaseValue;
    mBaseValue = baseValue;
  }

  public void setBaseValue(float baseValue) {
    mBaseValue = baseValue;
  }

  public float getBaseValue() {
    return mBaseValue * (mAlwaysNegateBaseValue ? -1 : 1);
  }

  public float getValueFromInterpolation() {
    return getBaseValue() * getInterpolationValue();
  }

  public float getValueFromInterpolationCompliment() {
    return getBaseValue() * getInterpolationValueCompliment();
  }

  public float getValueFromMinHotspotDimen() {
    return getBaseValue() * getMinHotspotDimen();
  }

  public float getValueFromSubInterpolationOfInterpolation(Interpolator interpolator) {
    return getBaseValue() * interpolator.getInterpolation(getInterpolationValue());
  }

  public float getValueFromSubInterpolationOfInterpolationCompliment(Interpolator interpolator) {
    return getBaseValue() * interpolator.getInterpolation(getInterpolationValueCompliment());
  }
}