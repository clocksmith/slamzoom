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
  public static final String HOTSPOT = "hotspot";
  public static final String HOTSPOT_SCALE = "hotspotScale";
  public static final String END_TEXT_LENGTH = "endTextLength";
  public static final String EFFECT_NAME = "effectName";
  public static final String PACK_NAME = "packName";

  public static final String SELECTED_HOTSPOT = "selectedHotspot";
  public static final String SELECTED_URI = "selectedUri";
  public static final String SELECTED_EFFECT_NAME = "selectedEffectName";
  public static final String SELECTED_END_TEXT = "selectedEndText";
  public static final String PURCHASED_PACK_NAMES = "purchasedPackNames";
  public static final String NEEDS_UPDATE_PURCHASED_PACK_NAMES = "needsUpdatePurchasedPackNames";

  public static final float DEFAULT_START_PAUSE_SECONDS = 1;
  public static final float DEFAULT_DURATION_SECONDS = 2;
  public static final float DEFAULT_END_PAUSE_SECONDS = 2;
  public static final int MAIN_FPS = 24;
  public static final int THUMBNAIL_FPS = 16;
  public static final int MAIN_SIZE_PX = 320; // must be even
  public static final int THUMBNAIL_SIZE_PX = 80; // must be even
  public static final float MEDIA_THUMBNAIL_DIVIDER = (float) MAIN_SIZE_PX / THUMBNAIL_SIZE_PX;
  public static final boolean DEFAULT_USE_LOCAL_COLOR_PALETTE = true;
  public static final int VIDEO_KBPS = 128;

  public static final int MAX_DIMEN_FOR_MIN_SELECTED_DIMEN_PX = 1024; // arbitrary

  public static final String WATERMARK_TEXT = "slamzoom";
  public static final int MAX_WATERMARK_TEXT_SIZE = 20;
  public static final int WATERMARK_TEXT_PADDING = 6;

  public static final PointF NORMAL_CENTER_POINT = new PointF(0.5f, 0.5f);

  public static final String PUBLIC_DIRECTORY = "/Slamzoom";
}
