package com.slamzoom.android.common.fonts;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by clocksmith on 8/3/16.
 */
public class FontProvider {
  private static final String PATH_PREFIX = "fonts/";

  private static final Map<String, Typeface> FONTS = Maps.newHashMap();
  private static final ImmutableList<String> FONT_NAMES = ImmutableList.of(
      "Lato-Regular.ttf",
      "Quicksand-Bold.ttf"
  );

  private volatile static FontProvider instance;

  public static FontProvider getInstance() {
    if (instance == null) {
      synchronized (FontProvider.class) {
        if (instance == null) {
          instance = new FontProvider();
        }
      }
    }
    return instance;
  }

  public synchronized void init(AssetManager assetsManager) {
    for (String fontName : FONT_NAMES) {
      FONTS.put(fontName, Typeface.createFromAsset(assetsManager, PATH_PREFIX + fontName));
    }
  }

  public Typeface getTitleFont() {
    return FONTS.get("Quicksand-Bold.ttf");
  }

  public Typeface getDefaultFont() {
    return FONTS.get("Lato-Regular.ttf");
  }
}