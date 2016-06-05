package com.slamzoom.android.common;

import android.graphics.PointF;

/**
 * Created by clocksmith on 3/1/16.
 */
public class Constants {
  // Flags
  public static final boolean USE_IMAGE_WATERMARK = false;
  public static final boolean USE_TEXT_WATERMARK = false;
  public static final boolean USE_WATERMARK = USE_IMAGE_WATERMARK || USE_TEXT_WATERMARK;
  // Debug
  public static final boolean SAVE_INDIVIDUAL_FRAMES_AS_BITMAPS = false;

  public static final String WATERMARK_TEXT = "slamzoom.com";
  public static final int MAX_WATERMARK_TEXT_SIZE = 18;
  public static final int WATERMARK_TEXT_PADDING = 2;

  public static final int REQUEST_PICK_IMAGE = 1;
  public static final int REQUEST_CROP_IMAGE = 2;

  public static final String IMAGE_URI = "imageUri";
  public static final String CROP_RECT = "cropRect";

  public static final float DEFAULT_DURATION_SECONDS = 2;
  public static final float DEFAULT_START_PAUSE_SECONDS = 0;
  public static final float DEFAULT_END_PAUSE_SECONDS = 0;

  public static final int DEFAULT_FPS = 24;
  public static final int DEFAULT_GIF_SIZE_PX = 320 ;
  public static final boolean DEFAULT_USE_LOCAL_COLOR_PALETTE = true;

  // arbitrary
  public static final int MAX_DIMEN_FOR_MIN_SELECTED_DIMEN_PX = DEFAULT_GIF_SIZE_PX * 8;

  public static PointF NORMAL_CENTER_POINT = new PointF(0.5f, 0.5f);
}
