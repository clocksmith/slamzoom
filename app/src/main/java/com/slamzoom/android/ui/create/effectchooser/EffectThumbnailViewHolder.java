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

  @Bind(R.id.nameTextView) TextView mNameTextView;
  @Bind(R.id.gifImageView) GifImageView mGifImageView;
  @Bind(R.id.progressBar) ProgressBar mProgressBar;

  private Context mContext;

  public EffectThumbnailViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
    mContext = itemView.getContext();
  }

  public void bind(final EffectModel model) {
    mNameTextView.setText(model.getEffectTemplate().getName());

    itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        BusProvider.getInstance().post(new ItemClickEvent(model.getEffectTemplate().getName()));
      }
    });

    String effectName = model.getEffectTemplate().getName();
    try {
      mGifImageView.setImageDrawable(
          new GifDrawable(mContext.getAssets(), "slamzoom_preview_" + effectName + ".gif"));
    } catch (IOException e) {
      mGifImageView.setImageDrawable(null);
      Log.e(TAG, "Could not open gif from assets");
    }
  }
}

