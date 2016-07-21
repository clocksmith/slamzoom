package com.slamzoom.android.billing;

import java.util.List;

/**
 * Created by clocksmith on 7/8/16.
 */
public interface GetPurchasedPacksCallback {
  void onSuccess(List<String> packNames);
  void onFailure();
}
