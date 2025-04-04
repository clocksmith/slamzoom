package com.slamzoom.android.common.ui;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.slamzoom.android.effects.EffectColors;

/**
 * Created by clocksmith on 9/13/16.
 */
public class SnackbarPresenter {
  private static Snackbar makeShortSnackbar(CoordinatorLayout coordinatorLayout, String message) {
    return Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
  }

  public static void showErrorMessage(CoordinatorLayout coordinatorLayout, String message) {
    Snackbar snackbar = makeShortSnackbar(coordinatorLayout, message);
    snackbar.getView().setBackgroundColor(EffectColors.getDarkerRed());
    snackbar.show();
  }

  public static void showSuccessMessage(CoordinatorLayout coordinatorLayout, String message) {
    Snackbar snackbar = makeShortSnackbar(coordinatorLayout, message);
    snackbar.getView().setBackgroundColor(EffectColors.getDarkerGreen());
    snackbar.show();
  }
}
