package com.slamzoom.android.ui.create.effectchooser;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.slamzoom.android.R;
import com.slamzoom.android.common.SzLog;

import java.util.List;

/**
 * Created by clocksmith on 2/27/16.
 */
public class EffectThumbnailRecyclerViewAdapter extends RecyclerView.Adapter<EffectThumbnailViewHolder> {
  private static final String TAG = EffectThumbnailRecyclerViewAdapter.class.getSimpleName();

  private List<EffectModel> mModels;
  private boolean mClickable;

  public EffectThumbnailRecyclerViewAdapter(List<EffectModel> models, boolean clickable) {
    mModels = models;
    mClickable = clickable;
  }

  public void update(List<EffectModel> models) {
    mModels = models;
    notifyDataSetChanged();
  }

  @Override
  public EffectThumbnailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new EffectThumbnailViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_effect, parent, false));
  }

  @Override
  public void onBindViewHolder(EffectThumbnailViewHolder holder, int position) {
    SzLog.f(TAG, "binding position: " + position);
    holder.unbindCurrentAndBindNew(mModels.get(position), mClickable);
  }

  @Override
  public int getItemCount() {
    return mModels.size();
  }

  public void setGif(Context context, String effectName, byte[] gifBytes) {
    for (int position = 0; position < mModels.size(); position++) {
      if (mModels.get(position).getEffectTemplate().getName().equals(effectName)) {
        mModels.get(position).setGifThumbnailBytes(gifBytes);
        final int finalPos = position;

        // This prevents stutter in scrolling when items are updated.
        new Handler(context.getMainLooper()).post(new Runnable() {
          @Override
          public void run() {
            notifyItemChanged(finalPos);
          }
        });

        break;
      }
    }
  }
}
