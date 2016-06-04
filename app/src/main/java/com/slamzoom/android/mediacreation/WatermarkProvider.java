package com.slamzoom.android.mediacreation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.slamzoom.android.R;

/**
 * Created by clocksmith on 6/2/16.
 */
public class WatermarkProvider {
  private static Bitmap mWatermarkBitmap;

  public static Bitmap getWatermarkBitmap(Context context) {
    if (mWatermarkBitmap == null) {
      mWatermarkBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.slamzoom_white);
    }
    return mWatermarkBitmap;
  }
}
