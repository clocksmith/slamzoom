package com.slamzoom.android.common.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.AnyRes;

import com.slamzoom.android.R;
import com.slamzoom.android.SzApp;

/**
 * Created by clocksmith on 9/16/16.
 */
public class UriUtils {
  public static Uri getUriFromRes(@AnyRes int resId) {
    return getUriFromRes(SzApp.CONTEXT, resId);
  }

  public static Uri getUriFromRes(Context context, @AnyRes int resId) {
    return Uri.parse(
        ContentResolver.SCHEME_ANDROID_RESOURCE
        + "://"
        + context.getResources().getResourcePackageName(resId)
        + "/"
        + context.getResources().getResourceTypeName(resId)
        + "/"
        + context.getResources().getResourceEntryName(resId));
  }

//  public static Uri getUriFromRes(Context context, @AnyRes int resId) {
//    return Uri.parse(
//        ContentResolver.SCHEME_ANDROID_RESOURCE
//            + "://"
//            + context.getResources().getResourcePackageName(resId)
//            + "/"
//            + resId);
//  }
}
