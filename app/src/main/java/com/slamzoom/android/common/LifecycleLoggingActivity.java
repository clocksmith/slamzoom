package com.slamzoom.android.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by clocksmith on 9/6/16.
 */
public class LifecycleLoggingActivity extends AppCompatActivity {
  private static final String TAG = LifecycleLoggingActivity.class.getSimpleName();

  private String mSubTag = "";

  protected void setSubTag(String tag) {
    mSubTag = tag;
  }

  private String getFullTagInternal() {
    return getFullTag(mSubTag);
  }

  public static String getFullTag(String subTag) {
    return TAG + "/" + subTag;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SzLog.f(getFullTagInternal(), "onCreate()");
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    SzLog.f(getFullTagInternal(), "onNewIntent()");
  }

  @Override protected void onStart() {
    super.onStart();
//    SzLog.f(getFullTagInternal(), "onStart()");
  }

  @Override protected void onResume() {
    super.onResume();
    SzLog.f(getFullTagInternal(), "onResume()");
  }

  @Override protected void onPause() {
    super.onPause();
    SzLog.f(getFullTagInternal(), "onPause()");
  }

  @Override protected void onStop() {
    super.onStop();
//    SzLog.f(getFullTagInternal(), "onStop()");
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    SzLog.f(TAG, "onSaveInstanceState()");
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    SzLog.f(TAG, "onDestroy()");
  }
}
