package com.slamzoom.android.billing;

/**
 * Created by clocksmith on 7/7/16.
 */
public class LicenseKeyUtils {
  private static final String LICENSE_KEY_PART_4 = "VehYU8WhhbmRNnHup3wvtuYZmzkU4aPgj3+2gbaGVY09+7OxsPmOkahWAwa9v8DM9NF";
  private static final String LICENSE_KEY_PART_1 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmS0X8k1AwKXmSW01mPtgBdx";
  private static final String LICENSE_KEY_PART_2 = "Oens+n4Ah/zZbGe5fr1dpUDcixWdRSULt3tHk1k8gvfmq+3MSCiUwVn1HfUlP1NJa9D";
  private static final String LICENSE_KEY_PART_5 = "k2YWs0SOVvfD3t7k+ywQO8E1Hjir8bZ6YWouALhDyEOZC1jn5aQVTqggDhbpr/v74T2";
  private static final String LICENSE_KEY_PART_3 = "xqNquzazdCGwiug099AQ2u6628+n6i9KsJctJ3JvW6okcEz35/ZcyBUEKxMR9yBK7gX";

  private static String getLicenseKeyPart6() {
    return "UC3N85M5p3cCcnPHeihruXmbuf4BGgW2dFIzpo5/7FOYl8fm0fQIDAQAB";
  }

  public static String getLicenseKey() {
    return LICENSE_KEY_PART_1 +
        LICENSE_KEY_PART_2 +
        LICENSE_KEY_PART_3 +
        LICENSE_KEY_PART_4 +
        LICENSE_KEY_PART_5 +
        new StringBuilder(getLicenseKeyPart6()).reverse().toString();
  }

}
