package com.slamzoom.android.mediacreation.gif;

import android.content.Context;

import com.slamzoom.android.mediacreation.MediaCreatorTracker;

/**
 * Created by clocksmith on 6/12/16.
 */
public class GifCreatorManager {
  private Context mContext;
  private GifConfig mGifConfig;
  private int mGifSize;
  private GifCreator.CreateGifCallback mCallback;
  private MediaCreatorTracker mTracker;

  private GifCreator mGifCreator;
  private boolean mIsRunning;

  public GifCreatorManager(
      Context context,
      GifConfig gifConfig,
      int gifSize,
      GifCreator.CreateGifCallback callback) {
    mContext = context;
    mGifConfig = gifConfig;
    mGifSize = gifSize;
    mCallback = callback;
    mTracker = new MediaCreatorTracker();
  }

  public void start() {
    mIsRunning = true;
    mGifCreator = new GifCreator(mContext, mGifConfig, mGifSize, mCallback, mTracker);
    mGifCreator.createAsync();
  }

  public void stop() {
    mIsRunning = false;
    if (mGifCreator != null) {
      mGifCreator.cancel();
    }
  }

  // TODO(clocksmith): actually cancel these.
  public void cancel() {
    stop();
  }

  public boolean isRunning() {
    return mIsRunning;
  }

  public String getName() {
    return mGifConfig.effectModel.getEffectTemplate().getName();
  }

  public MediaCreatorTracker getTracker() {
    return mTracker;
  }
}
