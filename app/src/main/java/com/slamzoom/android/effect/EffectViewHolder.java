package com.slamzoom.android.effect;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.slamzoom.android.R;
import com.slamzoom.android.common.BusProvider;
import com.slamzoom.android.gif.GifService;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by clocksmith on 2/27/16.
 */
public class EffectViewHolder extends RecyclerView.ViewHolder {
  private static final String TAG = EffectViewHolder.class.getSimpleName();

  public class ItemClickEvent {
    public final String effectName;
    public ItemClickEvent(String effectName) {
      this.effectName = effectName;
    }
  }

  @Bind(R.id.nameTextView) TextView mNameTextView;
  @Bind(R.id.gifImageView) GifImageView mGifImageView;
  @Bind(R.id.progressBar) ProgressBar mProgressBar;

  public EffectViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }

  public void bind(final EffectModel model) {
    mNameTextView.setText(model.getName());

    itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        BusProvider.getInstance().post(new ItemClickEvent(model.getName()));
      }
    });

    if (model.getGifBytes() != null) {
      mProgressBar.setVisibility(View.GONE);
      try {
        mGifImageView.setImageDrawable(new GifDrawable(model.getGifBytes()));
      } catch (IOException e) {
        Log.e(TAG, "Could not set gif", e);
      }
    } else {
//      Log.e(TAG, "gif bytes are null");
      mProgressBar.setVisibility(View.VISIBLE);
      mGifImageView.setImageBitmap(null);
    }
  }
}
