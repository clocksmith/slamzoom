package com.slamzoom.android.mediacreation.gif;

import android.content.Context;

import com.google.common.base.Objects;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.utils.SzLog;
import com.slamzoom.android.mediacreation.MultiPhaseStopwatch;

/**
 * Created by clocksmith on 6/12/16.
 */
public class GifCreatorManager {
  private static final String TAG = GifCreatorManager.class.getSimpleName();

  private Context mContext;
  private GifConfig mGifConfig;
  private boolean mIsPreview;
  private int mGifSize;
  private GifCreator.CreateGifCallback mCallback;
  private MultiPhaseStopwatch mTracker;

  private GifCreator mGifCreator;
  private boolean mIsRunning;
  private boolean mHasStopped;

  public GifCreatorManager(
      Context context,
      GifConfig gifConfig,
      boolean preview,
      GifCreator.CreateGifCallback callback) {
    mContext = context;
    mGifConfig = gifConfig;
    mIsPreview = preview;
    mGifSize = preview ? Constants.DEFAULT_GIF_PREVIEW_SIZE_PX : Constants.DEFAULT_GIF_SIZE_PX;
    mCallback = callback;
    mTracker = new MultiPhaseStopwatch();
  }

  public void start() {
    SzLog.f(TAG, "start() " + (mIsPreview ? "preview_" : "") + mGifConfig.effectTemplate.getName());
    mIsRunning = true;
    mGifCreator = new GifCreator(mContext, mGifConfig, mGifSize, mCallback, mTracker);
    mGifCreator.createAsync();
  }

  public void stop() {
    SzLog.f(TAG, "stop() " + (mIsPreview ? "preview_" : "") + mGifConfig.effectTemplate.getName());
    mHasStopped = true;
    mIsRunning = false;
    mTracker.stopAll();
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

  public boolean hasStopped() {
    return mHasStopped;
  }

  public String getName() {
    return mGifConfig.effectTemplate.getName();
  }

  public int getGifSize() {
    return mGifSize;
  }

  public int getFps() {
    return mGifConfig.fps;
  }

  public float getEndScale() {
    return (float) mGifConfig.hotspot.width() / mGifConfig.bitmap.getWidth();
  }

  public String getEndText() {
    return mGifConfig.endText;
  }

  public MultiPhaseStopwatch getTracker() {
    return mTracker;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(mGifConfig);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final GifCreatorManager other = (GifCreatorManager) obj;
    return Objects.equal(mGifConfig, other.mGifConfig);
  }
}
