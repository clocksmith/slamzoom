package com.slamzoom.android.common.utils;

import android.graphics.Bitmap;
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

  public static void saveFrameAsBitmap(Bitmap finalBitmap, int frameIndex) {
      File direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "/SlamZoom");
      File file = new File(direct, "test" + frameIndex + ".png");
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
