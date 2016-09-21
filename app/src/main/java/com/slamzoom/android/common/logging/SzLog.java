package com.slamzoom.android.common.logging;

import android.util.Log;

import com.google.common.collect.ImmutableSet;
import com.slamzoom.android.common.activities.LifecycleLoggingActivity;
import com.slamzoom.android.ui.create.CreateActivity;

import java.util.Set;

//import com.google.firebase.crash.FirebaseCrash;

/**
 * Created by clocksmith on 6/14/16.
 *
 * Custom logger class that can do filtered logging and crash logging to firebase.
 */
public class SzLog {
  // NOTE: Please keep in alphabetical order.
  private static Set<String> mAcceptedTags = ImmutableSet.of(
//      CreateActivity.class.getSimpleName(),
//      EffectThumbnailViewHolder.class.getSimpleName(),
//      GifCreator.class.getSimpleName(),
//      GifCreatorManager.class.getSimpleName(),
//      GifService.class.getSimpleName(),
      LifecycleLoggingActivity.getFullTag(CreateActivity.class.getSimpleName()),
//      LifecycleLoggingActivity.getFullTag(HotspotChooserActivity.class.getSimpleName()),
//      LifecycleLoggingActivity.getFullTag(StartActivity.class.getSimpleName()),
//      LifecycleLoggingActivity.getFullTag(HotspotChooserActivity.class.getSimpleName()),
//      MediaCreator.class.getSimpleName(),
//      SzAnalytics.class.getSimpleName(),
//      VideoFrame.class.getSimpleName(),

      "" // This is so we can always have a trailing comma.
  );

  public static void f(String tag, String message) {
    if (mAcceptedTags.contains(tag)) {
      Log.d("F/" + tag, message);
    } else {
      Log.d(tag, message);
    }
  }

  public static void e(String tag, String message) {
    e(tag, message, null);
  }

  public static void e(String tag, String message, Exception e) {
    Log.e("F/" + tag, message, e);
    if (e != null) {
//      FirebaseCrash.report(e);
    } else {
//      FirebaseCrash.report(new Exception(message));
    }
  }
}
