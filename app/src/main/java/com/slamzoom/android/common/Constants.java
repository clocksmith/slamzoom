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

  // Request codes
  public static final int REQUEST_PICK_IMAGE = 101;
  public static final int REQUEST_CROP_IMAGE = 201;
  public static final int REQUEST_BUY_PACK = 301;
  public static final int REQUEST_SHARE_GIF_PERMISSIONS= 401;
  public static final int REQUEST_SHARE_GIF = 501;
  public static final int REQUEST_SHARE_VIDEO = 601;

  // Params TODO(clocksmith): refactor to params
  public static final String IMAGE_URI = "imageUri";
  public static final String CROP_RECT = "cropRect";
  public static final String EFFECT_NAME = "effectName";
  public static final String PACK_NAME = "packName";

  public static final String SELECTED_HOTSPOT = "selectedHotspot";
  public static final String SELECTED_URI = "selectedUri";
  public static final String SELECTED_EFFECT_NAME = "selectedEffectName";
  public static final String SELECTED_END_TEXT = "selectedEndText";



  public static final float DEFAULT_DURATION_SECONDS = 2;
  public static final float DEFAULT_START_PAUSE_SECONDS = 0;
  public static final float DEFAULT_END_PAUSE_SECONDS = 0;
  public static final int MAIN_FPS = 25;
  public static final int THUMBNAIL_FPS = 25;
  public static final int MAIN_SIZE_PX = 320;
  public static final int MEDIA_THUMBNAIL_DIVIDER = 4;
  public static final int THUMBNAIL_SIZE_PX = MAIN_SIZE_PX / MEDIA_THUMBNAIL_DIVIDER;
  public static final boolean DEFAULT_USE_LOCAL_COLOR_PALETTE = true;

  public static final int MAX_DIMEN_FOR_MIN_SELECTED_DIMEN_PX = MAIN_SIZE_PX * 2; // arbitrary

  public static final String WATERMARK_TEXT = "slamzoom";
  public static final int MAX_WATERMARK_TEXT_SIZE = 20;
  public static final int WATERMARK_TEXT_PADDING = 6;

  public static final PointF NORMAL_CENTER_POINT = new PointF(0.5f, 0.5f);

  public static final String PUBLIC_DIRECTORY = "/Slamzoom";
}
