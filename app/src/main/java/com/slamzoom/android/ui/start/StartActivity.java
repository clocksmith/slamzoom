package com.slamzoom.android.ui.start;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.slamzoom.android.R;
import com.slamzoom.android.common.FontLoader;
import com.slamzoom.android.common.utils.DebugUtils;
import com.slamzoom.android.ui.create.CreateActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by clocksmith on 4/5/16.
 */
public class StartActivity extends AppCompatActivity {
  @Bind(R.id.createSlamzoomButton) Button mCreateSlamzoomButton;
  @Bind(R.id.videoView) VideoView mVideoView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_start);
    ButterKnife.bind(this);

    if (DebugUtils.SKIP_START_SCREEN) {
      startCreateActivity();
    }

    mCreateSlamzoomButton.setTypeface(FontLoader.getInstance().getDefaultFont());
    mCreateSlamzoomButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startCreateActivity();
      }
    });

    mVideoView.setVideoURI(Uri.parse("android.resource://"+  getPackageName() + "/raw/out_politics_no_watermark"));
    mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      @Override
      public void onPrepared(MediaPlayer mp) {
        mp.setLooping(true);
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    mVideoView.start();
  }

  private void startCreateActivity() {
    startActivity(new Intent(StartActivity.this, CreateActivity.class));
  }
}

