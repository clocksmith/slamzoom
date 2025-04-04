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
import com.slamzoom.android.common.intents.Params;

/**
 * Created by clocksmith on 7/16/16.
 */
public class UnlockPackDialogFragment extends DialogFragment {
  private static final String TAG = UnlockPackDialogFragment.class.getSimpleName();

  private String mEffectName;
  private String mPackName;
  private UnlockPackDialogView mContentView;

  public static UnlockPackDialogFragment newInstance(String effectName, String packName) {
    UnlockPackDialogFragment fragment = new UnlockPackDialogFragment();

    Bundle args = new Bundle();
    args.putString(Params.EFFECT_NAME, effectName);
    args.putString(Params.PACK_NAME, packName);
    fragment.setArguments(args);

    return fragment;
  }

  @Override
  public @NonNull Dialog onCreateDialog(Bundle savedInstanceState) {
    final String effectName = getArguments().getString(Params.EFFECT_NAME);
    final String packName = getArguments().getString(Params.PACK_NAME);
    mEffectName = effectName;
    mPackName = packName;

    mContentView = new UnlockPackDialogView(getContext(), effectName, packName);
    mContentView.setMinimumHeight(getResources().getDimensionPixelOffset(R.dimen.unlock_pack_dialog_effect_height));

    Drawable icon = ContextCompat.getDrawable(getActivity(), R.drawable.sz_ic_dialog_lock);
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
