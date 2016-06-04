package com.slamzoom.android.effects.interpolation.filter.single;

import android.graphics.PointF;
import android.graphics.RectF;

import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;
import com.slamzoom.android.interpolators.base.Interpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolators.spline.PointListBuilder;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSwirlFilter;

/**
 * Created by clocksmith on 6/3/16.
 */
public class UnswirlHalfTurntableOnHotspotFilterInterpolator extends UnswirlHalfOnHotspotFilterInterpolator {
  Interpolator mAngleInterpolator;

  public UnswirlHalfTurntableOnHotspotFilterInterpolator() {
    this(null);
  }

  public UnswirlHalfTurntableOnHotspotFilterInterpolator(Interpolator interpolator) {
    super(interpolator);

    mAngleInterpolator = new LinearSplineInterpolator(
        PointListBuilder.newPointListBuilder()
            .add(0f, 1f)
            .add(0.2f, 0.4f)
            .add(0.3f, 0.45f)
            .add(0.4f, 0.4f)
            .add(0.5f, 0.45f)
            .add(0.6f, 0.4f)
            .add(0.7f, 1f)
            .add(1f, 0)
            .build());
  }

  @Override
  public float getRotation() {
    return BASE_ROTATION * mAngleInterpolator.getInterpolation(getInterpolationValue());
  }
}
