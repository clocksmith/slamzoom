package com.slamzoom.android.mediacreation.gif;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Runnables;
import com.slamzoom.android.common.logging.SzLog;
import com.slamzoom.android.mediacreation.MediaConfig;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;

/**
 * Created by antrob on 9/24/16.
 */

public class DeferrableGifService {
  private static final String TAG = DeferrableGifService.class.getSimpleName();

  private Activity mActivity;
  private GifService mGifService;
  private GifServiceConnection mGifServiceConnection;
  private Queue<Runnable> mDeferredGifServiceRunnable = Queues.newConcurrentLinkedQueue();

  public DeferrableGifService(Activity activity) {
    mActivity = activity;
  }

  public void bind() {
    if (mGifServiceConnection == null) {
      mGifServiceConnection = new GifServiceConnection();
    }
    mActivity.bindService(new Intent(mActivity, GifService.class), mGifServiceConnection, Context.BIND_AUTO_CREATE);
  }

  public void unbind() {
    mDeferredGifServiceRunnable.clear();
    if (mGifService != null) {
      mActivity.unbindService(mGifServiceConnection);
    }
  }

  public void clearGifService() {
    SzLog.f(TAG, "clearGifService");
    if (mGifService != null) {
      mGifService.clear();
    } else {
      mDeferredGifServiceRunnable.add(new Runnable() {
        @Override
        public void run() {
          mGifService.clear();
        }
      });
    }
  }

  public void requestMainGif(final Callable<MediaConfig> getMediaConfigCallable) {
    SzLog.f(TAG, "requestMainGif");
    if (mGifService != null) {
      try {
        mGifService.requestMainGif(getMediaConfigCallable.call());
      } catch (Exception e) {
        SzLog.e(TAG, "Could not get media config", e);
      }
    } else {
      mDeferredGifServiceRunnable.add(new Runnable() {
        @Override
        public void run() {
          try {
            mGifService.requestMainGif(getMediaConfigCallable.call());
          } catch (Exception e) {
            SzLog.e(TAG, "Could not get media config", e);
          }
        }
      });
    }
  }

  public void requestThumbnailGifs(final Callable<List<MediaConfig>> getMediaConfigsCallable) {
    SzLog.f(TAG, "requestThumbnailGifs");
    if (mGifService != null) {
      try {
        mGifService.requestThumbnailGifs(getMediaConfigsCallable.call());
      } catch (Exception e) {
        SzLog.e(TAG, "Could not get media configs", e);
      }
    } else {
      mDeferredGifServiceRunnable.add(new Runnable() {
        @Override
        public void run() {
          try {
            mGifService.requestThumbnailGifs(getMediaConfigsCallable.call());
          } catch (Exception e) {
            SzLog.e(TAG, "Could not get media configs", e);
          }
        }
      });
    }
  }

  public byte[] getGifBytes(String effectName, String endText) {
    if (mGifService != null) {
      return mGifService.getGifBytes(effectName, endText);
    } else {
      SzLog.e(TAG, "mGifService is null while trying to getSelectedGifBytes");
      return null;
    }
  }

  private synchronized void callAllDeferred() {
    SzLog.f(TAG, "callAllDeferred");
    while (!mDeferredGifServiceRunnable.isEmpty()) {
      try {
        SzLog.f(TAG, "dequeuing and running");
        mDeferredGifServiceRunnable.remove().run();
      } catch (Exception e) {
        SzLog.e(TAG, "Could not call deferred callable", e);
      }
    }
  }

  private class GifServiceConnection implements ServiceConnection {
    @Override
    public void onServiceConnected(ComponentName className, IBinder iBinder) {
      SzLog.f(TAG, "onServiceConnected");
      mGifService = ((GifService.GifServiceBinder) iBinder).getService();
      callAllDeferred();
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {
      mGifService = null;
    }
  }
}
