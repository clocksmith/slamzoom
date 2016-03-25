package com.slamzoom.android.ui.main.effect;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.slamzoom.android.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by clocksmith on 2/27/16.
 */
public class EffectChooser extends LinearLayout {
  @Bind(R.id.recyclerView) RecyclerView mRecyclerView;

  private EffectRecyclerViewAdapter mAdapter;

  public EffectChooser(Context context) {
    this(context, null);
  }

  public EffectChooser(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public EffectChooser(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    LayoutInflater.from(context).inflate(R.layout.view_effect_chooser, this);
    ButterKnife.bind(this);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    mRecyclerView.setLayoutManager(linearLayoutManager);
  }

  public void setEffectModels(List<EffectModel> effectModels) {
    mAdapter = new EffectRecyclerViewAdapter(effectModels);
    mRecyclerView.setAdapter(mAdapter);
  }

  public void setGifPreview(String effectName, byte[] gifBytes) {
    mAdapter.setGifPreview(effectName, gifBytes);
  }

  public void clearGifsAndShowSpinners() {
    mAdapter.clearGifsAndShowSpinners();
  }
}
