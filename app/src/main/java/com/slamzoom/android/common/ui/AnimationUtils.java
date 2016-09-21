package com.slamzoom.android.common.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

  public static AnimatorSet getUniformScaleSet(View view, float scale, int durationMillis) {
    ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", scale);
    ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", scale);
    AnimatorSet scaleDownAnimatorSet = new AnimatorSet();
    scaleDownAnimatorSet.playTogether(scaleX, scaleY);
    scaleDownAnimatorSet.setDuration(durationMillis);
    return scaleDownAnimatorSet;
  }

  public static AnimatorSet getPulseForeverSet(final View view, final float scale, final int durationMillis) {
    AnimatorSet up = AnimationUtils.getUniformScaleSet(view, scale, durationMillis);
    up.addListener(new AnimationUtils.OnAnimationEndOnlyListener() {
      @Override
      public void onAnimationEnd(Animator animation) {
        AnimatorSet down = AnimationUtils.getUniformScaleSet(view, 1, durationMillis);
        down.addListener(new AnimationUtils.OnAnimationEndOnlyListener() {
          @Override
          public void onAnimationEnd(Animator animation) {
            getPulseForeverSet(view, scale, durationMillis).start();
          }
        });
        down.start();
      }
    });
    return up;
  }

  public static AnimatorSet getChangeTextColorSet(final TextView view, @ColorInt int endColor, int durationMillis) {
    final float[] startHsvArr = new float[3];
    final float[] endHsvArr = new float[3];

    Color.colorToHSV(view.getCurrentTextColor(), startHsvArr);
    Color.colorToHSV(endColor, endHsvArr);

    ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
    final float[] hsv  = new float[3];
    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        // TODO(clocksmith): use interpolator.
        hsv[0] = startHsvArr[0] + (endHsvArr[0] - startHsvArr[0]) * animation.getAnimatedFraction();
        hsv[1] = startHsvArr[1] + (endHsvArr[1] - startHsvArr[1]) * animation.getAnimatedFraction();
        hsv[2] = startHsvArr[2] + (endHsvArr[2] - startHsvArr[2]) * animation.getAnimatedFraction();

        view.setTextColor(Color.HSVToColor(hsv));
      }
    });

    AnimatorSet set = new AnimatorSet();
    set.play(anim);
    set.setDuration(durationMillis);
    return set;
  }

  public static AnimatorSet getChangeButtonBackgroundColorSet(
      final Button button, @ColorInt int endColor, int durationMillis) {
    final float[] startHsvArr = new float[3];
    final float[] endHsvArr = new float[3];

    Color.colorToHSV(((ColorDrawable) button.getBackground()).getColor(), startHsvArr);
    Color.colorToHSV(endColor, endHsvArr);

    ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
    final float[] hsv  = new float[3];
    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        // TODO(clocksmith): use interpolator.
        hsv[0] = startHsvArr[0] + (endHsvArr[0] - startHsvArr[0]) * animation.getAnimatedFraction();
        hsv[1] = startHsvArr[1] + (endHsvArr[1] - startHsvArr[1]) * animation.getAnimatedFraction();
        hsv[2] = startHsvArr[2] + (endHsvArr[2] - startHsvArr[2]) * animation.getAnimatedFraction();

        button.setBackgroundColor(Color.HSVToColor(hsv));
      }
    });

    AnimatorSet set = new AnimatorSet();
    set.play(anim);
    set.setDuration(durationMillis);
    return set;
  }
}
