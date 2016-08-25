package com.slamzoom.android.common.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by clocksmith on 8/24/16.
 */
public class CreatorPreferences {
  private static final String TAG = CreatorPreferences.class.getSimpleName();
  private static final String BUILD_NAMESPACE = "namespace$creatorPreferences";

  private static final String CYCLE_KEY = "cycle";

  private static SharedPreferences getPreferences(Context context) {
    return context.getSharedPreferences(BUILD_NAMESPACE, Context.MODE_PRIVATE);
  }

  public static void toggleCycleOn(Context context) {
    getPreferences(context).edit().putBoolean(CYCLE_KEY, true).commit();
  }

  public static void toggleCycleOff(Context context) {
    getPreferences(context).edit().putBoolean(CYCLE_KEY, false).commit();
  }

  public static boolean isCycle(Context context) {
    return getPreferences(context).getBoolean(CYCLE_KEY, false);
  }
}
