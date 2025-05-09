package com.slamzoom.android.common.intents;

import android.app.Activity;
import android.content.Intent;
import android.graphics.RectF;
import android.net.Uri;

import com.slamzoom.android.R;
import com.slamzoom.android.BuildFlags;
import com.slamzoom.android.common.data.UriUtils;
import com.slamzoom.android.ui.create.CreateActivity;
import com.slamzoom.android.ui.create.hotspotchooser.HotspotChooserActivity;
import com.slamzoom.android.ui.start.CreateTemplate;
import com.slamzoom.android.ui.start.StartActivity;

/**
 * Created by antrob on 9/21/16.
 */

public class Intents {
  public static void startCreateActivity(Activity currentActivity) {
    Intent intent = new Intent(currentActivity, CreateActivity.class);
    currentActivity.startActivity(intent);
  }

  public static void startCreateActivityWithMonaTemplate(Activity currentActivity) {
    final RectF MONA_LISA_PHONE_HOTSPOT = new RectF(0.16f, 0.75f, 0.28f, 0.87f);
    final RectF MONA_LISA_FACE_HOTSPOT = new RectF(0.35f, 0.15f, 0.55f, 0.35f);
    final CreateTemplate CREATE_TEMPLATE =
        new CreateTemplate(UriUtils.getUriFromRes(R.drawable.sz_mona_lisa_960x1280), MONA_LISA_FACE_HOTSPOT);
    Intent intent = new Intent(currentActivity, CreateActivity.class);
    intent.putExtra(Params.CREATE_TEMPLATE,CREATE_TEMPLATE);
    currentActivity.startActivity(intent);
  }

  public static void startNextActivityAfterSplash(Activity currentActivity) {
    Intent intent =
        new Intent(currentActivity, BuildFlags.SKIP_START_SCREEN ? CreateActivity.class : StartActivity.class);
    currentActivity.startActivity(intent);
  }

  public static void startImageChooser(Activity currentActivity) {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("image/*");
    currentActivity.startActivityForResult(
        Intent.createChooser(intent, "Select Image"),
        RequestCodes.REQUEST_SELECT_IMAGE);
  }

  public static void startHotspotChooser(Activity currentActivity, Uri imageUri, boolean fromChangeHotspotRequest) {
    Intent intent = new Intent(currentActivity, HotspotChooserActivity.class);
    intent.putExtra(Intent.EXTRA_STREAM, imageUri);
    intent.putExtra(Params.FROM_CHANGE_HOTSPOT_REQUEST, fromChangeHotspotRequest);
    currentActivity.startActivityForResult(intent, RequestCodes.REQUEST_SELECT_HOTSPOT);
  }
}
