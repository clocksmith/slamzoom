package com.slamzoom.android.common.settings;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by clocksmith on 8/24/16.
 */
public class Preferences {
  private static final String NAME = "preferences";
  private static final String FIRST_HOTSPOT_OPEN = "firstHotspotOpen";

  private static SharedPreferences getPreferences(Context context) {
    return context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
  }

  public static void setFirstHotspotOpen(Context context, boolean firstHotspotOpen) {
    getPreferences(context).edit().putBoolean(FIRST_HOTSPOT_OPEN, firstHotspotOpen).commit();
  }

  public static boolean isFirstHotspotOpen(Context context) {
    return getPreferences(context).getBoolean(FIRST_HOTSPOT_OPEN, true);
  }
}
