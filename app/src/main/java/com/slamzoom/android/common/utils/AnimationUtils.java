package com.slamzoom.android.common.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by clocksmith on 6/10/16.
 */
public class AnimationUtils {
  public static final int DEFAULT_ANIMATION_DURATION_MS = 175;

  public static abstract class OnAnimationEndOnlyListener implements Animator.AnimatorListener {
    @Override
    public void onAnimationStart(Animator animation) {
      // Do nothing.
    }

    @Override
    public void onAnimationCancel(Animator animation) {
      // Do nothing.
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
      // Do nothing.
    }
  }

  public static AnimatorSet getScaleDownSet(View view) {
    return getScaleDownSet(view, DEFAULT_ANIMATION_DURATION_MS);
  }

  public static AnimatorSet getScaleUpSet(View view) {
    return getScaleUpSet(view, DEFAULT_ANIMATION_DURATION_MS);
  }


  public static AnimatorSet getScaleDownSet(View view, int durationMillis) {
    return getUniformScaleSet(view, 0, durationMillis);
  }

  public static AnimatorSet getScaleUpSet(View view, int durationMillis) {
    return getUniformScaleSet(view, 1, durationMillis);
  }

  private static AnimatorSet getUniformScaleSet(View view, float scale, int durationMillis) {
    ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", scale);
    ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", scale);
    AnimatorSet scaleDownAnimatorSet = new AnimatorSet();
    scaleDownAnimatorSet.playTogether(scaleX, scaleY);
    scaleDownAnimatorSet.setDuration(durationMillis);
    return scaleDownAnimatorSet;
  }
}
