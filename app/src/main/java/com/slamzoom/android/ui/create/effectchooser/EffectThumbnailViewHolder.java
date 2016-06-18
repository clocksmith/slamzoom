package com.slamzoom.android.ui.create.effectchooser;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.slamzoom.android.R;
import com.slamzoom.android.common.singletons.BusProvider;
import com.slamzoom.android.common.utils.AnimationUtils;
import com.slamzoom.android.common.utils.FLog;

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
    public ItemClickEvent(String effectName) {
      this.effectName = effectName;
    }
  }

  public class RequestGifPreviewEvent {
    public final String effectName;
    public RequestGifPreviewEvent(String effectName) {
      this.effectName = effectName;
    }
  }

  public class RequestGifPreviewStopEvent {
    public final String effectName;
    public RequestGifPreviewStopEvent(String effectName) {
      this.effectName = effectName;
    }
  }

  @Bind(R.id.gifImageView) GifImageView mGifImageView;
  @Bind(R.id.progressBar) ProgressBar mProgressBar;
  @Bind(R.id.nameTextView) TextView mNameTextView;
  @Bind(R.id.tabView) View mTabView;

  private EffectModel mModel;
  private int mColor;

  public EffectThumbnailViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }

  public void unbindCurrentAndBindNew(final EffectModel model, int color) {
    mColor = color;
    unbind();
    bind(model);
  }

  public void bind(final EffectModel model) {
    FLog.f(TAG, "bind: " + getAdapterPosition());
    mModel = model;
    final String name = mModel.getEffectTemplate().getName();

    mNameTextView.setText(name);

    // Either show the gif or request it.
    if (model.getGifPreviewBytes() != null && model.getGifPreviewBytes().length > 0) {
      try {
        mProgressBar.setVisibility(View.GONE);
        mGifImageView.setImageDrawable(new GifDrawable(mModel.getGifPreviewBytes()));
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      BusProvider.getInstance().post(new RequestGifPreviewEvent(name));
    }

    itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        BusProvider.getInstance().post(new ItemClickEvent(name));
      }
    });
  }

  public void unbind() {
    FLog.f(TAG, "unbind: " + getAdapterPosition());
    if (mModel != null && (mModel.getGifPreviewBytes() == null || mModel.getGifPreviewBytes().length == 0)) {
      BusProvider.getInstance().post(new RequestGifPreviewStopEvent(mModel.getEffectTemplate().getName()));
    }
    mNameTextView.setText(null);
    mGifImageView.setImageDrawable(null);
    itemView.setOnClickListener(null);

    mTabView.setBackgroundColor(mColor);
    mProgressBar.setVisibility(View.VISIBLE);
    mProgressBar.getIndeterminateDrawable().setColorFilter(mColor, android.graphics.PorterDuff.Mode.MULTIPLY);
  }

  private void showGif(){
    AnimatorSet scaleDown = AnimationUtils.getScaleDownSet(mProgressBar, 1000);
    scaleDown.addListener(new AnimationUtils.OnAnimationEndOnlyListener() {
      @Override
      public void onAnimationEnd(Animator animation) {
        mProgressBar.setVisibility(View.GONE);
        try {
          mGifImageView.setImageDrawable(new GifDrawable(mModel.getGifPreviewBytes()));
        } catch (IOException e) {
          mGifImageView.setImageDrawable(null);
          mProgressBar.setVisibility(View.VISIBLE);
          Log.e(TAG, "Could not create GifDrawable for: " + mModel.getEffectTemplate().getName());
        }
        AnimationUtils.getScaleUpSet(mGifImageView, 1000).start();
      }
    });
    scaleDown.start();
  }
}

