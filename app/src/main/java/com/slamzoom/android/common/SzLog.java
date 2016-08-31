package com.slamzoom.android.common;

import android.util.Log;

import com.google.common.collect.ImmutableSet;
import com.google.firebase.crash.FirebaseCrash;
import com.slamzoom.android.mediacreation.MediaCreator;
import com.slamzoom.android.mediacreation.gif.GifCreator;
import com.slamzoom.android.mediacreation.gif.GifService;
import com.slamzoom.android.mediacreation.video.VideoFrame;
import com.slamzoom.android.ui.create.CreateActivity;
import com.slamzoom.android.ui.create.effectchooser.EffectThumbnailViewHolder;

import java.util.Set;

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
      MediaCreator.class.getSimpleName(),
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
    Log.e(tag, message, e);
    FirebaseCrash.log(message);
    if (e != null) {
      FirebaseCrash.report(e);
    }
  }
}
