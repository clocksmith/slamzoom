package com.slamzoom.android.common.utils;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;

import com.slamzoom.android.common.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by clocksmith on 6/2/16.
 */
public class DebugUtils {
  private static final String TAG = DebugUtils.class.getSimpleName();

  // START FLAGS

  // TODO(cloksmith): convert these to build flags.

  // Firebase Crash hack
  public static final boolean REPORT_FAKE_ERROR_ON_START = false;

  // Superuser
  public static final boolean UNLOCK_UNPAID_PACKS = false;

  // Effects
  public static final boolean REVERSE_LOOP_EFFECTS = false;
  public static final boolean FORCE_SQUARE_OUTPUT_VIDEO = false;
  public static final boolean SKIP_WATERMARK = false;
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

  // Static rectangle params
  public static final float DEBUG_RECT_LEFT_FRACTION = 0.33f;
  public static final float DEBUG_RECT_TOP_FRACTION = 0.22f;
  public static final float DEBUG_RECT_SIZE_FRACTION = 0.3333f;

  // END FLAGS

  public static Rect getDebugRect(Bitmap src) {
    return new Rect(
        (int) (DEBUG_RECT_LEFT_FRACTION * src.getWidth() + 0.5f),
        (int) (DEBUG_RECT_TOP_FRACTION * src.getHeight() + 0.5f),
        (int) (src.getWidth() * (DEBUG_RECT_LEFT_FRACTION + DEBUG_RECT_SIZE_FRACTION) + 0.5f),
        (int) (src.getHeight() * (DEBUG_RECT_TOP_FRACTION + DEBUG_RECT_SIZE_FRACTION) + 0.5f));
  }

}
