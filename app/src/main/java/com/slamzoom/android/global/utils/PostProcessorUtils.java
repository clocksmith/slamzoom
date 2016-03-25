package com.slamzoom.android.global.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 3/20/16.
 */
public class PostProcessorUtils {
  private static final float MAX_GUASSIAN_BLUR = 1;
  public static Bitmap process(Context context, Bitmap original, List<GPUImageFilter> filters) {
    Bitmap processedBitmap = original;
    for (GPUImageFilter filter : filters) {
      GPUImage gpuImage = new GPUImage(context);
      gpuImage.setFilter(filter);
      processedBitmap = gpuImage.getBitmapWithFilterApplied(processedBitmap);
    }
    return processedBitmap;
  }
}
