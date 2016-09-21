package com.slamzoom.android.ui.start;

import android.content.Context;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.R;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.SzLog;
import com.slamzoom.android.common.utils.UriUtils;
import com.slamzoom.android.ui.create.CreateActivity;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by clocksmith on 9/19/16.
 */
public class TutorialView extends FrameLayout {
  private static final String TAG = TutorialView.class.getSimpleName();

  private static final RectF MONA_LISA_PHONE_HOTSPOT = new RectF(0.16f, 0.75f, 0.28f, 0.87f);
  private static final CreateTemplate CREATE_TEMPLATE =
      new CreateTemplate(UriUtils.getUriFromRes(R.drawable.mona_lisa_sz_960x1280), MONA_LISA_PHONE_HOTSPOT);

  @BindView(R.id.viewPager) ViewPager mViewPager;
  @BindView(R.id.titlePageIndicator) CirclePageIndicator mPageIndicator;

  private FragmentActivity mFragmentActivity;

  public TutorialView(Context context) {
    this(context, null);
  }

  public TutorialView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public TutorialView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    LayoutInflater.from(context).inflate(R.layout.view_tutorial, this);
    ButterKnife.bind(this);

    mFragmentActivity = ((FragmentActivity) context);

    mViewPager.setAdapter(new TutorialPagerAdapter(mFragmentActivity.getSupportFragmentManager()));
    mPageIndicator.setViewPager(mViewPager);
  }

  private void startCreateActivityWithTemplate() {
    Intent intent = new Intent(mFragmentActivity, CreateActivity.class);
    intent.putExtra(Constants.CREATE_TEMPLATE, CREATE_TEMPLATE);
    mFragmentActivity.startActivity(intent);
  }


  private static class TutorialPagerAdapter extends FragmentPagerAdapter {
    ImmutableList<WelcomeFragment> FRAGMENTS = ImmutableList.of(
        WelcomeFragment.newInstance(),
        WelcomeFragment.newInstance(),
        WelcomeFragment.newInstance()
    );

    public TutorialPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      return FRAGMENTS.get(position);
    }

    @Override
    public int getCount() {
      return FRAGMENTS.size();
    }
  }

  public static class WelcomeFragment extends Fragment {
    public static WelcomeFragment newInstance() {
      return new WelcomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      GifImageView gifImageView = new GifImageView(getContext());
      try {
        gifImageView.setImageDrawable(new GifDrawable(getResources(), R.drawable.mona_lisa_sz_960x1280));
      } catch (IOException e) {
        SzLog.e(TAG, "Could not load mona slamin", e);
      }
      return gifImageView;
    }
  }
}
