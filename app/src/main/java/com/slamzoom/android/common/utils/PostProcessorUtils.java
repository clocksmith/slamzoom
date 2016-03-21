package com.slamzoom.android.common.utils;

import android.graphics.Bitmap;

import com.slamzoom.android.SlamzoomApplication;

import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 3/20/16.
 */
public class PostProcessorUtils {
  private static final float MAX_GUASSIAN_BLUR = 1;
  public static Bitmap process(Bitmap original, List<GPUImageFilter> filters) {
    Bitmap processedBitmap = original;
    for (GPUImageFilter filter : filters) {
      GPUImage gpuImage = new GPUImage(SlamzoomApplication.getAppContext());
      gpuImage.setFilter(filter);
      processedBitmap = gpuImage.getBitmapWithFilterApplied(processedBitmap);
    }
    return processedBitmap;
  }
}
