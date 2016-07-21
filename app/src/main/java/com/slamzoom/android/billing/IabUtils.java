package com.slamzoom.android.billing;

import android.app.PendingIntent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;

import com.android.vending.billing.IInAppBillingService;
import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.utils.SzLog;

import java.util.List;
import java.util.Map;

/**
 * Created by clocksmith on 7/7/16.
 */
public class IabUtils {
  private static final String TAG = IabUtils.class.getSimpleName();

  // TODO(clocksmith): This would be perfect for remote config. Maybe Extract elsewhere to also hide packs not for sale.
  private static final Map<String, String> PURCHASE_IDS_TO_PACK_NAMES = ImmutableMap.of(
      "packs.exp1.1", "expansion a",
      "packs.exp1.2", "expansion b",
      "packs.exp1.3", "expansion c",
      "packs.exp1.4", "expansion d");

  public static void getBuyIntent(
      final String productId,
      final IInAppBillingService service,
      final GetBuyIntentCallback callback) {
    new AsyncTask<Void, Void, PendingIntent>() {
      @Override
      protected PendingIntent doInBackground(Void... voids) {
        try {
          if (service == null) {
            SzLog.e(TAG, "Trying to consume buying intent but billing service is null!");
            return null;
          }
          Bundle buyIntentBundle = service.getBuyIntent(
              3, Constants.PACKAGE_NAME, productId, "inapp", LicenseKeyUtils.getLicenseKey());
          int responseCode = buyIntentBundle.getInt("RESPONSE_CODE");
          if (responseCode == 0) {
            PendingIntent buyIntent = buyIntentBundle.getParcelable("BUY_INTENT");
            if (buyIntent == null) {
              SzLog.e(TAG, "getBuyIntent() returned a bad bundle");
            }
            return buyIntent;
          } else {
            SzLog.e(TAG, "getBuyIntent() error code: " + responseCode);
            return null;
          }
        } catch (RemoteException e) {
          SzLog.e(TAG, "Could not call getBuyIntent()", e);
          return null;
        }
      }

      @Override
      protected void onPostExecute(PendingIntent buyIntent) {
        if (buyIntent != null) {
          callback.onSuccess(buyIntent);
        } else {
          callback.onFailure();
        }
      }
    }.execute();
  }

  public static void getPurchasedPackNames(final IInAppBillingService service, final GetPurchasedPacksCallback callback) {
    getPurchases(service, new GetPurchasesCallback() {
      @Override
      public void onSuccess(List<Purchase> purchases) {
//        callback.onSuccess(FluentIterable.from(purchases)
//            .transform(new Function<Purchase, String>() {
//              @Override
//              public String apply(Purchase input) {
//                return PURCHASE_IDS_TO_PACK_NAMES.consume(input.productId);
//              }
//            })
//            .filter(Predicates.notNull())
//            .toList());
        callback.onSuccess(ImmutableList.of("expansion a", "expansion b", "expansion c", "expansion d"));
      }

      @Override
      public void onFailure() {
        callback.onFailure();
      }
    });
  }

  public static void getPurchases(final IInAppBillingService service, final GetPurchasesCallback callback) {
    new AsyncTask<Void, Void, List<Purchase>>() {
      @Override
      protected List<Purchase> doInBackground(Void... voids) {
        try {
          if (service == null) {
            SzLog.e(TAG, "Trying to update purchases but billing service is null!");
            return null;
          }
          Bundle purchasesBundle = service.getPurchases(3, Constants.PACKAGE_NAME, "inapp", null);
          int responseCode = purchasesBundle.getInt("RESPONSE_CODE");
          if (responseCode == 0) {
            List<String> productIds = purchasesBundle.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
            List<String> signatures = purchasesBundle.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
            List<Purchase> purchases = Lists.newArrayList();
            if (productIds != null && signatures != null && productIds.size() == signatures.size()) {
              for (int i = 0; i < productIds.size(); i++) {
                purchases.add(new Purchase(productIds.get(i), signatures.get(i)));
              }
              return purchases;
            } else {
              SzLog.e(TAG, "getPurchases() returned a bad bundle");
              return null;
            }
          } else {
            SzLog.e(TAG, "getPurchases() error code: " + responseCode);
            return null;
          }
        } catch (RemoteException e) {
          SzLog.e(TAG, "Could not call getPurchases()", e);
          return null;
        }
      }

      @Override
      protected void onPostExecute(List<Purchase> purchases) {
        if (purchases != null) {
          callback.onSuccess(purchases);
        } else {
          callback.onFailure();
        }
      }
    }.execute();
  }
}
