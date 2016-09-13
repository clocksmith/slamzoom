package com.slamzoom.android.ui.create;

import android.app.Dialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.WindowManager;

import com.slamzoom.android.R;
import com.slamzoom.android.common.Constants;

/**
 * Created by clocksmith on 7/16/16.
 */
public class UnlockPackDialogFragment extends DialogFragment {
  private static final String TAG = UnlockPackDialogFragment.class.getSimpleName();

//  public class OnBuyClickedEvent {
//    public String packName;
//    public OnBuyClickedEvent(String packName) {
//      this.packName = packName;
//    }
//  }
//
//  public class OnCancelClickedEvent {}

  private String mEffectName;
  private String mPackName;
  private BuyToUnlockDialogView mContentView;

  public static UnlockPackDialogFragment newInstance(String effectName, String packName) {
    UnlockPackDialogFragment f = new UnlockPackDialogFragment();

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
    mContentView.setMinimumHeight(getResources().getDimensionPixelOffset(R.dimen.unlock_pack_dialog_effect_height));
//    mContentView.setLayoutParams(new LinearLayout.LayoutParams(
//        ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.buy_dialog_effect_height)));

    Drawable icon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_gfx_buy_dialog_lock);
    icon.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
    Dialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.SlamzoomDialog))
        .setIcon(icon)
        .setTitle(R.string.unlock_pack_dialog_title)
        .setView(mContentView)
        .create();

    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    return dialog;
  }
}
