package com.slamzoom.android.common;

import android.graphics.RectF;

/**
 * Created by clocksmith on 6/2/16.
 */
public class BuildFlags {
  private static final String TAG = BuildFlags.class.getSimpleName();

  // TODO(cloksmith): convert these to actual build flags.

  // Firebase Crash hack
  public static final boolean REPORT_FAKE_ERROR_ON_START = false;

  // Superuser
  public static final boolean UNLOCK_UNPAID_PACKS = false;
  public static final boolean ENABLED_ADD_TEXT = false;
  public static final boolean SKIP_WATERMARK = false;
  public static final boolean USE_MONA_TEMPLATE = false;

  // Effects
  public static final boolean REVERSE_LOOP_EFFECTS = false;
  public static final boolean FORCE_SQUARE_OUTPUT_VIDEO = false;
  public static final boolean SKIP_START_AND_END_PAUSE = false;
  public static final boolean USE_DEBUG_EFFECTS = false;

  // Convenience
  public static final boolean SKIP_GIF_CACHE = false; // setting this to true is broken
  public static final boolean SKIP_GENERATE_THUMBNAIL_GIFS = false;
  public static final boolean SKIP_RECYCLE_BITMAP = false;
  public static final boolean SKIP_START_SCREEN = false;
  public static final boolean USE_PREDEFINED_HOTSPOT = false;

  // Quality testing
  public static final boolean SAVE_SRC_AS_PNG = false;
  public static final boolean SAVE_SCALED_FRAMES_AS_PNGS = false;
  public static final boolean SAVE_FILTERED_FRAMES_AS_PNGS = false;

  // Static rectangle params TODO(clocksmith): Can use relative rect now.
  public static final RectF DEBUG_RECT = new RectF(0.3f, 0.2f, 0.6f, 0.5f);
}
