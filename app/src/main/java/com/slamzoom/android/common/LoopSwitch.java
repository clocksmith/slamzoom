package com.slamzoom.android.common;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;

import com.slamzoom.android.R;

/**
 * Created by clocksmith on 8/24/16.
 */
public class LoopSwitch extends SwitchCompat {
  public LoopSwitch(Context context) {
    super(context, null);
  }

  public LoopSwitch(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public LoopSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    this.setShowText(true);
    if (Build.VERSION.SDK_INT < 23) {
      this.setTextAppearance(this.getContext(), R.style.SwitchTextAppearance);
    } else {
      this.setTextAppearance(R.style.SwitchTextAppearance);
    }
    this.setTextOn("loop");
    this.setTextOff("pause");
    this.setThumbResource(R.drawable.loop_thumb);

  }
}
