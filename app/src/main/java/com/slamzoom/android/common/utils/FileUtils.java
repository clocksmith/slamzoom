package com.slamzoom.android.common.utils;

import android.os.Environment;
import android.util.Log;

import com.slamzoom.android.SzApp;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.FileType;
import com.slamzoom.android.common.SzLog;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by clocksmith on 8/25/16.
 */
public class FileUtils {
  public static final String TAG = FileUtils.class.getSimpleName();

  private static final String PREFIX = "slamzoom_";

  public static File createTimestampedFileWithId(FileType fileType, String id) {
    String filename = PREFIX + id + "_" + System.currentTimeMillis() + "." + fileType.ext;
    return createFileWithFilename(fileType, filename);
  }

  public static File createFileWithId(FileType fileType, String id) {
    String filename = PREFIX + id + "." + fileType.ext;
    return createFileWithFilename(fileType, filename);
  }

  public static File createPrivateFileWithFilename(String filename) {
    return createFileInDir(SzApp.CONTEXT.getFilesDir(), filename);
  }

  public static boolean deletePrivateFileWithFilename(String filename) {
    File file = new File(SzApp.CONTEXT.getFilesDir(), filename);
    return file.delete();
  }

  private static File createFileWithFilename(FileType fileType, String filename) {
    switch(fileType) {
      case GIF:
      case PNG:
      case JPEG:
        return createFileInEnvDir(Environment.DIRECTORY_PICTURES, filename);
      case VIDEO:
        return createFileInEnvDir(Environment.DIRECTORY_MOVIES, filename);
      default:
        SzLog.e(TAG, "Unspported fileType: " + fileType.name());
        return null;
    }
  }

  private static File createFileInEnvDir(String envDir, String filename) {
    File dir = new File(Environment.getExternalStoragePublicDirectory(envDir).getPath(), Constants.PUBLIC_DIRECTORY);
    return createFileInDir(dir, filename);
  }

  private static File createFileInDir(File dir, String filename) {
    if (!dir.exists()) {
      if (!dir.mkdirs()) {
        SzLog.e(TAG, "Cannot make directory: " + dir);
      } else {
        Log.d(TAG, dir + " successfully created.");
      }
    } else {
      Log.d(TAG, dir + " already exists.");
    }

    File file = new File(dir, filename);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        SzLog.e(TAG, "Cannot make file: " + dir);
      }
    }

    return file;
  }
}
