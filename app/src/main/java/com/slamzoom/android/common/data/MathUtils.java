package com.slamzoom.android.common.data;

import java.util.List;
import java.util.Random;

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

  public static <E> E getRandomElement(List<E> list) {
    return list.get((new Random()).nextInt(list.size()));
  }
}
