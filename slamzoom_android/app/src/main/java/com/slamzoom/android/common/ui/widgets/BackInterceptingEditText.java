package com.slamzoom.android.common.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

import com.slamzoom.android.common.events.BusProvider;

/**
 * Created by clocksmith on 3/30/16.
 */
public class BackInterceptingEditText extends EditText {
  public class OnBackPressedEvent {}

  public BackInterceptingEditText(Context context) {
    this(context, null);
  }

  public BackInterceptingEditText(Context context, AttributeSet attrs) {
    this(context, attrs, android.R.attr.editTextStyle);
  }

  public BackInterceptingEditText(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public boolean dispatchKeyEventPreIme(KeyEvent event) {
    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
      BusProvider.getInstance().post(new OnBackPressedEvent());
      return true;
    } else {
      return super.dispatchKeyEventPreIme(event);
    }
  }
}