package com.slamzoom.android.common;

import android.os.Environment;
import android.util.Log;

import com.google.common.base.Strings;
import com.slamzoom.android.common.utils.SzLog;

import java.io.File;

/**
 * Created by clocksmith on 8/25/16.
 */
public class Files {
  public static final String TAG = Files.class.getSimpleName();

  public enum FileType {
    GIF("image/*", "gif"),
    VIDEO("video/*", "3gp");

    public String mime;
    public String ext;

    FileType(String mime, String ext) {
      this.mime = mime;
      this.ext = ext;
    }
  }

  public static File makeFile(FileType fileType, String id) {
    long now = System.currentTimeMillis();
    String filename = "slamzoom_" + id + "_" + now + "." + fileType.ext;
    String envDir = Environment.DIRECTORY_DCIM;
    if (fileType == FileType.GIF) {
      envDir = Environment.DIRECTORY_PICTURES;
    } else if (fileType == FileType.VIDEO) {
      envDir = Environment.DIRECTORY_MOVIES;
    }
    return makeFile(envDir, filename);
  }

  private static File makeFile(String envDir, String filename) {
    File dir = new File(Environment.getExternalStoragePublicDirectory(envDir).getPath(), Constants.PUBLIC_DIRECTORY);

    if (!dir.exists()) {
      if (!dir.mkdirs()) {
        SzLog.e(TAG, "Cannot make directory: " + dir);
      } else {
        Log.d(TAG, dir + " successfully created.");
      }
    } else {
      Log.d(TAG, dir + " already exists.");
    }

    return new File(dir, filename);
  }
}
