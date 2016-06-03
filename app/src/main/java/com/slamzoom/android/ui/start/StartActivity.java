package com.slamzoom.android.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.slamzoom.android.R;
import com.slamzoom.android.ui.create.CreateActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by clocksmith on 4/5/16.
 */
public class StartActivity extends AppCompatActivity {
  @Bind(R.id.createSlamzoomButton) Button mCreateSlamzoomButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_start);
    ButterKnife.bind(this);

    mCreateSlamzoomButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(StartActivity.this, CreateActivity.class));
      }
    });
  }
}
