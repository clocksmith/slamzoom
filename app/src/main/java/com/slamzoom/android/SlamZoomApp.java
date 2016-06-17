package com.slamzoom.android;

import android.app.Application;

import com.slamzoom.android.ui.create.effectchooser.EffectColors;

/**
 * Created by clocksmith on 6/17/16.
 */
public class SlamZoomApp extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    EffectColors.init(this.getApplicationContext());
  }
}
