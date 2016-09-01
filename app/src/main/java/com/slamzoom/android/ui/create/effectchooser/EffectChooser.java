package com.slamzoom.android.ui.create.effectchooser;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
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
  private LinearLayoutManagerWithSmoothScroller mLayoutManager;

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

    mLayoutManager = new LinearLayoutManagerWithSmoothScroller(getContext());
    setLayoutManager(mLayoutManager);
    setHasFixedSize(true);
  }

  public void update(List<EffectModel> effectModels) {
    mAdapter.update(effectModels);
  }

  public void smoothScrollTo(String effectName) {
    smoothScrollToPosition(getPositionForEffect(effectName));
  }

  private int getPositionForEffect(String effectName) {
    int position = 0;
    for (EffectModel effectModel : mAdapter.getModels()) {
      if (effectModel.getEffectTemplate().getName().equals(effectName)) {
        break;
      }
      position++;
    }
    return position;
  }

  @Subscribe
  public void on(GifService.GifReadyEvent event) {
    if (mAdapter != null && event.thumbnail) {
      mAdapter.setGif(getContext(), event.effectName, event.gifBytes);
    }
  }

  public class LinearLayoutManagerWithSmoothScroller extends LinearLayoutManager {
    public LinearLayoutManagerWithSmoothScroller(Context context) {
      super(context, HORIZONTAL, false);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
      RecyclerView.SmoothScroller smoothScroller = new TopSnappedSmoothScroller(recyclerView.getContext());
      smoothScroller.setTargetPosition(position);
      startSmoothScroll(smoothScroller);
    }

    private class TopSnappedSmoothScroller extends LinearSmoothScroller {
      public TopSnappedSmoothScroller(Context context) {
        super(context);
      }

      @Override
      public PointF computeScrollVectorForPosition(int targetPosition) {
        return LinearLayoutManagerWithSmoothScroller.this.computeScrollVectorForPosition(targetPosition);
      }

      @Override
      protected int getVerticalSnapPreference() {
        return SNAP_TO_START;
      }
    }
  }
}
