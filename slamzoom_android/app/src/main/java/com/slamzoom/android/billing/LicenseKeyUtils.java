package com.slamzoom.android.billing;

/**
 * Created by clocksmith on 7/7/16.
 */
public class LicenseKeyUtils {

  private static String getLicenseKeyPart() {
    return "<r_key>";
  }

  public static String getLicenseKey() {
        new StringBuilder(getLicenseKeyPart()).reverse().toString();
  }

}
