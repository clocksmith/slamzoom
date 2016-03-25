package com.slamzoom.android.mediacreation.gif;

import android.content.Context;
import android.graphics.Bitmap;

import com.slamzoom.android.common.singletons.BusProvider;
import com.slamzoom.android.mediacreation.CreateMediaCallback;
import com.slamzoom.android.mediacreation.MediaCreator;
import com.slamzoom.android.effects.EffectModel;

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

  public GifCreator(
      Context context,
      Bitmap selectedBitmap,
      EffectModel effectModel,
      int gifSize,
      boolean isFinalGif,
      CreateGifCallback callback) {
    super(context, selectedBitmap, effectModel, gifSize, isFinalGif, callback);
  }

  @Override
  public GifFrame createFrame(Bitmap bitmap, int delayMillis) {
    GifFrame frame =  new GifFrame(bitmap, delayMillis);

    if (mIsFinalGif) {
      BusProvider.getInstance().post(new ProgressUpdateEvent(mEffectModel, 1.0 / 3 / mTotalNumFrames));
    }

    return frame;
  }

  @Override
  public GifEncoder createEncoder() {
    GifEncoder gifEncoder = new GifEncoder();
    if (mIsFinalGif) {
      gifEncoder.setProgressUpdateListener(this);
    }
    return gifEncoder;
  }
}
