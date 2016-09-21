package com.slamzoom.android.ui.create.hotspotchooser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.slamzoom.android.R;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.FontLoader;
import com.slamzoom.android.common.LifecycleLoggingActivity;
import com.slamzoom.android.common.SzAnalytics;
import com.slamzoom.android.common.SzLog;
import com.slamzoom.android.common.preferences.Preferences;
import com.slamzoom.android.common.utils.BitmapUtils;
import com.slamzoom.android.common.utils.DebugUtils;
import com.slamzoom.android.common.utils.UriUtils;
import com.slamzoom.android.mediacreation.BitmapSet;
import com.slamzoom.android.ui.create.CreateActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clocksmith on 3/5/16.
 */
public class HotspotChooserActivity extends LifecycleLoggingActivity {
  private static final String TAG = HotspotChooserActivity.class.getSimpleName();

  @BindView(R.id.title) TextView mTitle;
  @BindView(R.id.hint) TextView mHint;
  @BindView(R.id.imageCropViewContainer) FrameLayout mImageCropViewContainer;
  @BindView(R.id.imageCropView) CropRectProvidingImageCropView mImageCropView;
  @BindView(R.id.doneButton) Button mDoneButton;

  private Bitmap mBitmap;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setSubTag(TAG);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_hotspot_chooser);
    ButterKnife.bind(this);

    mTitle.setTypeface(FontLoader.getInstance().getTitleFont());
    mHint.setTypeface(FontLoader.getInstance().getDefaultFont());
    mDoneButton.setTypeface(FontLoader.getInstance().getDefaultFont());

    final Uri uri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);

    SzAnalytics.newSelectImageEvent()
        .withPackageName(uri.toString().replace("content://", ""))
        .log(this);

    try {
      if (UriUtils.isResUri(uri)) {
        // TODO(clocksmith): Make generic method in BitmapUtils and replace BitmapSet fork.
        mBitmap = new BitmapSet(this, uri, 80).get(80);
      } else {
        mBitmap = BitmapUtils.readScaledBitmap(uri, this.getContentResolver());
      }

      if (DebugUtils.USE_PREDEFINED_HOTSPOT) {
        Rect debugCropRect = DebugUtils.getDebugRect(mBitmap);
        Log.d(TAG, "using debug cropRect: " + debugCropRect.toString());
        finishWithCropRect(debugCropRect, uri);
      } else {
        mImageCropView.setAspectRatio(mBitmap.getWidth(), mBitmap.getHeight());
        mImageCropView.setImageBitmap(mBitmap, new Matrix(), 1f, 100f);
      }
    } catch (FileNotFoundException e) {
      Log.e(TAG, "Cannot read selected image", e);
      finish();
    }

    mDoneButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finishWithCropRect(mImageCropView.getCropRect(), uri);
      }
    });

    if (Preferences.isFirstHotspotOpen(this)) {
      showHelpOverlay();
      Preferences.setFirstHotspotOpen(this, false);
    }
  }

  private void showHelpOverlay() {
    final ImageView overlay = new ImageView(this);
    overlay.setBackgroundColor(Color.argb(128, 0, 0, 0));
    overlay.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pinch_zoom_light_transparent));
    overlay.setScaleType(ImageView.ScaleType.CENTER);
    overlay.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        mImageCropViewContainer.removeView(overlay);
        return false;
      }
    });
    mImageCropViewContainer.addView(overlay,
        new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
  }

  private void finishWithCropRect(Rect hotspot, Uri imageUri) {
    RectF normalizedHotspot = new RectF(
        (float) hotspot.left / mBitmap.getWidth(),
        (float) hotspot.top / mBitmap.getHeight(),
        (float) hotspot.right / mBitmap.getWidth(),
        (float) hotspot.bottom / mBitmap.getHeight());

    if (getCallingActivity() != null &&
        getCallingActivity().getClassName().equals(CreateActivity.class.getCanonicalName())) {
      Intent returnIntent = new Intent();
      returnIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
      returnIntent.putExtra(Constants.NORMALIZED_HOTSPOT, normalizedHotspot);
      setResult(RESULT_OK, returnIntent);
      finish();
    } else {
      Intent intent = new Intent(HotspotChooserActivity.this, CreateActivity.class);
      intent.putExtra(Intent.EXTRA_STREAM, imageUri);
      intent.putExtra(Constants.NORMALIZED_HOTSPOT, normalizedHotspot);
      startActivity(intent);
    }
  }

  @Override
  public void onBackPressed() {
//    super.onBackPressed();
    finish();
  }
}
