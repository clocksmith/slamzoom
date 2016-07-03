package com.slamzoom.android.common.utils;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by clocksmith on 6/22/16.
 */
public class SzAnalytics {

  public static class CustomEvent {
    public static final String GIF_TRANSFORM_MS = "gif_transform_ms";
    public static final String GIF_FILTER_MS = "gif_filter_ms";
    public static final String GIF_PIXELIZE_MS = "gif_pixelize_ms";
    public static final String GIF_ENCODE_MS = "gif_encode_ms";
    public static final String GIF_WRITE_MS = "gif_write_ms";
    public static final String GIF_GENERATED = "gif_generated";
  }

  public static class CustomParam {
    public static final String DURATION_MS= "duration_ms";
    public static final String END_SCALE = "end_scale";
    public static final String GIF_SIZE = "gif_size";
    public static final String HAS_STOPPED = "has_stopped";
  }

  public static class Value {
    public static class ContentType {
      public static final String EFFECT_THUMBNAIL = "effect_thumbnail";
      public static final String PREVIEW_GIF_START = "preview_gif_start";
      public static final String MAIN_GIF_START = "preview_gif_start";
    }
  }

  public static Event newSelectContentEvent() {
    return new Event(FirebaseAnalytics.Event.SELECT_CONTENT);
  }

  public static Event newGifGeneratedEvent() {
    return new Event(CustomEvent.GIF_GENERATED);
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

    public Event withDurationMs(long value) {
      mBundle.putLong(CustomParam.DURATION_MS, value);
      return this;
    }

    public Event withEndScale(double endScale) {
      mBundle.putDouble(CustomParam.END_SCALE, endScale);
      return this;
    }

    public Event withGifSize(int gifSize) {
      mBundle.putLong(CustomParam.GIF_SIZE, gifSize);
      return this;
    }

    // Log

    public void log(Context context) {
      FirebaseAnalytics.getInstance(context).logEvent(mEvent, mBundle);
    }
  }

}
