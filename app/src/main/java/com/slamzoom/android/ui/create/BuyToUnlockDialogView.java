package com.slamzoom.android.ui.create;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slamzoom.android.R;
import com.slamzoom.android.common.utils.SzLog;
import com.slamzoom.android.effects.EffectPack;
import com.slamzoom.android.effects.EffectPacks;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.EffectTemplates;
import com.slamzoom.android.ui.create.effectchooser.EffectChooser;
import com.slamzoom.android.ui.create.effectchooser.EffectModel;
import com.slamzoom.android.ui.create.effectchooser.EffectThumbnailRecyclerViewAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by clocksmith on 7/20/16.
 */
public class BuyToUnlockDialogView extends LinearLayout {
  public static final String TAG = BuyToUnlockDialogView.class.getSimpleName();

  private static final int GRID_SPAN_COUNT = 3;

  @Bind(R.id.message) TextView mMessage;
  @Bind(R.id.otherEffects) EffectChooser mOtherEffects;

  private EffectThumbnailRecyclerViewAdapter mAdapter;

  public BuyToUnlockDialogView(Context context, String effectName, String packName, List<EffectModel> effectModes) {
    this(context, null);

    EffectPack pack = EffectPacks.getPack(packName);
    EffectTemplate effect = EffectTemplates.get(effectName);

    if (pack == null) {
      SzLog.e(TAG, "effectPack: " + packName + " is null!");
    } else if (effect == null) {
      SzLog.e(TAG, "effectName: " + effectName + " is null!");
    } else {
      String message = String.format(getResources().getString(R.string.buy_dialog_text), packName, effectName);
      Spannable messageSpannable = new SpannableString(message);
      int packStart = message.indexOf(packName);
      int packEnd = packStart + packName.length();
      messageSpannable.setSpan(
          new ForegroundColorSpan(pack.getColor()),
          packStart,
          packEnd,
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      int effectStart = message.indexOf(effectName);
      int effectEnd = effectStart + effectName.length();
      messageSpannable.setSpan(
          new ForegroundColorSpan(effect.getColor()),
          effectStart,
          effectEnd,
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      mMessage.setText(messageSpannable);

//      StringBuilder sb = new StringBuilder();
//      for (EffectTemplate otherEffect : pack.getEffectTemplates()) {
//        if (!otherEffect.getName().equals(effectName)) {
//          sb.append(otherEffect.getName().toUpperCase());
//          sb.append("\n");
//        }
//      }
//      mOtherEffects.setText(sb);

      mOtherEffects.init(effectModes, false);
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
