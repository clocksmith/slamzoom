package com.slamzoom.android.ui.start;

import android.content.Intent;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RawRes;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
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

//  private static final @RawRes int VIDEO_RES = R.raw.mona_sz2;
  private static final int HTML_RES_ID = R.raw.index;

//  @BindView(R.id.backgroundWebView) WebView mBackgroundWebView;
//  @BindView(R.id.backgroundGifView) GifImageView mBackgroundGifView;
  @BindView(R.id.backgroundVideoView) TextureView backgroundVideoView;
  @BindView(R.id.tapAnywhereToBeginText) TextView mTapAnywhereToBeginText;
  @BindView(R.id.privacyPolicyLink) Button mPrivacyPolicyLink;
  @BindView(R.id.termsOfUseLink) Button mTermsOfUseLink;
  @BindView(R.id.tutorialLink) Button mTutorialLink;

  private MediaPlayer mMediaPlayer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_start);
    ButterKnife.bind(this);

    // TODO(clocksmith): Is there a way to set a custom default font yet?
    mTapAnywhereToBeginText.setTypeface(FontLoader.getInstance().getDefaultFont());
    mPrivacyPolicyLink.setTypeface(FontLoader.getInstance().getDefaultFont());
    mTermsOfUseLink.setTypeface(FontLoader.getInstance().getDefaultFont());
    mTutorialLink.setTypeface(FontLoader.getInstance().getDefaultFont());

    // Little hack to disable all caps since these are "links", but we used buttons to get the ripple effect.
    mPrivacyPolicyLink.setTransformationMethod(null);
    mTermsOfUseLink.setTransformationMethod(null);
    mTutorialLink.setTransformationMethod(null);

//    mBackgroundWebView.getSettings().setJavaScriptEnabled(true);
//    mBackgroundGifView.setImageURI(UriUtils.getUriFromRes(R.drawable.mona_slamin));
//    backgroundVideoView.setSurfaceTextureListener(new TextureListener());

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

    mTutorialLink.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(StartActivity.this, TutorialActivity.class));
      }
    });
  }

//  @Override
//  protected void onResume() {
//    super.onResume();
//    mBackgroundWebView.loadUrl(UriUtils.getUrlStringFromAssets("bg.html"));
//  }
//
//  protected void onPause() {
//    super.onPause();
//    if (Build.VERSION.SDK_INT < 18) {
//      mBackgroundWebView.clearView();
//    } else {
//      mBackgroundWebView.loadUrl("about:blank");
//    }
//    mBackgroundWebView.destroyDrawingCache();
//  }

//  private void fadeInMainView() {
//    AnimatorSet set = new AnimatorSet();
//    ObjectAnimator textureFadeOut = ObjectAnimator.ofFloat(mTextureView, "alpha", 1, 0);
//    ObjectAnimator mainFadeIn = ObjectAnimator.ofFloat(mMainView, "alpha", 0, 1);
//    set.setDuration(2000);
//    set.playTogether(textureFadeOut, mainFadeIn);
//    set.start();
//    new Handler().postDelayed(new Runnable() {
//      @Override
//      public void run() {
//        ObjectAnimator imageFadeIn = ObjectAnimator.ofFloat(mMonaLisaImageView, "alpha", 0, 0.5f);
//        imageFadeIn.setDuration(2000);
//        imageFadeIn.start();
//
//        mReplayButton.setOnClickListener(new View.OnClickListener() {
//          @Override
//          public void onClick(View v) {
//            resetViewAndStartShow();
//          }
//        });
//      }
//    }, 1000);
//  }

//  private class TextureListener implements TextureView.SurfaceTextureListener {
//    @Override
//    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
//      try {
//        mMediaPlayer= new MediaPlayer();
//        mMediaPlayer.setDataSource(StartActivity.this, UriUtils.getUriFromRes(VIDEO_RES));
//        mMediaPlayer.setSurface(new Surface(surfaceTexture));
//        mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
//          @Override
//          public void onVideoSizeChanged(MediaPlayer mp, int videoWidth, int videoHeight) {
//            Display display = getWindowManager().getDefaultDisplay();
//            Point size = new Point();
//            display.getSize(size);
//            int screenWidth = size.x;
//            int screenHeight = size.y;
//            double screenAspect = (double) screenWidth / screenHeight;
//            double videoAspect = (double) videoWidth / videoHeight;
//            if (screenAspect < videoAspect) {
//              mTextureView.getLayoutParams().width = (int) (screenHeight * videoAspect);
//              mTextureView.getLayoutParams().height = screenHeight;
//            } else {
//              mTextureView.getLayoutParams().width = screenWidth;
//              mTextureView.getLayoutParams().height = (int) (screenWidth * videoAspect);
//            }
//            mTextureView.requestLayout();
//          }
//        });
//        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//          @Override
//          public void onCompletion(MediaPlayer mp) {
//            fadeInMainView();
//          }
//        });
//        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//          @Override
//          public void onPrepared(MediaPlayer mp) {
//            mMediaPlayer.start();
//          }
//        });
//        mMediaPlayer.prepareAsync();
//      } catch (Exception e) {
//        SzLog.e(TAG, "Could not play video", e);
//        fadeInMainView();
//      }
//    }
//
//    @Override
//    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
//      // Do nothing.
//    }
//
//    @Override
//    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
//      return false;
//    }
//
//    @Override
//    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//      // Do nothing.
//    }
//  }
}

