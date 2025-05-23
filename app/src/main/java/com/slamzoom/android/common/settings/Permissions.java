package com.slamzoom.android.common.settings;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.slamzoom.android.common.intents.RequestCodes;

/**
 * Created by clocksmith on 8/29/16.
 */
public class Permissions {

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
            RequestCodes.REQUEST_SHARE_GIF_PERMISSIONS);
  }
}
