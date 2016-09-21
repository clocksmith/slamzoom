package com.slamzoom.android.common.data;

import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by clocksmith on 6/4/16.
 */
public class RectFUtils {
  public static float getMinDimen(RectF rect) {
    return Math.min(rect.width(), rect.height());
//    return rect.width();
  }

  public static PointF getCenterPointF(RectF rect) {
    return new PointF(rect.centerX(), rect.centerY());
  }
}
