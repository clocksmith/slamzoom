package com.slamzoom.android.common;

import android.graphics.PointF;

/**
 * Created by clocksmith on 3/1/16.
 */
public class Constants {
  public static final String PACKAGE_NAME = "com.slamzoom.android";
  // Flags
  public static final boolean USE_IMAGE_WATERMARK = false;
  public static final boolean USE_TEXT_WATERMARK = true;
  public static final boolean USE_WATERMARK = USE_IMAGE_WATERMARK || USE_TEXT_WATERMARK;

  // Request codes
  public static final int REQUEST_PICK_IMAGE = 101;
  public static final int REQUEST_CROP_IMAGE = 201;
  public static final int REQUEST_BUY_PACK = 301;
  public static final int REQUEST_SHARE = 401;

  // Params TODO(clocksmith): refactor to params
  public static final String IMAGE_URI = "imageUri";
  public static final String CROP_RECT = "cropRect";
  public static final String EFFECT_NAME = "effectName";
  public static final String PACK_NAME = "packName";

  public static final float DEFAULT_DURATION_SECONDS = 2;
  public static final float DEFAULT_START_PAUSE_SECONDS = 0;
  public static final float DEFAULT_END_PAUSE_SECONDS = 0;
  public static final int MAIN_FPS = 24;
  public static final int THUMBNAIL_FPS = 12;
  public static final int DEFAULT_GIF_SIZE_PX = 320;
  public static final int GIF_THUMBNAIL_DIVIDER = 4;
  public static final int DEFAULT_GIF_PREVIEW_SIZE_PX = DEFAULT_GIF_SIZE_PX / GIF_THUMBNAIL_DIVIDER;
  public static final boolean DEFAULT_USE_LOCAL_COLOR_PALETTE = true;

  public static final int MAX_DIMEN_FOR_MIN_SELECTED_DIMEN_PX = DEFAULT_GIF_SIZE_PX * 4; // arbitrary

  public static final String WATERMARK_TEXT = "slamzoom.com";
  public static final int MAX_WATERMARK_TEXT_SIZE = 16;
  public static final int WATERMARK_TEXT_PADDING = 2;

  public static final PointF NORMAL_CENTER_POINT = new PointF(0.5f, 0.5f);

  public static final String PUBLIC_DIRECTORY = "/SlamZoom";
}
