package com.slamzoom.android.common.utils;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by clocksmith on 6/2/16.
 */
public class DebugUtils {
  private static final String TAG = DebugUtils.class.getSimpleName();

  // TODO(cloksmith): convert these to build flags.

  // Superuser
  public static final boolean UNLOCK_ALL_PACKS = false;

  // Performance
  public static final boolean USE_GIF_CACHE = true;
  public static final boolean GENERATE_THUMBNAIL_GIFS = true;

  // Debug gif quality
  public static final boolean USE_DEBUG_EFFECTS = false;
  public static final boolean SAVE_SRC_AS_BITMAP = false;
  public static final boolean SAVE_SCALED_FRAMES_AS_BITMAPS = false;
  public static final boolean SAVE_FILTERED_FRAMES_AS_BITMAPS = false;

  // Convenience
  public static final boolean SKIP_START_SCREEN = false;
  public static final boolean USE_STATIC_RECTANGLE = false;

  public static final float DEBUG_RECT_LEFT_FRACTION = 0.33f;
  public static final float DEBUG_RECT_TOP_FRACTION = 0.22f;
  public static final float DEBUG_RECT_SIZE_FRACTION = 0.3333f;

  public static Rect getDebugRect(Bitmap src) {
    return new Rect(
        (int) (DEBUG_RECT_LEFT_FRACTION * src.getWidth() + 0.5f),
        (int) (DEBUG_RECT_TOP_FRACTION * src.getHeight() + 0.5f),
        (int) (src.getWidth() * (DEBUG_RECT_LEFT_FRACTION + DEBUG_RECT_SIZE_FRACTION) + 0.5f),
        (int) (src.getHeight() * (DEBUG_RECT_TOP_FRACTION + DEBUG_RECT_SIZE_FRACTION) + 0.5f));
  }

  public static void saveFrameAsBitmap(Bitmap finalBitmap, String type, int frameIndex) {
      File direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "/SlamZoom");
      File file = new File(direct, "debug_" + frameIndex + "_" + type + ".png");
      if (!file.getParentFile().isDirectory()) {
        Log.d(TAG, "No directory exists: " + file.getParentFile());
        if (!file.getParentFile().mkdirs()) {
          SzLog.e(TAG, "Cannot make directory: " + file.getParentFile());
        }
      } else {
        Log.d(TAG, direct + " already exists.");
      }
      if (frameIndex >= 0) {
        FileOutputStream out = null;
        try {
          out = new FileOutputStream(file);
          finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          try {
            if (out != null) {
              out.close();
            }
          } catch (IOException e) {
            SzLog.e(TAG, "Cannot save bitmap");
          }
        }
      }
  }
}
