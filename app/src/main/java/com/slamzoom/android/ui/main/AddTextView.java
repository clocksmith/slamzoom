package com.slamzoom.android.ui.main;

import android.content.Context;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;

import com.slamzoom.android.R;
import com.slamzoom.android.global.BackInterceptingEditText;
import com.slamzoom.android.global.singletons.BusProvider;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by clocksmith on 3/27/16.
 */
public class AddTextView extends LinearLayoutCompat {

  @Bind(R.id.editText) BackInterceptingEditText mEditText;

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
