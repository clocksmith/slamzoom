package com.slamzoom.android.ui.create.effectchooser;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.slamzoom.android.R;
import com.slamzoom.android.common.bus.BusProvider;
import com.slamzoom.android.common.utils.AnimationUtils;
import com.slamzoom.android.common.utils.SzAnalytics;
import com.slamzoom.android.common.utils.SzLog;
import com.squareup.otto.Subscribe;

import java.io.IOException;

import butterknife.Bind;
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

  @Bind(R.id.lockIcon) ImageView mLockIcon;
  @Bind(R.id.gifImageView) GifImageView mGifImageView;
  @Bind(R.id.progressBar) ProgressBar mProgressBar;
  @Bind(R.id.nameTextView) TextView mNameTextView;
  @Bind(R.id.packNameTextView) TextView mPackNameTextView;
  @Bind(R.id.tabView) View mTabView;

  private EffectModel mModel;
  private int mColor;

  private int mInitialTabHeight;
  private boolean mTabExpanded;

  public EffectThumbnailViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
    BusProvider.getInstance().register(this);
    mInitialTabHeight = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.effect_chooser_tab_height);
  }

  public void unbindCurrentAndBindNew(final EffectModel model, boolean clickable) {
    unbind();
    bind(model, clickable);
    mTabExpanded = false;
  }

  public void bind(final EffectModel model, boolean clickable) {
    SzLog.f(TAG, "bind: " + getAdapterPosition());
    mModel = model;
    mColor = model.getEffectTemplate().getColor();
    final String name = mModel.getEffectTemplate().getName();
    final String packName = mModel.getEffectTemplate().getPackName();

    mLockIcon.setVisibility(model.isLocked() ? View.VISIBLE : View.GONE);

    mNameTextView.setText(name);
    mPackNameTextView.setText(packName);

//    mPackNameTextView.setTextColor(mColor);
    mTabView.setBackgroundColor(mColor);
    mLockIcon.setColorFilter(mColor);
    mProgressBar.getIndeterminateDrawable().setColorFilter(mColor, android.graphics.PorterDuff.Mode.MULTIPLY);

    // Either show the gif or request it.
    if (model.getGifThumbnailBytes() != null && model.getGifThumbnailBytes().length > 0) {
      try {
        mProgressBar.setVisibility(View.GONE);
        mGifImageView.setImageDrawable(new GifDrawable(mModel.getGifThumbnailBytes()));
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      BusProvider.getInstance().post(new RequestThumbnailGifEvent(name));
    }

    if (clickable) {
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          SzAnalytics.newSelectContentEvent()
              .withContentType("effect")
              .withItemId(name)
              .log(itemView.getContext());

          BusProvider.getInstance().post(new ItemClickEvent(name, getAdapterPosition()));
          expandTab();
        }
      });
    } else {
      itemView.setOnClickListener(null);
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

  @Subscribe
  public void on(EffectThumbnailViewHolder.ItemClickEvent event) throws IOException {
    if (getAdapterPosition() != event.position) {
      collapseTab(true);
    }
  }

  private void expandTab() {
    if (!mTabExpanded) {
      mTabExpanded = true;
      mProgressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
      ValueAnimator anim = ValueAnimator.ofInt(mInitialTabHeight, itemView.getMeasuredHeight());
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
      mProgressBar.getIndeterminateDrawable().setColorFilter(mColor, android.graphics.PorterDuff.Mode.MULTIPLY);
      if (animate) {
        ValueAnimator anim = ValueAnimator.ofInt(mTabView.getMeasuredHeight(), mInitialTabHeight);
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
        layoutParams.height = mInitialTabHeight;
        mTabView.setLayoutParams(layoutParams);
      }
    }
  }

  private void showGif(){
    AnimatorSet scaleDown = AnimationUtils.getScaleDownSet(mProgressBar, 1000);
    scaleDown.addListener(new AnimationUtils.OnAnimationEndOnlyListener() {
      @Override
      public void onAnimationEnd(Animator animation) {
        mProgressBar.setVisibility(View.GONE);
        try {
          mGifImageView.setImageDrawable(new GifDrawable(mModel.getGifThumbnailBytes()));
        } catch (IOException e) {
          mGifImageView.setImageDrawable(null);
          mProgressBar.setVisibility(View.VISIBLE);
          SzLog.e(TAG, "Could not create GifDrawable for: " + mModel.getEffectTemplate().getName());
        }
        AnimationUtils.getScaleUpSet(mGifImageView, 1000).start();
      }
    });
    scaleDown.start();
  }
}

