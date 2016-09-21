package com.slamzoom.android.ui.start;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.slamzoom.android.R;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.SzLog;
import com.slamzoom.android.common.preferences.Preferences;
import com.slamzoom.android.common.utils.DebugUtils;
import com.slamzoom.android.common.utils.UriUtils;
import com.slamzoom.android.ui.create.CreateActivity;

/**
 * Created by clocksmith on 9/1/16.
 */
public class SplashActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Stupid hack for firebase crash mappings.
    if (DebugUtils.REPORT_FAKE_ERROR_ON_START) {
      SzLog.e("NOT A REAL ERROR", "NOT A REAL ERROR");
    }

    if (DebugUtils.USE_MONA_TEMPLATE) {
      final RectF MONA_LISA_PHONE_HOTSPOT = new RectF(0.16f, 0.75f, 0.28f, 0.87f);
      final CreateTemplate CREATE_TEMPLATE =
          new CreateTemplate(UriUtils.getUriFromRes(R.drawable.mona_lisa_sz_1920x2560), MONA_LISA_PHONE_HOTSPOT);
      Intent intent = new Intent(this, CreateActivity.class);
      intent.putExtra(Constants.CREATE_TEMPLATE, CREATE_TEMPLATE);
      this.startActivity(intent);
      finish();
    } else {
      Intent intent = new Intent(this, DebugUtils.SKIP_START_SCREEN ? CreateActivity.class : StartActivity.class);
      startActivity(intent);
      finish();
    }
  }
}
