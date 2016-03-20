package com.slamzoom.android.ui.effect;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.slamzoom.android.R;

import java.util.List;

/**
 * Created by clocksmith on 2/27/16.
 */
public class EffectRecyclerViewAdapter extends RecyclerView.Adapter<EffectViewHolder> {
  private static final String TAG = EffectRecyclerViewAdapter.class.getSimpleName();

  private List<EffectModel> mModels;

  public EffectRecyclerViewAdapter(List<EffectModel>  models) {
    mModels = models;
  }

  @Override
  public EffectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new EffectViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_effect, parent, false));
  }

  @Override
  public void onBindViewHolder(EffectViewHolder holder, int position) {
    holder.bind(mModels.get(position));
  }

  @Override
  public int getItemCount() {
    return mModels.size();
  }

  public void setGifPreview(String effectName, byte[] gifbytes) {
    Log.d(TAG, "setGifPreview: " + effectName + " and gifbytes are: " + (gifbytes == null ? "null" : "not null"));
    int position = 0;
    for (EffectModel model : mModels) {
      if (model.getName().equals(effectName)) {
        model.setGifBytes(gifbytes);
        Log.e(TAG, "updating position: " + position);
        notifyItemChanged(position);
        break;
      }
      position++;
    }
  }

  public void clearGifsAndShowSpinners() {
    for (EffectModel model : mModels) {
      model.setGifBytes(null);
    }
    notifyItemRangeChanged(0, getItemCount());
  }
}
