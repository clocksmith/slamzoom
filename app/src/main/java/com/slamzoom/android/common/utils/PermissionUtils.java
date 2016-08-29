package com.slamzoom.android.common.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.slamzoom.android.common.Constants;

/**
 * Created by clocksmith on 8/29/16.
 */
public class PermissionUtils {

  public static boolean checkReadwriteExternalStorage(Context context) {
    return ContextCompat.checkSelfPermission(
        context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
  }

  public static void requestReadWriteExternalStorage(Activity activity) {
    ActivityCompat.requestPermissions
        (activity,
            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
            Constants.REQUEST_SHARE_GIF_PERMISSIONS);
  }
}
