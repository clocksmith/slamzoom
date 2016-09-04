package com.slamzoom.android.common;

import android.content.Context;
import android.os.Bundle;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clocksmith on 6/22/16.
 */
public class SzAnalytics {
  private static final String TAG = SzAnalytics.class.getSimpleName();

  public static class CustomEvent {
    public static final String GIF_TRANSFORM_MS = "gif_transform_ms";
    public static final String GIF_FILTER_MS = "gif_filter_ms";
    public static final String GIF_PIXELIZE_MS = "gif_pixelize_ms";
    public static final String GIF_ENCODE_MS = "gif_encode_ms";
    public static final String GIF_WRITE_MS = "gif_write_ms";

    public static final String GIF_GENERATED = "gif_generated";

    public static final String GIF_SAVED = "gif_saved";
    public static final String VIDEO_SAVED = "video_saved";
    public static final String GIF_SHARED = "gif_shared";
    public static final String VIDEO_SHARED = "video_saved";
  }

  public static class CustomParam {
    public static final String PACKAGE_NAME = "package_name";

    public static final String SIZE = "size";
    public static final String FPS = "fps";

    public static final String END_SCALE = "end_scale";
    public static final String END_TEXT_LENGTH = "text_length";
    public static final String DURATION_MS= "duration_ms";
    public static final String HAS_STOPPED = "has_stopped";
  }

  public static class Value {
    public static class ContentType {
      public static final String EFFECT_THUMBNAIL = "effect_thumbnail";
    }
  }

  public static Event newSelectContentEvent() {
    return new Event(FirebaseAnalytics.Event.SELECT_CONTENT);
  }

  public static Event newGifGeneratedEvent() {
    return new Event(CustomEvent.GIF_GENERATED);
  }

  public static Event newGifSavedEvent() {
    return new Event(CustomEvent.GIF_SAVED);
  }

  public static Event newVideoSavedEvent() {
    return new Event(CustomEvent.VIDEO_SAVED);
  }

  public static Event newGifSharedEvent() {
    return new Event(CustomEvent.GIF_SHARED);
  }

  public static Event newVideoSharedEvent() {
    return new Event(CustomEvent.VIDEO_SHARED);
  }

  public static class Event {
    private String mEvent;
    private Bundle mBundle;

    public Event(String event) {
      mEvent = event;
      mBundle = new Bundle();
    }

    public Event withContentType(String contentType) {
      mBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType);
      return this;
    }

    public Event withItemCategory(String itemCategory) {
      mBundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, itemCategory);
      return this;
    }

    public Event withItemId(String itemId) {
      mBundle.putString(FirebaseAnalytics.Param.ITEM_ID, itemId);
      return this;
    }

    public Event withItemName(String itemName) {
      mBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, itemName);
      return this;
    }

    // Custom

    public Event withPackageName(String packageName) {
      mBundle.putString(CustomParam.PACKAGE_NAME, packageName);
      return this;
    }

    public Event withGifSize(int gifSize) {
      mBundle.putLong(CustomParam.SIZE, gifSize);
      return this;
    }

    public Event withFps(int fps) {
      mBundle.putLong(CustomParam.FPS, fps);
      return this;
    }

    public Event withEndScale(double endScale) {
      mBundle.putDouble(CustomParam.END_SCALE, endScale);
      return this;
    }

    public Event withEndTextLength(int endTextLength) {
      mBundle.putDouble(CustomParam.END_TEXT_LENGTH, endTextLength);
      return this;
    }


    public Event withDurationMs(long value) {
      mBundle.putLong(CustomParam.DURATION_MS, value);
      return this;
    }

    public Event withHasStopped(boolean value) {
      mBundle.putLong(CustomParam.HAS_STOPPED, value ? 1 : 0);
      return this;
    }

    // Log

    public void log(Context context) {
      FirebaseAnalytics.getInstance(context).logEvent(mEvent, mBundle);

      List<String> bundleStrings = Lists.newArrayList();
      for (String key : mBundle.keySet()) {
        String value = String.valueOf(mBundle.get(key));
        if (value == null) {
          value = "";
        }
        bundleStrings.add(key + ": " + value);
      }
      SzLog.f(TAG, "EVENT: " + mEvent + ":\n" + Joiner.on("\n").join(bundleStrings));
    }
  }
}
