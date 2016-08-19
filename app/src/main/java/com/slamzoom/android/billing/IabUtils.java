package com.slamzoom.android.billing;

import android.app.PendingIntent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.android.vending.billing.IInAppBillingService;
import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.BiMap;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.utils.DebugUtils;
import com.slamzoom.android.common.utils.SzLog;
import com.slamzoom.android.effects.EffectPack;
import com.slamzoom.android.effects.EffectPacks;

import java.util.List;

/**
 * Created by clocksmith on 7/7/16.
 */
public class IabUtils {
  private static final String TAG = IabUtils.class.getSimpleName();

  private static final ImmutableList<String> GIFTED_PACK_NAMES = ImmutableList.of(EffectPacks.Pack.ONE.getName());

  // TODO(clocksmith): This would be perfect for remote config. Maybe Extract elsewhere to also hide packs not for sale.
  private static final BiMap<String, String> PURCHASE_IDS_TO_PACK_NAMES = ImmutableBiMap.of(
      "packs.v1.2", EffectPacks.Pack.TWO.getName(),
      "packs.v1.3", EffectPacks.Pack.THREE.getName(),
      "packs.v1.4", EffectPacks.Pack.FOUR.getName(),
      "packs.v1.5", EffectPacks.Pack.FIVE.getName());

  public static void getBuyIntentByPack(
      final String packName,
      final IInAppBillingService service,
      final GetBuyIntentCallback callback) {
    getBuyIntentByProduct(PURCHASE_IDS_TO_PACK_NAMES.inverse().get(packName), service, callback);
  }

  public static void getBuyIntentByProduct(
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
          SzLog.f(TAG, "getBuyIntent for productId: " + productId);
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

        if (DebugUtils.UNLOCK_ALL_PACKS) {
          callback.onSuccess(
              ImmutableList.copyOf(Iterables.concat(GIFTED_PACK_NAMES, PURCHASE_IDS_TO_PACK_NAMES.values())));
        } else {
          // TODO(clocksmith): make this add purchased packs.
          callback.onSuccess(Lists.newArrayList(Iterables.concat(GIFTED_PACK_NAMES, FluentIterable.from(purchases)
              .transform(new Function<Purchase, String>() {
                @Override
                public String apply(Purchase input) {
                  // TODO(clocksmith): verify.
                  return PURCHASE_IDS_TO_PACK_NAMES.get(input.productId);
                }
              })
              .filter(Predicates.notNull())
              .toList())));
        }
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
