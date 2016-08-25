package com.slamzoom.android.effects;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.util.Log;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Created by clocksmith on 6/14/16.
 */
public class EffectColors {
  private static final int NUM_COLORS_IN_GROUP = 10;

  private static final ImmutableList<Integer> SUFFIXES_AS_RESOUCRES = ImmutableList.of(
      900,
      800,
      700,
      600,
      500,
      400,
      300,
      200,
      100); // excluding 50, too light

  private static Map<String, ImmutableList<Integer>> mColorGroups = Maps.newHashMap();
  private static Context mContext;

  public static void init(Context context) {
    mContext = context;
  }

  private static ImmutableList<Integer> getRainbowColorGroup() {
    return ImmutableList.of(
        Color.rgb(255, 201, 94),
        Color.rgb(191, 255, 94),
        Color.rgb(0, 255, 204),
        Color.rgb(56, 209, 255),
        Color.rgb(223, 94, 255),
        Color.rgb(255, 102, 207),
        Color.rgb(255, 94, 94),
        Color.rgb(255, 119, 0));
  }

  public static ImmutableList<Integer> getColorGroup(final String colorGroup) {
    // TODO(clocksmith): extract this and get rid of the other stuff if we decide on this scheme.
    if (colorGroup.equals("rainbow")) {
      return ImmutableList.copyOf(Lists.transform(getRainbowColorGroup(), new Function<Integer, Integer>() {
        @Override
        public Integer apply(Integer color) {
          float[] hsv = new float[3];
          Color.colorToHSV(color, hsv);
          hsv[2] *= 0.75f; // value component
          return Color.HSVToColor(hsv);
        }
      }));
    }

    String[] frags = colorGroup.split("_");
    int start = Integer.parseInt(frags[1]);
    int end = Integer.parseInt(frags[2]);
    int range = end - start;
    double inc = ((double) range) / NUM_COLORS_IN_GROUP;
    if (!mColorGroups.containsKey(colorGroup)) {
      List<Integer> colors = Lists.newArrayList();
      for (int i = 0; i < NUM_COLORS_IN_GROUP; i++) {
        colors.add(getColor(frags[0], (int) Math.round(start + inc * i)));
      }
      mColorGroups.put(colorGroup, ImmutableList.copyOf(colors));
    }
    return mColorGroups.get(colorGroup);
  }

  private static int getColor(String color, int value) {
    if (SUFFIXES_AS_RESOUCRES.contains(value)) {
      return ContextCompat.getColor(mContext, mContext.getResources().getIdentifier(
          "md_" + color + value,
          "color",
          mContext.getPackageName()));
    } else {
      return getInterpolatedColor(color, value);
    }
  }

  private static int getInterpolatedColor(String color, int value) {
    // Dependent on all res being ordered multiple of 100
    int lowerResSuffix = value / 100 * 100;
    int upperResSuffix = lowerResSuffix + 100;

    int lowerColor = ContextCompat.getColor(mContext, mContext.getResources().getIdentifier(
        "md_" + color + lowerResSuffix,
        "color",
        mContext.getPackageName()));
    int upperColor = ContextCompat.getColor(mContext, mContext.getResources().getIdentifier(
        "md_" + color + upperResSuffix,
        "color",
        mContext.getPackageName()));
    float percent = (value - lowerResSuffix) / 100f;
    return (int) (new ArgbEvaluator().evaluate(percent, lowerColor, upperColor));
  }
}
