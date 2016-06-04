package com.slamzoom.android.effects.interpolation.filter.single;

import com.slamzoom.android.effects.interpolation.filter.base.BaseSwirlFilterInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;

/**
 * Created by clocksmith on 6/4/16.
 */
public class UnswirlFilterInterpolator extends BaseSwirlFilterInterpolator {
  public UnswirlFilterInterpolator() {
    this(null);
  }

  public UnswirlFilterInterpolator(Interpolator interpolator) {
    super(interpolator);
  }

  @Override
  public float getRotation() {
    return BASE_ROTATION * getInterpolationValueCompliment();
  }
}
