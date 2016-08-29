package com.slamzoom.android;

import android.app.Application;
import android.content.Context;

import com.slamzoom.android.common.FontLoader;
import com.slamzoom.android.effects.EffectPacks;

/**
 * Created by clocksmith on 6/17/16.
 */
public class SzApp extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    // Init the packs.
    EffectPacks.init(this.getApplicationContext());

    // Init the fonts.
    FontLoader.getInstance().init(getAssets());

    context = this.getApplicationContext();
  }

  public static Context context;
}
