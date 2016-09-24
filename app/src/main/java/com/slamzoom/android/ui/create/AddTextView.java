package com.slamzoom.android.ui.create;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.slamzoom.android.R;
import com.slamzoom.android.common.ui.widgets.BackInterceptingEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clocksmith on 3/27/16.
 */
public class AddTextView extends LinearLayoutCompat {

  @BindView(R.id.editText) BackInterceptingEditText mEditText;

  public AddTextView(Context context) {
    this(context, null);
  }

  public AddTextView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public AddTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    LayoutInflater.from(context).inflate(R.layout.view_add_text, this);
    ButterKnife.bind(this);
  }

  public EditText getEditText() {
    return mEditText;
  }
}
