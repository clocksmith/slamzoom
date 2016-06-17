package com.slamzoom.android.ui.start;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.slamzoom.android.R;
import com.slamzoom.android.common.utils.AnimationUtils;
import com.slamzoom.android.ui.create.CreateActivity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by clocksmith on 4/5/16.
 */
public class StartActivity extends AppCompatActivity {
  private static final String TAG = StartActivity.class.getSimpleName();

  private static final int NUM_GIFS_IN_SHOW = 4;
  private static final int ANIMATION_DURATION_MS = 350;

  @Bind(R.id.createSlamzoomButton) Button mCreateSlamzoomButton;
  @Bind(R.id.gifImageView1) GifImageView mGifImageView1;
  @Bind(R.id.gifImageView2) GifImageView mGifImageView2;
  @Bind(R.id.gifImageView3) GifImageView mGifImageView3;
  @Bind(R.id.gifImageView4) GifImageView mGifImageView4;

  private List<GifImageView> mGifImageViews;
  private List<GifDrawable> mGifDrawables;
  private Map<Integer, AnimationListener> mGifAnimationListeners;

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

    mGifImageViews = Lists.newArrayList(mGifImageView1, mGifImageView2, mGifImageView3, mGifImageView4);
    mGifDrawables = Lists.newArrayList();
    for (int gifNum = 1; gifNum <= NUM_GIFS_IN_SHOW; gifNum++) {
      try {
        mGifDrawables.add(new GifDrawable(getAssets(), "start_" + gifNum + ".gif"));
      } catch (IOException e) {
        Log.e(TAG, "Could not open gif from assets");
      }
    }

    for (int i = 0; i < NUM_GIFS_IN_SHOW; i++) {
      final GifImageView currentGifImageView = mGifImageViews.get(i);
      final GifDrawable currentGifDrawable = mGifDrawables.get(i);

      currentGifImageView.setScaleX(0f);
      currentGifImageView.setScaleY(0f);
      currentGifImageView.setImageDrawable(currentGifDrawable);
      currentGifDrawable.stop();
      currentGifDrawable.seekToFrame(0);
    }

    mGifAnimationListeners = Maps.newHashMap();

    runShow(0);
  }


  private void showGifImageView(int gifIndex) {
    for (int i = 0; i < NUM_GIFS_IN_SHOW; i++) {
      mGifImageViews.get(i).setVisibility(i == gifIndex ? View.VISIBLE : View.GONE);
    }
  }

  private void runShow(final int step) {
    final int gifIndex = step % NUM_GIFS_IN_SHOW;
    final GifImageView currentGifImageView = mGifImageViews.get(gifIndex);
    final GifDrawable currentGifDrawable = mGifDrawables.get(gifIndex);
    showGifImageView(gifIndex);

    AnimatorSet scaleUp = AnimationUtils.getScaleUpSet(currentGifImageView);
    scaleUp.addListener(new AnimationUtils.OnAnimationEndOnlyListener() {
      @Override
      public void onAnimationEnd(Animator animation) {
        if (mGifAnimationListeners.containsKey(gifIndex)) {
          currentGifDrawable.removeAnimationListener(mGifAnimationListeners.get(gifIndex));
        }

       AnimationListener animationListener = new AnimationListener() {
          @Override
          public void onAnimationCompleted(int loopNumber) {
            new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                currentGifDrawable.stop();
                AnimatorSet scaleDown = AnimationUtils.getScaleDownSet(currentGifImageView);
                scaleDown.addListener(new AnimationUtils.OnAnimationEndOnlyListener() {
                  @Override
                  public void onAnimationEnd(Animator animation) {
                    new Handler().post(new Runnable() {
                      @Override
                      public void run() {
                        runShow(step + 1);
                      }
                    });
                  }});
                scaleDown.start();
              }
            }, gifIndex == 0 ? 1000 : 0);
          }
        };
        mGifAnimationListeners.put(gifIndex, animationListener);
        currentGifDrawable.addAnimationListener(animationListener);
        currentGifDrawable.start();
      }
    });
    currentGifDrawable.seekToFrame(0);
    scaleUp.start();
  }
}

