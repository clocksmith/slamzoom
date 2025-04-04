package com.slamzoom.android.mediacreation;

import android.os.AsyncTask;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.slamzoom.android.common.logging.SzLog;

import java.util.List;
import java.util.Set;

/**
 * Created by clocksmith on 3/25/16.
 */
public abstract class MediaEncoder<F extends MediaFrame> {
  private static final String TAG = MediaEncoder.class.getSimpleName();

  protected List<F> mFrames = Lists.newArrayList();
  protected int mWidth;
  protected int mHeight;
  protected Set<AsyncTask> mTasks = Sets.newConcurrentHashSet();
  protected MediaCreatorCallback mCallback;

  public void encodeAsync(MediaCreatorCallback callback) {
    mCallback = callback;
  }

  public void cancel() {
    for (AsyncTask task : mTasks) {
      task.cancel(true);
    }
  }

  public void addFrames(Iterable<F> frames) {
    for (F frame : frames) {
      addFrame(frame);
    }
  }

  public void addFrame(F frame) {
    setOrVerifyGifDimensions(frame);
    mFrames.add(frame);
  }

  protected void setOrVerifyGifDimensions(MediaFrame frame) {
    if (mWidth < 1 && mHeight < 1) {
      mWidth = frame.width;
      mHeight = frame.height;
    }
    if (mWidth > 0 && mWidth != frame.width || mHeight > 0 & mHeight != frame.height) {
      SzLog.e(TAG, "Bitmap does not have same dimensions as previous frames");
    }
  }
}
