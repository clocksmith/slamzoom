package com.slamzoom.android.mediacreation.gif;

import android.content.Context;

import com.google.common.base.Objects;
import com.slamzoom.android.mediacreation.MediaConstants;
import com.slamzoom.android.common.logging.SzLog;
import com.slamzoom.android.mediacreation.MediaConfig;
import com.slamzoom.android.mediacreation.StopwatchTracker;

/**
 * Created by clocksmith on 6/12/16.
 */
public class GifCreatorManager implements Comparable {
  private static final String TAG = GifCreatorManager.class.getSimpleName();

  private Context mContext;
  private MediaConfig mMediaConfig;
  private boolean mIsThumbnail;
  private int mIndex;
  private GifCreatorCallback mCallback;

  private int mGifSize;
  private StopwatchTracker mTracker;

  private GifCreator mGifCreator;
  private boolean mIsRunning;
  private boolean mHasStopped;

  public GifCreatorManager(
      Context context,
      MediaConfig mediaConfig,
      boolean thumbnail,
      int index,
      GifCreatorCallback callback) {
    mContext = context;
    mMediaConfig = mediaConfig;
    mIsThumbnail = thumbnail;
    mIndex = index;
    mCallback = callback;

    mGifSize = thumbnail ? MediaConstants.THUMBNAIL_SIZE_PX : MediaConstants.MAIN_SIZE_PX;
    mTracker = new StopwatchTracker();
  }

  public void start() {
    SzLog.f(TAG, "start() " + (mIsThumbnail ? "thumbnail_" : "") + mMediaConfig.effectTemplate.getName());
    mIsRunning = true;
    mGifCreator = new GifCreator(mContext, mMediaConfig, mTracker);
    mGifCreator.createAsync(mCallback);
  }

  public void stop() {
    SzLog.f(TAG, "stop() " + (mIsThumbnail ? "thumbnail_" : "") + mMediaConfig.effectTemplate.getName());
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

  public String getId() {
    return mIsThumbnail ? mMediaConfig.effectTemplate.getName() : GifService.getKeyFromConfig(mMediaConfig);
  }

  public int getIndex() {
    return mIndex;
  }

  public float getHotspotScale() {
    return mMediaConfig.hotspot.width();
  }

  public String getEndText() {
    return mMediaConfig.endText;
  }

  public StopwatchTracker getTracker() {
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
