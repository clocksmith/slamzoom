package com.slamzoom.android.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by clocksmith on 9/6/16.
 */
public class LifecycleLoggingActivity extends AppCompatActivity {
  private static final String TAG = LifecycleLoggingActivity.class.getSimpleName();

  private String mTag = "";

  protected void setTag(String tag) {
    mTag = tag;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SzLog.f(TAG, mTag + ":" + "onCreate()");
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    SzLog.f(TAG, mTag + ":" + "onNewIntent()");
  }

  @Override protected void onStart() {
    super.onStart();
    SzLog.f(TAG, mTag + ":" + "onStart()");
  }

  @Override protected void onResume() {
    super.onResume();
    SzLog.f(TAG, mTag + ":" + "onResume()");
  }

  @Override protected void onPause() {
    super.onPause();
    SzLog.f(TAG, mTag + ":" + "onPause()");
  }

  @Override protected void onStop() {
    super.onStop();
    SzLog.f(TAG, mTag + ":" + "onStop()");
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    SzLog.f(TAG, mTag + ":" + "onSaveInstanceState()");
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    SzLog.f(TAG, mTag + ":" + "onDestroy()");
  }
}
