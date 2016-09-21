package com.slamzoom.android.common.intents;

import android.app.Activity;
import android.content.Intent;
import android.graphics.RectF;

import com.slamzoom.android.R;
import com.slamzoom.android.BuildFlags;
import com.slamzoom.android.common.data.UriUtils;
import com.slamzoom.android.ui.create.CreateActivity;
import com.slamzoom.android.ui.start.CreateTemplate;
import com.slamzoom.android.ui.start.StartActivity;

/**
 * Created by antrob on 9/21/16.
 */

public class IntentUtils {
  public static void startCreateActivityWithMonaTemplate(Activity currentActivity) {
    final RectF MONA_LISA_PHONE_HOTSPOT = new RectF(0.16f, 0.75f, 0.28f, 0.87f);
    final CreateTemplate CREATE_TEMPLATE =
        new CreateTemplate(UriUtils.getUriFromRes(R.drawable.mona_lisa_sz_1920x2560), MONA_LISA_PHONE_HOTSPOT);
    Intent intent = new Intent(currentActivity, CreateActivity.class);
    intent.putExtra(Params.CREATE_TEMPLATE,CREATE_TEMPLATE);
    currentActivity.startActivity(intent);
  }

  public static void startNextActivityAfterSplash(Activity currentActivity) {
    Intent intent =
        new Intent(currentActivity, BuildFlags.SKIP_START_SCREEN ? CreateActivity.class : StartActivity.class);
    currentActivity.startActivity(intent);
  }
}
