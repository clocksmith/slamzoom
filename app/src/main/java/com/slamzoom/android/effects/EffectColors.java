package com.slamzoom.android.effects;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by clocksmith on 6/14/16.
 */
public class EffectColors {
  private static final ImmutableList<String> SUFFIXES_AS_RESOUCRES = ImmutableList.of(
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

  private static final ImmutableList<String> SUFFIXES = ImmutableList.of(
      "900",
      "850",
      "800",
      "750",
      "700",
      "650",
      "600",
      "550",
      "500",
      "450");

  private static Map<String, ImmutableList<Integer>> mColorGroups = Maps.newHashMap();
  private static Context mContext;

  public static void init(Context context) {
    mContext = context;
  }

  public static ImmutableList<Integer> getColorGroup(final String colorGroup) {
    if (!mColorGroups.containsKey(colorGroup)) {
      mColorGroups.put(colorGroup, ImmutableList.copyOf(Lists.transform(SUFFIXES, new Function<String, Integer>() {
        @Override
        public Integer apply(String input) {
          return getColor(colorGroup, input);
        }
      })));
    }
    return mColorGroups.get(colorGroup);
  }

  private static int getColor(String color, String suffix) {
    if (SUFFIXES_AS_RESOUCRES.contains(suffix)) {
      return ContextCompat.getColor(mContext, mContext.getResources().getIdentifier(
          "md_" + color + suffix,
          "color",
          mContext.getPackageName()));
    } else {
      return getInterpolatedColor(color, suffix);
    }
  }

  private static int getInterpolatedColor(String color, String suffix) {
    String lowerResSuffix = suffix.charAt(0) + "00";
    String upperResSuffix = (Integer.parseInt(String.valueOf(suffix.charAt(0))) + 1) + "00";
    int lowerColor = ContextCompat.getColor(mContext, mContext.getResources().getIdentifier(
        "md_" + color + lowerResSuffix,
        "color",
        mContext.getPackageName()));
    int upperColor = ContextCompat.getColor(mContext, mContext.getResources().getIdentifier(
        "md_" + color + upperResSuffix,
        "color",
        mContext.getPackageName()));
    float percent = Float.parseFloat(suffix.substring(1)) / 100;
    return (int) (new ArgbEvaluator().evaluate(percent, lowerColor, upperColor));
  }
}
