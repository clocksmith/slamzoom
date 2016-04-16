package com.slamzoom.android.mediacreation.gif;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.common.base.Strings;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.global.singletons.BusProvider;
import com.slamzoom.android.mediacreation.CreateMediaCallback;
import com.slamzoom.android.mediacreation.MediaCreator;
import com.slamzoom.android.ui.create.effectchooser.EffectModel;

/**
 * Created by clocksmith on 3/25/16.
 */
public class GifCreator extends MediaCreator implements GifEncoder.ProgressUpdateListener {
  public static final String TAG = GifCreator.class.getSimpleName();

  public interface CreateGifCallback extends CreateMediaCallback {
    void onCreateGif(byte[] gifBytes);
  }

  @Override
  public void onProgressUpdate(double amountToUpdate) {
    BusProvider.getInstance().post(new ProgressUpdateEvent(mEffectModel, amountToUpdate));
  }

  public class ProgressUpdateEvent {
    public final EffectModel effectModel;
    public final double amountToUpdate;
    public ProgressUpdateEvent(EffectModel effectModel, double amountToUpdate) {
      this.effectModel = effectModel;
      this.amountToUpdate = amountToUpdate;
    }
  }

  public GifCreator(Context context, GifConfig gifConfig, int gifSize, CreateGifCallback callback) {
    super(context, gifConfig.bitmap, getAdjustedEffectModel(gifConfig), gifSize, callback);
  }

  private static EffectModel getAdjustedEffectModel(GifConfig gifConfig) {
    // This is weird. Not yet sure how else to do this.
    EffectModel effectModel = gifConfig.effectModel;
    for (EffectStep step :  effectModel.getEffectTemplate().getEffectSteps()) {
      step.setHotspot(gifConfig.hotspot);
      if (!Strings.isNullOrEmpty(gifConfig.endText)) {
        step.setEndText(gifConfig.endText);
      }
    }
    return effectModel;
  }

  @Override
  public GifFrame createFrame(Bitmap bitmap, int delayMillis) {
    GifFrame frame =  new GifFrame(bitmap, delayMillis);
    BusProvider.getInstance().post(new ProgressUpdateEvent(mEffectModel, 1.0 / 3 / mTotalNumFrames));
    return frame;
  }

  @Override
  public GifEncoder createEncoder() {
    GifEncoder gifEncoder = new GifEncoder();
    gifEncoder.setProgressUpdateListener(this);
    return gifEncoder;
  }
}
