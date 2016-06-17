package com.slamzoom.android.ui.create.effectchooser;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

/**
 * Created by clocksmith on 6/14/16.
 */
public class EffectColors {
  private static final ImmutableList<String> SUFFIXES = ImmutableList.of(
      "900",
      "800",
      "700",
      "600",
      "500",
      "400",
      "300",
      "200",
      "100",
      "50");

  private static ImmutableList<Integer> mColors;
  private static Context mContext;

  public static void init(Context context) {
    mContext = context;
    mColors = ImmutableList.<Integer>builder()
        .addAll(getColorGroup("green"))
        .addAll(getColorGroup("blue"))
        .addAll(getColorGroup("purple"))
        .addAll(getColorGroup("red"))
        .addAll(getColorGroup("orange"))
        .build();
  }

  private static ImmutableList<Integer> getColorGroup(final String color) {
    return ImmutableList.copyOf(Lists.transform(SUFFIXES, new Function<String, Integer>() {
      @Override
      public Integer apply(String input) {
        return getColor(color, input);
      }
    }));
  }

  private static int getColor(String color, String suffix) {
    return ContextCompat.getColor(mContext, mContext.getResources().getIdentifier(
        "md_" + color + suffix,
        "color",
        mContext.getPackageName()));
  }

  public static ImmutableList<Integer> list() {
    return mColors;
  }
}
