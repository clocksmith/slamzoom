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
public class LocalWebViewDialogPresenter {
  private static final String TAG = LocalWebViewDialogPresenter.class.getSimpleName();

  private static void showAlertWithView(Activity activity, View view) {
    final AlertDialog.Builder alert =
        new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.SlamzoomDialog));
    alert.setView(view);
    alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int id) {
        dialog.dismiss();
      }
    });
    alert.show();
  }

  public static void showLocalHtmlFromAssets(Activity activity, String assetName) {
    WebView webView = new WebView(activity);
    webView.loadUrl(UriUtils.getUrlStringFromAssets(assetName));
    showAlertWithView(activity, webView);
  }

  public static void showUrl(Activity activity, String url) {
    WebView webView = new WebView(activity);
    webView.loadUrl(url);
    showAlertWithView(activity, webView);
  }
}

