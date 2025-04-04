package com.slamzoom.android.billing;

import java.util.List;

/**
 * Created by clocksmith on 7/7/16.
 */
public interface GetPurchasesCallback {
  void onSuccess(List<Purchase> purchases);
  void onFailure();
}
