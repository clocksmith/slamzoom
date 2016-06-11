package com.slamzoom.android.common.utils;

import android.animation.Animator;

/**
 * Created by clocksmith on 6/10/16.
 */
public class AnimationUtils {
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
}
