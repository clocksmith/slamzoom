package com.slamzoom.android.ui.create.effectchooser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.slamzoom.android.R;
import com.slamzoom.android.common.singletons.BusProvider;

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

  public class RequestCancelGifPreviewEvent {
    public final String effectName;
    public RequestCancelGifPreviewEvent(String effectName) {
      this.effectName = effectName;
    }
  }

  @Bind(R.id.nameTextView) TextView mNameTextView;
  @Bind(R.id.gifImageView) GifImageView mGifImageView;
  @Bind(R.id.progressBar) ProgressBar mProgressBar;

  private EffectModel mModel;

  public EffectThumbnailViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }

  public void unbindCurrentAndBindNew(final EffectModel model) {
    unbind();
    bind(model);
  }

  public void bind(final EffectModel model) {
    mModel = model;
    final String name = model.getEffectTemplate().getName();

    mNameTextView.setText(name);

    // Either the load the gif or request it.
    if (model.getGifPreviewBytes() != null && model.getGifPreviewBytes().length > 0) {
      try {
        mGifImageView.setImageDrawable(new GifDrawable(model.getGifPreviewBytes()));
        mProgressBar.setVisibility(View.GONE);
      } catch (IOException e) {
        mGifImageView.setImageDrawable(null);
        mProgressBar.setVisibility(View.VISIBLE);
        Log.e(TAG, "Could not create GifDrawable for: " + name);
      }
    } else {
      mGifImageView.setImageDrawable(null);
      mProgressBar.setVisibility(View.VISIBLE);
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
    if (mModel != null) {
      BusProvider.getInstance().post(new RequestCancelGifPreviewEvent(mModel.getEffectTemplate().getName()));
    }
    mNameTextView.setText(null);
    mGifImageView.setImageDrawable(null);
    itemView.setOnClickListener(null);
  }
}

