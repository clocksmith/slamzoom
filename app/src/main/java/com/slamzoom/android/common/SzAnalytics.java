package com.slamzoom.android.common;

import android.content.Context;
import android.os.Bundle;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

/**
 * Created by clocksmith on 6/22/16.
 */
public class SzAnalytics {
  private static final String TAG = SzAnalytics.class.getSimpleName();

  public static class CustomEvent {
    public static final String THUMBNAIL_GIF_GENERATED = "thumbnail_gif_generated";
    public static final String MAIN_GIF_GENERATED = "main_gif_generated";

    public static final String GIF_SAVED = "gif_saved";
    public static final String GIF_SHARED = "gif_shared";
    public static final String VIDEO_SAVED = "video_saved";
    public static final String VIDEO_SHARED = "video_shared";
  }

  public static class CustomParam {
    public static final String DURATION_MS= "duration_ms";
    public static final String HOTSPOT_SCALE = "hotspot_scale";
    public static final String END_TEXT_LENGTH = "end_text_length";
    public static final String PACKAGE_NAME = "package_name";
  }

  public static class ContentType {
    public static final String IMAGE = "image";
    public static final String EFFECT = "effect";
    public static final String CHANGE_IMAGE = "change_image";
    public static final String CHANGE_HOTSPOT = "change_hotspot";
  }


  // Select Content

  private static Event newSelectContentEvent() {
    return new Event(FirebaseAnalytics.Event.SELECT_CONTENT);
  }

  public static Event newSelectImageEvent() {
    return newSelectContentEvent().withContentType(ContentType.IMAGE);
  }


  public static Event newSelectEffectEvent() {
    return newSelectContentEvent().withContentType(ContentType.EFFECT);
  }

  public static Event newSelectChangeImageEvent() {
    return newSelectContentEvent().withContentType(ContentType.CHANGE_IMAGE);
  }

  public static Event newSelectChangeHotspotEvent() {
    return newSelectContentEvent().withContentType(ContentType.CHANGE_HOTSPOT);
  }

  // Custom

  public static Event newMainGifGeneratedEvent() {
    return new Event(CustomEvent.MAIN_GIF_GENERATED);
  }

  public static Event newThumbnailGifGeneratedEvent() {
    return new Event(CustomEvent.THUMBNAIL_GIF_GENERATED);
  }

  public static Event newGifSavedEvent() {
    return new Event(CustomEvent.GIF_SAVED);
  }

  public static Event newGifSharedEvent() {
    return new Event(CustomEvent.GIF_SHARED);
  }

  public static Event newVideoSavedEvent() {
    return new Event(CustomEvent.VIDEO_SAVED);
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
      // Firebase apparently fails silently here if we don't do this. Bad.
      if (packageName.length() > 36) {
        packageName = packageName.substring(0, 36);
      }
      mBundle.putString(CustomParam.PACKAGE_NAME, packageName);
      return this;
    }

    public Event withHotspotScale(double endScale) {
      mBundle.putDouble(CustomParam.HOTSPOT_SCALE, endScale);
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
