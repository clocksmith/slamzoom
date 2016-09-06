package com.slamzoom.android.common.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by clocksmith on 9/5/16.
 */
public class UiUtils {
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  public static void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener){
    if (Build.VERSION.SDK_INT < 16) {
      v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
    } else {
      v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
    }
  }

  public static void onGlobalLayout(final View view, final ViewTreeObserver.OnGlobalLayoutListener listener) {
    view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        removeOnGlobalLayoutListener(view, this);
        listener.onGlobalLayout();
      }
    });
  }
}
