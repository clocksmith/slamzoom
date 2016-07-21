package com.slamzoom.android.ui.create;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.slamzoom.android.R;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.bus.BusProvider;
import com.squareup.otto.Bus;

/**
 * Created by clocksmith on 7/16/16.
 */
public class BuyToUnlockDialogFragment extends DialogFragment {
  public class OnBuyClickedEvent {
    public String packName;
    public OnBuyClickedEvent(String packName) {
      this.packName = packName;
    }
  }

  public class OnCancelClickedEvent {}

  public static BuyToUnlockDialogFragment newInstance(String effectName, String packName) {
    BuyToUnlockDialogFragment f = new BuyToUnlockDialogFragment();

    Bundle args = new Bundle();
    args.putString(Constants.EFFECT_NAME, effectName);
    args.putString(Constants.PACK_NAME, packName);
    f.setArguments(args);

    return f;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final String effectName = getArguments().getString(Constants.EFFECT_NAME);
    final String packName = getArguments().getString(Constants.PACK_NAME);

    BuyToUnlockDialogView contentView = new BuyToUnlockDialogView(this.getContext(), effectName, packName);
    contentView.setLayoutParams(
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

    return new AlertDialog.Builder(getActivity())
        .setIcon(R.drawable.ic_lock)
        .setTitle(R.string.buy_dialog_title)
        .setView(contentView)
        .setPositiveButton(R.string.buy_ok,
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int whichButton) {
                BusProvider.getInstance().post(new OnBuyClickedEvent(packName));
              }
            }
        )
        .setNegativeButton(R.string.buy_cancel,
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int whichButton) {
                BusProvider.getInstance().post(new OnCancelClickedEvent());
              }
            }
        )
        .create();

//    return new AlertDialog.Builder(getActivity())
//        .setIcon(R.drawable.ic_lock)
//        .setTitle(R.string.buy_dialog_title)
//        .setMessage(R.string.buy_dialog_text)
//        .setPositiveButton(R.string.buy_ok,
//            new DialogInterface.OnClickListener() {
//              public void onClick(DialogInterface dialog, int whichButton) {
//                BusProvider.getInstance().post(new OnBuyClickedEvent(packName));
//              }
//            }
//        )
//        .setNegativeButton(R.string.buy_cancel,
//            new DialogInterface.OnClickListener() {
//              public void onClick(DialogInterface dialog, int whichButton) {
//                BusProvider.getInstance().post(new OnCancelClickedEvent());
//              }
//            }
//        )
//        .create();
  }
}
