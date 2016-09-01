package com.slamzoom.android.ui.create.effectchooser;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.google.common.collect.Lists;
import com.slamzoom.android.R;
import com.slamzoom.android.common.bus.BusProvider;
import com.slamzoom.android.mediacreation.gif.GifService;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Created by clocksmith on 2/27/16.
 */
public class EffectChooser extends RecyclerView {
  private static final String TAG = EffectChooser.class.getSimpleName();

  private EffectThumbnailRecyclerViewAdapter mAdapter;

  public EffectChooser(Context context) {
    this(context, null);
  }

  public EffectChooser(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public EffectChooser(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void init(List<EffectModel> effectModels, boolean inDialog) {
    BusProvider.getInstance().register(this);

    mAdapter = new EffectThumbnailRecyclerViewAdapter(effectModels, inDialog);
    setAdapter(mAdapter);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) {
//      @Override
//      public boolean supportsPredictiveItemAnimations() {
//        return true;
//      }
    };
    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    setLayoutManager(linearLayoutManager);
    setHasFixedSize(true);
//    setNestedScrollingEnabled(true);

    ItemAnimator animator = getItemAnimator();
    if (animator instanceof SimpleItemAnimator) {
//      animator.setRemoveDuration(0);
      // TODO(clocksmith): only animate incoming gif.
//      ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
    }
  }

  public void update(List<EffectModel> effectModels) {
    mAdapter.update(effectModels);
  }

  @Subscribe
  public void on(GifService.GifReadyEvent event) {
    if (mAdapter != null && event.thumbnail) {
      mAdapter.setGif(getContext(), event.effectName, event.gifBytes);
    }

  }
}
