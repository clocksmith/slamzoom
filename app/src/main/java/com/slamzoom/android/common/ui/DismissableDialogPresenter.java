package com.slamzoom.android.common.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.webkit.WebView;

import com.slamzoom.android.R;
import com.slamzoom.android.common.data.UriUtils;

/**
 * Created by clocksmith on 9/17/16.
 */
public class DismissableDialogPresenter {
  public static void show(Activity activity, String title, View view) {
    new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.SlamzoomDialog))
        .setTitle(title)
        .setView(view)
        .setPositiveButton(activity.getString(R.string.dismissable_dialog_ok), new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int id) {
            dialog.dismiss();
          }
        })
        .show();
  }
}

