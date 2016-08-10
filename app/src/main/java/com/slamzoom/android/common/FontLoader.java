package com.slamzoom.android.common;

import android.content.res.AssetManager;
import android.graphics.Typeface;

/**
 * Created by clocksmith on 8/3/16.
 */
public class FontLoader {
  private static final String PATH_PREFIX = "fonts/";

  private static final String OSWALD_REGULAR = PATH_PREFIX + "Oswald-Regular.ttf";
  private static final String LATO_REGULAR = PATH_PREFIX + "Lato-Regular.ttf";

  private volatile static FontLoader instance;

  private Typeface mOswaldRegular;
  private Typeface mLatoRegular;

  public static FontLoader getInstance() {
    if (instance == null) {
      synchronized (FontLoader.class) {
        if (instance == null) {
          instance = new FontLoader();
        }
      }
    }
    return instance;
  }

  public synchronized void init(AssetManager assetsManager) {
    mOswaldRegular = Typeface.createFromAsset(assetsManager, OSWALD_REGULAR);
    mLatoRegular = Typeface.createFromAsset(assetsManager, LATO_REGULAR);
  }

  public Typeface getOswaldRegular() {
    return mOswaldRegular;
  }

  public Typeface getLatoRegular() {
    return mLatoRegular;
  }

  public Typeface getTitleFont() {
    return getOswaldRegular();
  }

  public Typeface getDefaultFont() {
    return getLatoRegular();
  }
}