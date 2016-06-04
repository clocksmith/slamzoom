package com.slamzoom.android.common.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by clocksmith on 3/30/16.
 */
public class KeyboardUtils {
  /**
   * Hide the keyboard that is attached to the view in focus in the activity.
   */
  public static void hideKeyboard(Activity activity) {
    if (activity.getCurrentFocus() != null) {
      InputMethodManager inputMethodManager =
          (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
  }

  /**
   * Force show the keyboard and attach it to the view in focus in the activity.
   */
  public static void showKeyboard(Activity activity) {
    if (activity.getCurrentFocus() != null) {
      InputMethodManager inputMethodManager =
          (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
      inputMethodManager.showSoftInput(activity.getCurrentFocus(), InputMethodManager.SHOW_FORCED);
    }
  }
}
