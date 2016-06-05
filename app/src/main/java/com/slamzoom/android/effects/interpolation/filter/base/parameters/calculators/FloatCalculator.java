package com.slamzoom.android.effects.interpolation.filter.base.parameters.calculators;

import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;

/**
 * Created by clocksmith on 6/4/16.
 */
public class FloatCalculator extends BaseCalculator {
  private float mBaseValue;

  public FloatCalculator(FilterInterpolator filterInterpolator, float baseValue) {
    super(filterInterpolator);
    mBaseValue = baseValue;
  }

  public void setBaseValue(float baseValue) {
    mBaseValue = baseValue;
  }

  public float getBaseValue() {
    return mBaseValue;
  }

  public float getValueFromInterpolation() {
    return mBaseValue * getInterpolationValue();
  }

  public float getValueFromInterpolationCompliment() {
    return mBaseValue * getInterpolationValueCompliment();
  }

  public float getValueFromMinHotspotDimen() {
    return mBaseValue * getMinHotspotDimen();
  }

  public float getValueFromSubInterpolationOfInterpolation(Interpolator interpolator) {
    return mBaseValue * interpolator.getInterpolation(getInterpolationValue());
  }

  public float getValueFromSubInterpolationOfInterpolationCompliment(Interpolator interpolator) {
    return mBaseValue * interpolator.getInterpolation(getInterpolationValueCompliment());
  }
}