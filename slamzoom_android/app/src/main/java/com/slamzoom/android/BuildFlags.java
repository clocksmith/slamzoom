package com.slamzoom.android;

import android.graphics.RectF;

/**
 * Created by clocksmith on 6/2/16.
 */
public class BuildFlags {
  // TODO(cloksmith): convert these to actual build flags.

  // Firebase Crash hack
  public static final boolean REPORT_FAKE_ERROR_ON_START = false;

  // Superuser
  public static final boolean UNLOCK_UNPAID_PACKS = false;
  public static final boolean SKIP_WATERMARK = true;
  public static final boolean USE_MONA_TEMPLATE = false;

  // Effect Modifications
  public static final boolean REVERSE_LOOP_EFFECTS = false;
  public static final boolean FORCE_SQUARE_OUTPUT_VIDEO = false;
  public static final boolean SKIP_START_AND_END_PAUSE = false;

  // Debug
  public static final boolean USE_DEBUG_EFFECTS = false;
  public static final boolean USE_DEBUG_HOTSPOT = false;
  public static final RectF DEBUG_HOTSPOT = new RectF(0.3f, 0.2f, 0.6f, 0.5f);
  public static final boolean ACT_AS_FIRST_OPEN = false;

  // Convenience
  public static final boolean SKIP_GIF_CACHE = false; // setting this to true is broken
  public static final boolean SKIP_GENERATE_THUMBNAIL_GIFS = false;
  public static final boolean SKIP_RECYCLE_BITMAP = false;
  public static final boolean SKIP_START_SCREEN = false;

  // Quality testing
  public static final boolean SAVE_SRC_AS_PNG = false;
  public static final boolean SAVE_SCALED_FRAMES_AS_PNGS = false;
  public static final boolean SAVE_FILTERED_FRAMES_AS_PNGS = false;
}
