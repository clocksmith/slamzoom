package com.slamzoom.android.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by clocksmith on 9/1/16.
 */
public class SplashActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = new Intent(this, StartActivity.class);
    startActivity(intent);
    finish();
  }
}
