package com.slamzoom.android.ui.start;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by clocksmith on 9/20/16.
 */
public class TutorialActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(new TutorialView(this));
  }
}
