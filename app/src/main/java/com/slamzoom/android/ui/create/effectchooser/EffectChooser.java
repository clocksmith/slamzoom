package com.slamzoom.android.ui.create.effectchooser;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.common.collect.Lists;
import com.slamzoom.android.R;
import com.slamzoom.android.common.singletons.BusProvider;
import com.slamzoom.android.mediacreation.gif.GifService;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by clocksmith on 2/27/16.
 */
public class EffectChooser extends LinearLayout {
  @Bind(R.id.recyclerView) RecyclerView mRecyclerView;

  private EffectThumbnailRecyclerViewAdapter mAdapter;

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
    BusProvider.getInstance().register(this);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    mRecyclerView.setLayoutManager(linearLayoutManager);
  }

  public void set(List<EffectModel> effectModels) {
      if (mAdapter != null) {
        mAdapter.unbindAll();
      }
      mAdapter = new EffectThumbnailRecyclerViewAdapter(Lists.newArrayList(effectModels));
      mRecyclerView.setAdapter(mAdapter);
  }

  @Subscribe
  public void on(GifService.GifReadyEvent event) {
    if (mAdapter != null && event.preview) {
      mAdapter.setGifPreview(event.effectName, event.gifBytes);
    }
  }
}
