package com.slamzoom.android.ui.start;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RawRes;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import com.slamzoom.android.R;
import com.slamzoom.android.common.FontLoader;
import com.slamzoom.android.common.LifecycleLoggingActivity;
import com.slamzoom.android.common.SzLog;
import com.slamzoom.android.common.utils.UriUtils;
import com.slamzoom.android.ui.create.CreateActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by clocksmith on 4/5/16.
 */
public class StartActivity extends LifecycleLoggingActivity {
  private static final String TAG = StartActivity.class.getSimpleName();

  private static final @RawRes int VIDEO_RES = R.raw.mona_slamio_640;
  private static final float FINAL_VIDEO_ALPHA = 0.5f;

  @BindView(R.id.backgroundVideoView) TextureView mBackgroundVideoView;
  @BindView(R.id.tapAnywhereToBeginText) TextView mTapAnywhereToBeginText;
  @BindView(R.id.privacyPolicyLink) Button mPrivacyPolicyLink;
  @BindView(R.id.termsOfUseLink) Button mTermsOfUseLink;

  private MediaPlayer mMediaPlayer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_start);
    ButterKnife.bind(this);

    // TODO(clocksmith): Is there a way to set a custom default font yet? (Yes there is, do it idiot)
    mTapAnywhereToBeginText.setTypeface(FontLoader.getInstance().getDefaultFont());
    mPrivacyPolicyLink.setTypeface(FontLoader.getInstance().getDefaultFont());
    mTermsOfUseLink.setTypeface(FontLoader.getInstance().getDefaultFont());
    // Little hack to disable all caps since these are "links", but we used buttons to get the ripple effect.
    mPrivacyPolicyLink.setTransformationMethod(null);
    mTermsOfUseLink.setTransformationMethod(null);

    mMediaPlayer = new MediaPlayer();
    mBackgroundVideoView.setSurfaceTextureListener(new TextureListener());

    mTapAnywhereToBeginText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(StartActivity.this, CreateActivity.class));
      }
    });

    mPrivacyPolicyLink.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        LocalWebViewDialogPresenter.show(StartActivity.this, "privacy.html");
      }
    });

    mTermsOfUseLink.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        LocalWebViewDialogPresenter.show(StartActivity.this, "terms.html");
      }
    });
  }

  private void fadeInVideo() {
    ObjectAnimator videoFadeIn = ObjectAnimator.ofFloat(mBackgroundVideoView, "alpha", 0, FINAL_VIDEO_ALPHA);
    videoFadeIn.setDuration(2000);
    videoFadeIn.start();
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
              mBackgroundVideoView.getLayoutParams().width = (int) (screenHeight * videoAspect);
              mBackgroundVideoView.getLayoutParams().height = screenHeight;
              ((ViewGroup.MarginLayoutParams) mBackgroundVideoView.getLayoutParams()).setMargins(
                  (screenWidth - mBackgroundVideoView.getLayoutParams().width) / 2,
                  0,
                  0,
                  0
              );
            } else {
              mBackgroundVideoView.getLayoutParams().width = screenWidth;
              mBackgroundVideoView.getLayoutParams().height = (int) (screenWidth * videoAspect);
              ((ViewGroup.MarginLayoutParams) mBackgroundVideoView.getLayoutParams()).setMargins(
                  0,
                    screenHeight - (mBackgroundVideoView.getLayoutParams().height - screenHeight) / 2,
                  0,
                  0
              );
            }
            mBackgroundVideoView.requestLayout();
          }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
          @Override
          public void onCompletion(MediaPlayer mp) {
            mMediaPlayer.start();
          }
        });
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
          @Override
          public void onPrepared(MediaPlayer mp) {
            fadeInVideo();
            mMediaPlayer.start();
          }
        });
        mMediaPlayer.prepareAsync();
      } catch (Exception e) {
        SzLog.e(TAG, "Could not play video", e);
        fadeInVideo();
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

