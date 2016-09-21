package com.slamzoom.android.ui.create.effectchooser;

import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.slamzoom.android.R;
import com.slamzoom.android.common.logging.SzLog;
import com.slamzoom.android.common.events.BusProvider;
import com.slamzoom.android.effects.Effects;
import com.slamzoom.android.mediacreation.gif.GifService;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Created by clocksmith on 2/27/16.
 */
public class EffectChooser extends RecyclerView {
  private static final String TAG = EffectChooser.class.getSimpleName();

  private List<EffectModel> mModels;
  private boolean mInDialog;

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

  public void initForDialog(List<EffectModel> effectModels, int selectedPosition) {
    init(effectModels, true);
    if (selectedPosition >= 0) {
      smoothScrollToPosition(selectedPosition);
    }
  }

  public void initForCreateActivity() {
    init(Effects.createEffectModels(), false);
  }

  private void init(List<EffectModel> effectModels, boolean inDialog) {
    BusProvider.getInstance().register(this);

    mModels = effectModels;
    mInDialog = inDialog;

    mAdapter = new EffectThumbnailRecyclerViewAdapter();
    setAdapter(mAdapter);

    mLayoutManager = new LinearLayoutManagerWithSmoothScroller(getContext());
    setLayoutManager(mLayoutManager);
    setHasFixedSize(true);
  }

  public EffectModel getEffectModel(String effectName) {
    for (EffectModel effectModel : mModels) {
      if (effectModel.getEffectTemplate().getName().equals(effectName)) {
        return effectModel;
      }
    }
    return null;
  }

  public void setSelectedEffect(String effectName) {
    for (EffectModel model : mModels) {
      model.setSelected(model.getEffectTemplate().getName().equals(effectName));
    }
    mAdapter.notifyDataSetChanged();
  }

  public void setGifBytes(String effectName, byte[] gifBytes) {
    for (int position = 0; position < mModels.size(); position++) {
      if (mModels.get(position).getEffectTemplate().getName().equals(effectName)) {
        mModels.get(position).setGifThumbnailBytes(gifBytes);
        final int finalPos = position;

        // This prevents stutter in scrolling when items are updated.
        new Handler(getContext().getMainLooper()).post(new Runnable() {
          @Override
          public void run() {
            mAdapter.notifyItemChanged(finalPos);
          }
        });

        break;
      }
    }
  }

  public void clearAllGifBytes() {
    for (EffectModel model : mModels) {
      model.setGifThumbnailBytes(null);
    }
    mAdapter.notifyDataSetChanged();
  }

  public void setLockStatuses(List<String> purchasedPackNames) {
    for (EffectModel model : mModels) {
      model.setLocked(!purchasedPackNames.contains(model.getEffectTemplate().getPackName()));
    }
    mAdapter.notifyDataSetChanged();
  }

  @Subscribe
  public void on(GifService.GifReadyEvent event) {
    if (event.thumbnail) {
      setGifBytes(event.effectName, event.gifBytes);
    }
  }
  
  public class EffectThumbnailRecyclerViewAdapter extends Adapter<EffectThumbnailViewHolder> {
    @Override
    public EffectThumbnailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new EffectThumbnailViewHolder(
          LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_effect, parent, false));
    }

    @Override
    public void onBindViewHolder(EffectThumbnailViewHolder holder, int position) {
      SzLog.f(TAG, "binding position: " + position);
      holder.rebind(mModels.get(position), mInDialog);
    }

    @Override
    public int getItemCount() {
      return mModels.size();
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
