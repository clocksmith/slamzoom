package com.slamzoom.android.ui.create.effectchooser;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.slamzoom.android.R;
import com.slamzoom.android.common.FontProvider;
import com.slamzoom.android.common.BusProvider;
import com.slamzoom.android.common.ui.AnimationUtils;
import com.slamzoom.android.common.logging.SzAnalytics;
import com.slamzoom.android.common.logging.SzLog;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by clocksmith on 2/27/16.
 */
public class EffectThumbnailViewHolder extends RecyclerView.ViewHolder {
  private static final String TAG = EffectThumbnailViewHolder.class.getSimpleName();

  public class ItemClickEvent {
    public final String effectName;
    public final int position;
    public ItemClickEvent(String effectName, int position) {
      this.effectName = effectName;
      this.position = position;
    }
  }

  public class RequestThumbnailGifEvent {
    public final String effectName;
    public RequestThumbnailGifEvent(String effectName) {
      this.effectName = effectName;
    }
  }

  public class RequestStopThumbnailGifGenerationEvent {
    public final String effectName;
    public RequestStopThumbnailGifGenerationEvent(String effectName) {
      this.effectName = effectName;
    }
  }

  @BindView(R.id.lockIcon) ImageView mLockIcon;
  @BindView(R.id.gifImageView) GifImageView mGifImageView;
  @BindView(R.id.progressBar) ProgressBar mProgressBar;
  @BindView(R.id.nameTextView) TextView mNameTextView;
  @BindView(R.id.packNameTextView) TextView mPackNameTextView;
  @BindView(R.id.tabView) View mTabView;

  private EffectModel mModel;
  @ColorInt private int mTabColorInt;

  private int mCollpasedTabHeight;
  private int mExpandedTabHeight;
  private boolean mTabExpanded;

  public EffectThumbnailViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
    BusProvider.getInstance().register(this);

    mCollpasedTabHeight =
        itemView.getContext().getResources().getDimensionPixelSize(R.dimen.effect_chooser_tab_height_collapsed);
    mExpandedTabHeight =
        itemView.getContext().getResources().getDimensionPixelSize(R.dimen.effect_chooser_tab_height_expanded);
    mTabExpanded = false;

    mNameTextView.setTypeface(FontProvider.getInstance().getDefaultFont());
    mPackNameTextView.setTypeface(FontProvider.getInstance().getTitleFont());
  }

  public void rebind(final EffectModel model, boolean inDialog) {
    unbind();
    bind(model, inDialog);
  }

  public void bind(final EffectModel model, boolean inDialog) {
    SzLog.f(TAG, "bind: " + getAdapterPosition());
    mModel = model;

    final @ColorInt int newTabColorInt = model.isLocked() ?
        Color.rgb(128, 128, 128) : model.getEffectTemplate().getColor();
    final @ColorInt int effectTextColorInt =  ContextCompat.getColor(itemView.getContext(), inDialog ?
        R.color.buy_dialog_effect_text : R.color.effect_text);
    final String name = mModel.getEffectTemplate().getName();
    final String packName = mModel.getEffectTemplate().getPackName().toLowerCase();

    mNameTextView.setText(name);
    //noinspection ResourceAsColor
    mNameTextView.setTextColor(model.isSelected() ?
        ContextCompat.getColor(itemView.getContext(), R.color.effect_text) : effectTextColorInt);

//    mPackNameTextView.setVisibility(inDialog ? View.GONE : View.VISIBLE);
    mPackNameTextView.setText(packName);
    mPackNameTextView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.effect_text));

    mLockIcon.setVisibility(model.isLocked() && !inDialog ? View.VISIBLE : View.GONE);

    if (newTabColorInt != mTabColorInt) {
      mTabColorInt = newTabColorInt;
      mTabView.setBackgroundColor(mTabColorInt);
      mProgressBar.getIndeterminateDrawable().setColorFilter(mTabColorInt, android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    setSelected(model.isSelected());

    // Either show the gif or request it.
    if (model.getGifThumbnailBytes() != null && model.getGifThumbnailBytes().length > 0) {
      try {
        mProgressBar.setVisibility(View.GONE);
        mGifImageView.setImageDrawable(new GifDrawable(mModel.getGifThumbnailBytes()));
      } catch (IOException e) {
        SzLog.e(TAG, "Could not show gif", e);
      }
    } else {
      BusProvider.getInstance().post(new RequestThumbnailGifEvent(name));
    }

    if (inDialog) {
      itemView.setOnClickListener(null);
    } else {
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          SzAnalytics.newSelectEffectEvent()
              .withItemId(name)
              .log(itemView.getContext());

          BusProvider.getInstance().post(new ItemClickEvent(name, getAdapterPosition()));
        }
      });
    }
  }

  public void unbind() {
    SzLog.f(TAG, "unbind: " + getAdapterPosition());
    collapseTab(false);

    if (mModel != null && (mModel.getGifThumbnailBytes() == null || mModel.getGifThumbnailBytes().length == 0)) {
      BusProvider.getInstance().post(new RequestStopThumbnailGifGenerationEvent(mModel.getEffectTemplate().getName()));
    }
    mNameTextView.setText(null);
    mGifImageView.setImageDrawable(null);
    itemView.setOnClickListener(null);

    mProgressBar.setVisibility(View.VISIBLE);
  }

  private void setSelected(boolean selected) {
    if (selected) {
      expandTab();
    } else {
      collapseTab(true);
    }
  }

  private void expandTab() {
    if (!mTabExpanded) {
      mTabExpanded = true;
      mProgressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
      ValueAnimator anim = ValueAnimator.ofInt(mCollpasedTabHeight, mExpandedTabHeight);
      anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
          int val = (Integer) valueAnimator.getAnimatedValue();
          ViewGroup.LayoutParams layoutParams = mTabView.getLayoutParams();
          layoutParams.height = val;
          mTabView.setLayoutParams(layoutParams);
        }
      });
      anim.setDuration(AnimationUtils.DEFAULT_ANIMATION_DURATION_MS);
      anim.start();
    }
  }

  private void collapseTab(boolean animate) {
    if (mTabExpanded) {
      mTabExpanded = false;
      mProgressBar.getIndeterminateDrawable().setColorFilter(mTabColorInt, android.graphics.PorterDuff.Mode.MULTIPLY);
      if (animate) {
        ValueAnimator anim = ValueAnimator.ofInt(mExpandedTabHeight, mCollpasedTabHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int val = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = mTabView.getLayoutParams();
            layoutParams.height = val;
            mTabView.setLayoutParams(layoutParams);
          }
        });
        anim.setDuration(AnimationUtils.DEFAULT_ANIMATION_DURATION_MS);
        anim.start();
      } else {
        ViewGroup.LayoutParams layoutParams = mTabView.getLayoutParams();
        layoutParams.height = mCollpasedTabHeight;
        mTabView.setLayoutParams(layoutParams);
      }
    }
  }
}

