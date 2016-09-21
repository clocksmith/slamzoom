package com.slamzoom.android.common.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.webkit.WebView;

import com.slamzoom.android.R;
import com.slamzoom.android.common.data.UriUtils;

/**
 * Created by clocksmith on 9/17/16.
 */
public class LocalWebViewDialogPresenter {
  private static final String TAG = LocalWebViewDialogPresenter.class.getSimpleName();

  public static void show(Activity activity, String assetName) {

    final AlertDialog.Builder alert =
        new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.SlamzoomDialog));

    WebView webView = new WebView(activity);
    webView.loadUrl(UriUtils.getUrlStringFromAssets(assetName));

    alert.setView(webView);
    alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int id) {
        dialog.dismiss();
      }
    });

    alert.show();
  }
}

