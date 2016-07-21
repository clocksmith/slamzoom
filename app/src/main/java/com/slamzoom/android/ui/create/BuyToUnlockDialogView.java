package com.slamzoom.android.ui.create;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slamzoom.android.R;
import com.slamzoom.android.common.utils.SzLog;
import com.slamzoom.android.effects.EffectPack;
import com.slamzoom.android.effects.EffectPacks;
import com.slamzoom.android.effects.EffectTemplate;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by clocksmith on 7/20/16.
 */
public class BuyToUnlockDialogView extends LinearLayout {
  public static final String TAG = BuyToUnlockDialogView.class.getSimpleName();

  @Bind(R.id.message) TextView mMessage;
  @Bind(R.id.otherEffects) TextView mOtherEffects;

  public BuyToUnlockDialogView(Context context, String effectName, String packName) {
    this(context, null);

    mMessage.setText(String.format(getResources().getString(R.string.buy_dialog_text), packName, effectName.toUpperCase()));

    EffectPack effectPack = EffectPacks.getPack(packName);
    if (effectPack != null) {
      StringBuilder sb = new StringBuilder();
      for (EffectTemplate otherEffect : effectPack.getEffectTemplates()) {
        sb.append(otherEffect.getName().toUpperCase() );
        sb.append("\n");
      }
      mOtherEffects.setText(sb);
    } else {
      SzLog.e(TAG, "effectPack: " + packName + " is null!");
    }
  }

  public BuyToUnlockDialogView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BuyToUnlockDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    LayoutInflater.from(context).inflate(R.layout.view_buy_to_unlock_dialog, this);
    ButterKnife.bind(this);
  }


}
