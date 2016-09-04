package com.slamzoom.android.ui.create;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.WindowManager;

import com.slamzoom.android.R;

/**
 * Created by clocksmith on 9/4/16.
 */
public class PermissionsWarningDialogFragment extends DialogFragment {
  public static PermissionsWarningDialogFragment newInstance() {
    return new PermissionsWarningDialogFragment();
  }

  @Override
  @NonNull
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.SlamzoomDialog))
        .setIcon(R.drawable.ic_gfx_dialog_warning)
        .setTitle(R.string.permissions_warning_dialog_title)
        .setMessage(R.string.permissions_warning_dialog_message)
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            dialog.dismiss();
          }
        }).create();

    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    return dialog;

  }
}