package com.slamzoom.android.ui.start;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.slamzoom.android.common.intents.RequestCodes;
import com.slamzoom.android.common.logging.SzLog;
import com.slamzoom.android.BuildFlags;
import com.slamzoom.android.common.intents.Intents;

/**
 * Created by clocksmith on 9/1/16.
 */
public class SplashActivity extends AppCompatActivity {
  private static final String TAG = SplashActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
    if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) != ConnectionResult.SUCCESS) {
      SzLog.e(TAG, "User does not have google play services.");
      GoogleApiAvailability.getInstance().getErrorDialog(
          this, code, RequestCodes.REQUEST_GOOGLE_PLAY_SERVICES_ERROR_DIALOG);
    }

    // TODO(clocksmith): Remove this stupid hack for firebase crash mappings.
    if (BuildFlags.REPORT_FAKE_ERROR_ON_START) {
      SzLog.e("NOT A REAL ERROR", "NOT A REAL ERROR");
    }

    if (BuildFlags.USE_MONA_TEMPLATE) {
      Intents.startCreateActivityWithMonaTemplate(this);
    } else {
      Intents.startNextActivityAfterSplash(this);
    }

    // We never want to be able to go back to the splash screen.
    finish();
  }
}
