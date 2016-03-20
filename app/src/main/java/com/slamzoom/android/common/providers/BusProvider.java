package com.slamzoom.android.common.providers;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by clocksmith on 3/7/16.
 */
public final class BusProvider {
  private static final Handler mHandler = new Handler(Looper.getMainLooper());

  private static final Bus BUS = new Bus() {
    @Override
    public void post(final Object event) {
      if (Looper.myLooper() == Looper.getMainLooper()) {
        super.post(event);
      } else {
        mHandler.post(new Runnable() {
          @Override
          public void run() {
            BUS.post(event);
          }
        });
      }
    }
  };

  public static Bus getInstance() {
    return BUS;
  }

  private BusProvider() {}
}
