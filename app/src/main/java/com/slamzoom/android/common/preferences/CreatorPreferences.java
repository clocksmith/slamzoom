package com.slamzoom.android.common.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by clocksmith on 8/24/16.
 */
public class CreatorPreferences {
  private static final String TAG = CreatorPreferences.class.getSimpleName();

  private static final String NAMESPACE_PREFIX = "$$";
  private static final String NAMESPACE = "creatorPreferences";

  private static final String FIRST_OPEN = "firstOpen";

  private static SharedPreferences getPreferences(Context context) {
    return context.getSharedPreferences(NAMESPACE_PREFIX + NAMESPACE, Context.MODE_PRIVATE);
  }

  public static void setFirstOpen(Context context, boolean firstOpen) {
    getPreferences(context).edit().putBoolean(FIRST_OPEN, firstOpen);
  }

  public static boolean isFirstOpen(Context context) {
    return getPreferences(context).getBoolean(FIRST_OPEN, true);
  }
}
