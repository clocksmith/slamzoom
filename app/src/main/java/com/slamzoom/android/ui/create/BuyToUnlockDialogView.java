package com.slamzoom.android.ui.create;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.slamzoom.android.R;

import butterknife.ButterKnife;

/**
 * Created by clocksmith on 7/20/16.
 */
public class BuyToUnlockDialogView extends LinearLayout {
  public BuyToUnlockDialogView(Context context) {
    this(context, null);
  }

  public BuyToUnlockDialogView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BuyToUnlockDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    LayoutInflater.from(context).inflate(R.layout.view_buy_dialog, this);
    ButterKnife.bind(this);
  }
}
