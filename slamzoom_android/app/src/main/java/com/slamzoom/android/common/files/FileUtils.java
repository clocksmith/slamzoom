package com.slamzoom.android.common.files;

import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.slamzoom.android.SzApp;
import com.slamzoom.android.common.logging.SzLog;
import com.slamzoom.android.ui.create.CreateActivity;

import java.io.File;
import java.io.IOException;

/**
 * Created by clocksmith on 8/25/16.
 */
public class FileUtils {
  public static final String TAG = FileUtils.class.getSimpleName();

  private static final String PUBLIC_DIRECTORY = "/Slamzoom";
  private static final String FILE_PREFIX = "slamzoom_";

  public static Uri getUri(File file) {
    // see https://stackoverflow.com/questions/38200282/android-os-fileuriexposedexception-file-storage-emulated-0-test-txt-exposed
    // old way: Uri uri = Uri.fromFile(gifFile);
    Uri uri = FileProvider.getUriForFile(
        SzApp.CONTEXT,
        SzApp.CONTEXT.getApplicationContext().getPackageName() + ".com.slamzoom.android.provider",
        file);
    return uri;
  }

  public static File createTimestampedFileWithId(FileType fileType, String id) {
    String filename = FILE_PREFIX + id + "_" + System.currentTimeMillis() + "." + fileType.ext;
    return createFileWithFilename(fileType, filename);
  }

  public static File createFileWithId(FileType fileType, String id) {
    String filename = FILE_PREFIX + id + "." + fileType.ext;
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
    File dir = new File(Environment.getExternalStoragePublicDirectory(envDir).getPath(), PUBLIC_DIRECTORY);
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
