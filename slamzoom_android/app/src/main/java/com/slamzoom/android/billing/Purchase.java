package com.slamzoom.android.billing;

/**
 * Created by clocksmith on 7/7/16.
 */
public class Purchase {
  public String productId;
  public String dataSignature;

  public Purchase(String productId, String dataSignature) {
    this.productId = productId;
    this.dataSignature = dataSignature;
  }
}
