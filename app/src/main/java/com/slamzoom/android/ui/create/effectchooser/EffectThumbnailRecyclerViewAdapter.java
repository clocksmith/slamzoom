package com.slamzoom.android.ui.create.effectchooser;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.slamzoom.android.R;

import java.util.List;

/**
 * Created by clocksmith on 2/27/16.
 */
public class EffectThumbnailRecyclerViewAdapter extends RecyclerView.Adapter<EffectThumbnailViewHolder> {
  private static final String TAG = EffectThumbnailRecyclerViewAdapter.class.getSimpleName();

  private List<EffectModel> mModels;

  public EffectThumbnailRecyclerViewAdapter(List<EffectModel>  models) {
    mModels = models;
  }

  @Override
  public EffectThumbnailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new EffectThumbnailViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_effect, parent, false));
  }

  @Override
  public void onBindViewHolder(EffectThumbnailViewHolder holder, int position) {
    holder.bind(mModels.get(position));
  }

  @Override
  public int getItemCount() {
    return mModels.size();
  }

  public void setGifPreview(String effectName, byte[] gifBytes) {
    for (int position = 0; position < mModels.size(); position++) {
      if (mModels.get(position).getEffectTemplate().getName().equals(effectName)) {
        mModels.get(position).setGifPreviewBytes(gifBytes);
        notifyItemChanged(position);
        break;
      }
    }
  }

  public void clearGifsAndShowSpinners() {
    for (EffectModel model : mModels) {
      model.setGifPreviewBytes(null);
    }
    notifyItemRangeChanged(0, getItemCount());
  }
}
