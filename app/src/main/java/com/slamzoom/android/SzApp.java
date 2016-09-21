package com.slamzoom.android;

import android.app.Application;
import android.content.Context;

import com.slamzoom.android.common.FontProvider;
import com.slamzoom.android.effects.Effects;

/**
 * Created by clocksmith on 6/17/16.
 */
public class SzApp extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    // Init the packs.
    Effects.init(this.getApplicationContext());

    // Init the fonts.
    FontProvider.getInstance().init(getAssets());

    CONTEXT = this.getApplicationContext();
    PACKAGE_NAME = this.getPackageName();
  }

  public static Context CONTEXT;
  public static String PACKAGE_NAME;
}
