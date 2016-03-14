package com.slamzoom.android;

import android.app.Application;
import android.content.Context;

/**
 * Created by clocksmith on 3/8/16.
 */
public class SlamzoomApplication extends Application {

  private static Context context;

  public void onCreate() {
    super.onCreate();
    SlamzoomApplication.context = getApplicationContext();
  }

  public static Context getAppContext() {
    return SlamzoomApplication.context;
  }
}
