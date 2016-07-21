package com.slamzoom.android.billing;

import android.app.PendingIntent;

import java.util.List;

/**
 * Created by clocksmith on 7/8/16.
 */
public interface GetBuyIntentCallback {
  void onSuccess(PendingIntent buyIntent);
  void onFailure();
}
