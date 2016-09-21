package com.slamzoom.android.ui.create;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.slamzoom.android.R;
import com.slamzoom.android.common.FontProvider;
import com.slamzoom.android.common.logging.SzLog;
import com.slamzoom.android.common.BusProvider;
import com.slamzoom.android.common.ui.AnimationUtils;
import com.slamzoom.android.effects.EffectColors;
import com.slamzoom.android.effects.packs.EffectPack;
import com.slamzoom.android.effects.Effects;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.ui.create.effectchooser.EffectChooser;
import com.slamzoom.android.ui.create.effectchooser.EffectModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clocksmith on 7/20/16.
 */
public class BuyToUnlockDialogView extends LinearLayout {
  public static final String TAG = BuyToUnlockDialogView.class.getSimpleName();

  public class OnBuyClickedEvent {
    public String packName;
    public OnBuyClickedEvent(String packName) {
      this.packName = packName;
    }
  }

  public class OnCancelClickedEvent {}

  @BindView(R.id.message) TextView mMessage;
  @BindView(R.id.otherEffects) EffectChooser mOtherEffects;
  @BindView(R.id.cancelButton) Button mCancelButton;
  @BindView(R.id.okButton) Button mOkButton;

  public BuyToUnlockDialogView(Context context, final String effectName, final String packName) {
    this(context, null);

    EffectPack pack = Effects.getPack(packName);
    EffectTemplate effect = Effects.getEffectTemplate(effectName);

    mMessage.setTypeface(FontProvider.getInstance().getDefaultFont());

    if (pack == null) {
      SzLog.e(TAG, "effectPack: " + packName + " is null!");
    } else if (effect == null) {
      SzLog.e(TAG, "effectName: " + effectName + " is null!");
    } else {
      String packNameWithPack = packName + " PACK  ";
      String paddedEffectName = "  " + effectName + "  ";
      String message = String.format(
          getResources().getString(R.string.unlock_pack_dialog_message), packNameWithPack, paddedEffectName);
      Spannable messageSpannable = new SpannableString(message);

      int packStart = message.indexOf(packNameWithPack);
      int packEnd = packStart + packNameWithPack.length();
      messageSpannable.setSpan(
          new StyleSpan(android.graphics.Typeface.BOLD),
          packStart,
          packEnd,
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      messageSpannable.setSpan(
          new CustomTypefaceSpan("sans-serif", FontProvider.getInstance().getTitleFont()),
          packStart,
          packEnd,
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

      int effectStart = message.indexOf(paddedEffectName);
      int effectEnd = effectStart + paddedEffectName.length();
      messageSpannable.setSpan(
          new ForegroundColorSpan(ContextCompat.getColor(context, R.color.effect_text)),
          effectStart,
          effectEnd,
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      messageSpannable.setSpan(
          new BackgroundColorSpan(effect.getColor()),
          effectStart,
          effectEnd,
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

      mMessage.setText(messageSpannable);

      mOkButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          BusProvider.getInstance().post(new OnBuyClickedEvent(packName));
        }
      });

      mCancelButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          BusProvider.getInstance().post(new OnCancelClickedEvent());
        }
      });

//      mOkButton.setBackgroundColor(effect.getColor());
//      UiUtils.onGlobalLayout(mOkButton, new ViewTreeObserver.OnGlobalLayoutListener() {
//        @Override
//        public void onGlobalLayout() {
//          mOkButton.setPivotX(mOkButton.getWidth() / 2);
//          mOkButton.setPivotY(mOkButton.getHeight() / 2);
//          AnimationUtils.getUniformScaleSet(mOkButton, 1.2f, 1000).start();
//          rotateColorsOkButton(0);
//        }
//      });
    }

    List<EffectModel> effectModelsForPack = FluentIterable.from(Effects.createEffectModels())
        .filter(new Predicate<EffectModel>() {
          @Override
          public boolean apply(EffectModel input) {
            return input.getEffectTemplate().getPackName().equals(packName);
          }
        }).toList();
    int selectedPosition = -1;
    for (int position = 0; position < effectModelsForPack.size(); position++) {
      EffectModel effectModel = effectModelsForPack.get(position);
      effectModel.setLocked(false);
      if (effectModel.getEffectTemplate().getName().equals(effectName)) {
        selectedPosition = position;
      }
    }
    mOtherEffects.initForDialog(effectModelsForPack, selectedPosition);
  }

  public BuyToUnlockDialogView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BuyToUnlockDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    LayoutInflater.from(context).inflate(R.layout.view_buy_to_unlock_dialog, this);
    ButterKnife.bind(this);
  }

  private void pulseOkButton() {
    mOkButton.setPivotX(mOkButton.getWidth() / 2);
    mOkButton.setPivotY(mOkButton.getHeight() / 2);
    AnimationUtils.getPulseForeverSet(mOkButton, 1.2f, 500).start();
  }

  private void rotateColorsOkButton(final int index) {
    final List<Integer> colors = EffectColors.getColorGroup("darkerRainbow");
    AnimatorSet set = AnimationUtils.getChangeButtonBackgroundColorSet(mOkButton, colors.get(index), 500);
    set.addListener(new AnimationUtils.OnAnimationEndOnlyListener() {
      @Override
      public void onAnimationEnd(Animator animation) {
        rotateColorsOkButton((index + 1) % colors.size());
      }
    });
    set.start();
  }

  // TODO(clocksmith): extract
  private static void applyCustomTypeFace(Paint paint, Typeface tf) {
    int oldStyle;
    Typeface old = paint.getTypeface();
    if (old == null) {
      oldStyle = 0;
    } else {
      oldStyle = old.getStyle();
    }

    int fake = oldStyle & ~tf.getStyle();
    if ((fake & Typeface.BOLD) != 0) {
      paint.setFakeBoldText(true);
    }

    if ((fake & Typeface.ITALIC) != 0) {
      paint.setTextSkewX(-0.25f);
    }

    paint.setTypeface(tf);
  }

  private class CustomTypefaceSpan extends TypefaceSpan {
    private final Typeface newType;

    public CustomTypefaceSpan(String family, Typeface type) {
      super(family);
      newType = type;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
      applyCustomTypeFace(ds, newType);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
      applyCustomTypeFace(paint, newType);
    }
  }
}
