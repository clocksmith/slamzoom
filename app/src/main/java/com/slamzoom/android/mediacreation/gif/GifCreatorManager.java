package com.slamzoom.android.mediacreation.gif;

import android.content.Context;

import com.google.common.base.Objects;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.SzLog;
import com.slamzoom.android.mediacreation.MediaConfig;
import com.slamzoom.android.mediacreation.MultiPhaseStopwatch;

/**
 * Created by clocksmith on 6/12/16.
 */
public class GifCreatorManager implements Comparable {
  private static final String TAG = GifCreatorManager.class.getSimpleName();

  private Context mContext;
  private MediaConfig mMediaConfig;
  private boolean mIsPreview;
  private int mIndex;
  private GifCreatorCallback mCallback;

  private int mGifSize;
  private MultiPhaseStopwatch mTracker;

  private GifCreator mGifCreator;
  private boolean mIsRunning;
  private boolean mHasStopped;

  public GifCreatorManager(
      Context context,
      MediaConfig mediaConfig,
      boolean preview,
      int index,
      GifCreatorCallback callback) {
    mContext = context;
    mMediaConfig = mediaConfig;
    mIsPreview = preview;
    mIndex = index;
    mCallback = callback;

    mGifSize = preview ? Constants.THUMBNAIL_SIZE_PX : Constants.MAIN_SIZE_PX;
    mTracker = new MultiPhaseStopwatch();
  }

  public void start() {
    SzLog.f(TAG, "start() " + (mIsPreview ? "preview_" : "") + mMediaConfig.effectTemplate.getName());
    mIsRunning = true;
    mGifCreator = new GifCreator(mContext, mMediaConfig, mTracker);
    mGifCreator.createAsync(mCallback);
  }

  public void stop() {
    SzLog.f(TAG, "stop() " + (mIsPreview ? "preview_" : "") + mMediaConfig.effectTemplate.getName());
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
    return mMediaConfig.effectTemplate.getName();
  }

  public int getIndex() {
    return mIndex;
  }

  public int getGifSize() {
    return mGifSize;
  }

  public int getFps() {
    return mMediaConfig.fps;
  }

  public float getEndScale() {
    return (float) mMediaConfig.hotspot.width() / mMediaConfig.bitmap.getWidth();
  }

  public String getEndText() {
    return mMediaConfig.endText;
  }

  public MultiPhaseStopwatch getTracker() {
    return mTracker;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(mMediaConfig);
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
    return Objects.equal(mMediaConfig, other.mMediaConfig);
  }

  /**
   * We want lower index managers to have higher priority.
   */
  @Override
  public int compareTo(Object o) {
    return mIndex - ((GifCreatorManager) o).getIndex();
  }
}
