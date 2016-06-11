package com.slamzoom.android.ui.start;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.common.collect.Lists;
import com.slamzoom.android.R;
import com.slamzoom.android.common.utils.AnimationUtils;
import com.slamzoom.android.ui.create.CreateActivity;

import java.io.IOException;
import java.util.List;

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

  private static final int NUM_GIFS_FOR_SHOW = 3;
  private static final int ANIMATION_DURATION_MS = 350;

  @Bind(R.id.createSlamzoomButton) Button mCreateSlamzoomButton;
  @Bind(R.id.gifImageView1) GifImageView mGifImageView1;
  @Bind(R.id.gifImageView2) GifImageView mGifImageView2;
  @Bind(R.id.gifImageView3) GifImageView mGifImageView3;

  private List<GifImageView> mGifImageViews;
  private List<GifDrawable> mGifDrawables;

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

    mGifImageViews = Lists.newArrayList(mGifImageView1, mGifImageView2, mGifImageView3);
    mGifDrawables = Lists.newArrayList();
    for (int gifNum = 1; gifNum <= NUM_GIFS_FOR_SHOW; gifNum++) {
      try {
        mGifDrawables.add(new GifDrawable(getAssets(), "start_" + gifNum + ".gif"));
      } catch (IOException e) {
        Log.e(TAG, "Could not open gif from assets");
      }
    }
    for (int i = 0; i < NUM_GIFS_FOR_SHOW; i++) {
      mGifImageViews.get(i).setImageDrawable(mGifDrawables.get(i));
    }
    runShow(1);
  }

  private void showGifDrawable(int index) {
  }

  private void runShow(final int step) {
    final GifDrawable currentGifDrawable = mGifDrawables.get(step % NUM_GIFS_FOR_SHOW);
    showGifDrawable(step - 1);
    currentGifDrawable.pause();
      ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(mGifImageView, "scaleX", 0f);
      ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(mGifImageView, "scaleY", 0f);
      AnimatorSet animatorSet = new AnimatorSet();
      animatorSet.playTogether(scaleDownX, scaleDownY);
      animatorSet.setDuration(ANIMATION_DURATION_MS);
      animatorSet.addListener(new AnimationUtils.OnAnimationEndOnlyListener() {
        @Override
        public void onAnimationEnd(Animator animation) {
          mGifImageView.setImageDrawable(null);
          mGifImageView.setImageDrawable(currentGifDrawable);

          ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(mGifImageView, "scaleX", 1f);
          ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(mGifImageView, "scaleY", 1f);
          AnimatorSet animatorSet = new AnimatorSet();
          animatorSet.playTogether(scaleDownX, scaleDownY);
          animatorSet.setDuration(ANIMATION_DURATION_MS);
          animatorSet.addListener(new AnimationUtils.OnAnimationEndOnlyListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
              currentGifDrawable.start();
              currentGifDrawable.addAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationCompleted(int loopNumber) {
//                  currentGifDrawable.pause();
                  runShow(step + 1, false);
                }
              });
            }});
          animatorSet.start();
        }
      });
      animatorSet.start();
    }
  }
}
