package com.slamzoom.android.ui.create;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.slamzoom.android.R;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.bus.BusProvider;

/**
 * Created by clocksmith on 7/16/16.
 */
public class BuyToUnlockDialogFragment extends DialogFragment {
  private static final String TAG = BuyToUnlockDialogFragment.class.getSimpleName();

  public class OnBuyClickedEvent {
    public String packName;
    public OnBuyClickedEvent(String packName) {
      this.packName = packName;
    }
  }

  public class OnCancelClickedEvent {}

  private String mEffectName;
  private String mPackName;
  private BuyToUnlockDialogView mContentView;

  public static BuyToUnlockDialogFragment newInstance(String effectName, String packName) {
    BuyToUnlockDialogFragment f = new BuyToUnlockDialogFragment();

    Bundle args = new Bundle();
    args.putString(Constants.EFFECT_NAME, effectName);
    args.putString(Constants.PACK_NAME, packName);
    f.setArguments(args);

    return f;
  }

  @Override
  public @NonNull Dialog onCreateDialog(Bundle savedInstanceState) {
    final String effectName = getArguments().getString(Constants.EFFECT_NAME);
    final String packName = getArguments().getString(Constants.PACK_NAME);
    mEffectName = effectName;
    mPackName = packName;

    mContentView = new BuyToUnlockDialogView(getContext(), effectName, packName);
    mContentView.setMinimumHeight(getResources().getDimensionPixelOffset(R.dimen.buy_dialog_effect_height));
    mContentView.setLayoutParams(new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.buy_dialog_effect_height)));

    Drawable icon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_gfx_buy_dialog_lock);
    icon.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
    Dialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.SlamzoomDialog))
        .setIcon(icon)
        .setTitle(R.string.buy_dialog_title)
        .setView(mContentView)
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

    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    return dialog;
  }
}
