package com.slamzoom.android.ui.start;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.slamzoom.android.common.logging.SzLog;
import com.slamzoom.android.BuildFlags;
import com.slamzoom.android.common.intents.Intents;

/**
 * Created by clocksmith on 9/1/16.
 */
public class SplashActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

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
