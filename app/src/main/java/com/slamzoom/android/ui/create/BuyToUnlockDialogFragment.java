package com.slamzoom.android.ui.create;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.common.collect.Lists;
import com.slamzoom.android.R;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.bus.BusProvider;
import com.slamzoom.android.common.utils.SzLog;
import com.slamzoom.android.ui.create.effectchooser.EffectModel;

import java.util.List;

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

    List<EffectModel> effectModels = Lists.newArrayList();
    Activity activity = getActivity();
    if (activity instanceof CreateActivity) {
      effectModels = ((CreateActivity) activity).getEffectModelsForPack(packName);
    } else {
      SzLog.e(TAG, "Parent activity is not an instance of CreateActivity");
    }

    BuyToUnlockDialogView contentView = new BuyToUnlockDialogView(
        this.getContext(), effectName, packName, effectModels);
    contentView.setLayoutParams(
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

    Dialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.SlamzoomDialog))
        .setIcon(R.drawable.ic_gfx_lock)
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
    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
    lp.dimAmount = 0.3f;
    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.black_22)));

    return dialog;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }
}
