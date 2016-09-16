package com.slamzoom.android.ui.start;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RawRes;
import android.view.Display;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slamzoom.android.R;
import com.slamzoom.android.common.FontLoader;
import com.slamzoom.android.common.LifecycleLoggingActivity;
import com.slamzoom.android.common.SzLog;
import com.slamzoom.android.common.utils.AnimationUtils;
import com.slamzoom.android.common.utils.DebugUtils;
import com.slamzoom.android.common.utils.UriUtils;
import com.slamzoom.android.ui.create.CreateActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clocksmith on 4/5/16.
 */
public class StartActivity extends LifecycleLoggingActivity {
  private static final String TAG = StartActivity.class.getSimpleName();

  private static final @RawRes int VIDEO_RES = R.raw.mona_sz2;

  @BindView(R.id.textureView) TextureView mTextureView;
  @BindView(R.id.monaLisaImageView) ImageView mMonaLisaImageView;
  @BindView(R.id.mainView) LinearLayout mMainView;
  @BindView(R.id.replayButton) LinearLayout mReplayButton;
  @BindView(R.id.replayIntroText) TextView mReplayText;
  @BindView(R.id.createText) TextView mCreateTextView;

  private MediaPlayer mMediaPlayer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_start);
    ButterKnife.bind(this);

    mReplayText.setTypeface(FontLoader.getInstance().getDefaultFont());
    mCreateTextView.setTypeface(FontLoader.getInstance().getDefaultFont());

    initAlphas();

    if (DebugUtils.REPORT_FAKE_ERROR_ON_START) {
      SzLog.e(TAG, "NOT A REAL ERROR");
    }

    if (DebugUtils.SKIP_START_SCREEN) {
      startCreateActivity();
    }

    mTextureView.setSurfaceTextureListener(new TextureListener());

    mMainView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startCreateActivity();
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    initAlphas();
  }

  private void initAlphas() {
    mTextureView.setAlpha(1f);
    mMonaLisaImageView.setAlpha(0f);
    mMainView.setAlpha(0);
  }

  private void resetView() {
    mReplayButton.setOnClickListener(null);
    mMediaPlayer.seekTo(0);
  }

  private void fadeInMainView() {
    AnimatorSet set = new AnimatorSet();
    ObjectAnimator textureFadeOut = ObjectAnimator.ofFloat(mTextureView, "alpha", 1, 0);
    ObjectAnimator mainFadeIn = ObjectAnimator.ofFloat(mMainView, "alpha", 0, 1);
    set.setDuration(2000);
    set.playTogether(textureFadeOut, mainFadeIn);
    set.start();
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        ObjectAnimator imageFadeIn = ObjectAnimator.ofFloat(mMonaLisaImageView, "alpha", 0, 0.5f);
        imageFadeIn.setDuration(2000);
        imageFadeIn.start();

        mReplayButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            resetViewAndStartShow();
          }
        });
      }
    }, 1000);
  }

  private void resetViewAndStartShow() {
    resetView();
    AnimatorSet set = new AnimatorSet();
    ObjectAnimator textureFadeIn = ObjectAnimator.ofFloat(mTextureView, "alpha", 0, 1);
    ObjectAnimator mainFadeOut = ObjectAnimator.ofFloat(mMainView, "alpha", 1, 0);
    ObjectAnimator imageFadeOut = ObjectAnimator.ofFloat(mMonaLisaImageView, "alpha", 0.5f, 0);
    set.setDuration(1000);
    set.playTogether(textureFadeIn, mainFadeOut, imageFadeOut);
    set.addListener(new AnimationUtils.OnAnimationEndOnlyListener() {
      @Override
      public void onAnimationEnd(Animator animation) {
        mMediaPlayer.start();
      }
    });
    set.start();
  }

  private void startCreateActivity() {
    startActivity(new Intent(StartActivity.this, CreateActivity.class));
  }

  private class TextureListener implements TextureView.SurfaceTextureListener {
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
      try {
        mMediaPlayer= new MediaPlayer();
        mMediaPlayer.setDataSource(StartActivity.this, UriUtils.getUriFromRes(VIDEO_RES));
        mMediaPlayer.setSurface(new Surface(surfaceTexture));
        mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
          @Override
          public void onVideoSizeChanged(MediaPlayer mp, int videoWidth, int videoHeight) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int screenWidth = size.x;
            int screenHeight = size.y;
            double screenAspect = (double) screenWidth / screenHeight;
            double videoAspect = (double) videoWidth / videoHeight;
            if (screenAspect < videoAspect) {
              mTextureView.getLayoutParams().width = (int) (screenHeight * videoAspect);
              mTextureView.getLayoutParams().height = screenHeight;
            } else {
              mTextureView.getLayoutParams().width = screenWidth;
              mTextureView.getLayoutParams().height = (int) (screenWidth * videoAspect);
            }
            mTextureView.requestLayout();
          }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
          @Override
          public void onCompletion(MediaPlayer mp) {
            fadeInMainView();
          }
        });
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
          @Override
          public void onPrepared(MediaPlayer mp) {
            mMediaPlayer.start();
          }
        });
        mMediaPlayer.prepareAsync();
      } catch (Exception e) {
        SzLog.e(TAG, "Could not play video", e);
        fadeInMainView();
      }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
      // Do nothing.
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
      return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
      // Do nothing.
    }
  }
}

