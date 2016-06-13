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

  // Flags
  public static final boolean DEBUG_SAVE_TRANSFORMED_FRAMES_AS_BITMAPS = false;
  public static final boolean DEBUG_SAVE_SCALED_FRAMES_AS_BITMAPS = false;
  public static final boolean DEBUG_SAVE_FILTERED_FRAMES_AS_BITMAPS = false;
  public static final boolean DEBUG_USE_STATIC_RECTANGLE = false;
  public static final boolean DEBUG_USE_CACHE = true;

  public static final float DEBUG_RECT_LEFT_FRACTION = 0.18f;
  public static final float DEBUG_RECT_TOP_FRACTION = 0.12f;
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
      Log.e(TAG, "w: " + finalBitmap.getWidth() + " h: " + finalBitmap.getHeight());
      if (!file.getParentFile().isDirectory()) {
        Log.e(TAG, "No directory exitsts: " + file.getParentFile());
        if (!file.getParentFile().mkdirs()) {
          Log.e(TAG, "Cannot make directory: " + file.getParentFile());
        }
        {
          Log.d(TAG, direct + " successfully created.");
        }
      } else {
        Log.d(TAG, direct + " already exists.");
      }
      if (frameIndex >= 0) {
        FileOutputStream out = null;
        try {
          out = new FileOutputStream(file);
          Log.e(TAG, "w: " + finalBitmap.getWidth() + " h: " + finalBitmap.getHeight());
          finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          try {
            if (out != null) {
              out.close();
            }
          } catch (IOException e) {
            Log.e(TAG, "Cannot save bitmap");
          }
        }
      }
  }
}
