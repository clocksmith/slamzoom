package com.slamzoom.android.ui.start;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.RawRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.slamzoom.android.R;
import com.slamzoom.android.common.FontLoader;
import com.slamzoom.android.common.MutedVideoView;
import com.slamzoom.android.common.utils.AnimationUtils;
import com.slamzoom.android.common.utils.DebugUtils;
import com.slamzoom.android.ui.create.CreateActivity;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by clocksmith on 4/5/16.
 */
public class StartActivity extends AppCompatActivity {
  private static final @RawRes int VIDEO_NAME = R.raw.out_politics_no_watermark;
  private static final int VIDEO_DURATION_MILLIS = 8000;


  @Bind(R.id.createButton) Button mCreateButton;
  @Bind(R.id.videoView) MutedVideoView mVideoView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_start);
    ButterKnife.bind(this);

    if (DebugUtils.SKIP_START_SCREEN) {
      startCreateActivity();
    }

    mCreateButton.setTypeface(FontLoader.getInstance().getDefaultFont());
    mCreateButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startCreateActivity();
      }
    });

    mVideoView.setVideoPath("android.resource://" + getPackageName() + "/" + VIDEO_NAME);
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
    startVideo();
  }

  @Override
  protected void onPause() {
    super.onPause();
//    mVideoView.setAlpha(0);
//    mVideoView.stopPlayback();
  }

  private void startCreateActivity() {
    startActivity(new Intent(StartActivity.this, CreateActivity.class));
  }

  private void startVideo() {
    int startTime = (new Random()).nextInt(VIDEO_DURATION_MILLIS);
    mVideoView.seekTo(startTime);
    mVideoView.start();
    ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mVideoView, "alpha", 0, 0.75f);
    alphaAnimator.setDuration(500 );
    alphaAnimator.start();
  }
}

