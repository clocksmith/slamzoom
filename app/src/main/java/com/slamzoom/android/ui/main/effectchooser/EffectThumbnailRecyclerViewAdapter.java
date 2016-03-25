package com.slamzoom.android.ui.main.effectchooser;

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
    return new EffectThumbnailViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_effect, parent, false));
  }

  @Override
  public void onBindViewHolder(EffectThumbnailViewHolder holder, int position) {
    holder.bind(mModels.get(position));
  }

  @Override
  public int getItemCount() {
    return mModels.size();
  }

  public void setGifPreview(String effectName, byte[] gifbytes) {
    int position = 0;
    for (EffectModel model : mModels) {
      if (model.getEffectTemplate().getName().equals(effectName)) {
        model.setGifPreviewBytes(gifbytes);
        notifyItemChanged(position);
        break;
      }
      position++;
    }
  }

  public void clearGifsAndShowSpinners() {
    for (EffectModel model : mModels) {
      model.setGifPreviewBytes(null);
    }
    notifyItemRangeChanged(0, getItemCount());
  }
}
