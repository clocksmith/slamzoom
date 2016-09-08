package com.slamzoom.android.common.utils;

/**
 * Created by clocksmith on 9/6/16.
 */
public class MathUtils {
  public static int roundToEvenNumber(float number) {
    return roundToEvenNumber(Math.round(number));
  }

  public static int roundToEvenNumber(int number) {
    return number / 2 * 2;
  }
}
