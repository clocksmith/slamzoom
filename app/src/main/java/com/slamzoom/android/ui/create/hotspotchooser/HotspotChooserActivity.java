package com.slamzoom.android.ui.create.hotspotchooser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.R;
import com.slamzoom.android.common.data.MathUtils;
import com.slamzoom.android.common.fonts.FontProvider;
import com.slamzoom.android.common.intents.Params;
import com.slamzoom.android.common.logging.SzAnalytics;
import com.slamzoom.android.common.logging.SzLog;
import com.slamzoom.android.common.bitmaps.BitmapUtils;
import com.slamzoom.android.BuildFlags;
import com.slamzoom.android.common.data.UriUtils;
import com.slamzoom.android.common.bitmaps.BitmapSet;
import com.slamzoom.android.common.settings.Preferences;
import com.slamzoom.android.common.ui.DismissableDialogPresenter;
import com.slamzoom.android.ui.create.CreateActivity;

import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clocksmith on 3/5/16.
 */
public class HotspotChooserActivity extends AppCompatActivity {
  private static final String TAG = HotspotChooserActivity.class.getSimpleName();

  private static ImmutableList<Integer> TIP_STRING_RES_IDS = ImmutableList.of(
      R.string.hotspot_chooser_hint_zoom_in_on_face,
      R.string.hotspot_chooser_hint_zoom_in_close
  );

  @BindView(R.id.hint) TextView mHint;
  @BindView(R.id.imageCropViewContainer) FrameLayout mImageCropViewContainer;
  @BindView(R.id.imageCropView) CropRectProvidingImageCropView mImageCropView;
  @BindView(R.id.doneButton) Button mDoneButton;

  private Bitmap mBitmap;
  private boolean mFromCreateAcitivty;
  private boolean mFromChangeHotspotRequest;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    SzLog.f(TAG, "onCreate");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_hotspot_chooser);
    ButterKnife.bind(this);

    mHint.setTypeface(FontProvider.getInstance().getDefaultFont());
    mDoneButton.setTypeface(FontProvider.getInstance().getDefaultFont());

    handleIntent(getIntent());
  }

  @Override
  protected void onNewIntent(Intent intent) {
    SzLog.f(TAG, "onNewIntent");
    super.onNewIntent(intent);
    handleIntent(intent);
  }

  @Override
  protected void onResume() {
    super.onResume();
    mHint.setText(MathUtils.getRandomElement(TIP_STRING_RES_IDS));
  }

  private void handleIntent(Intent intent) {
    mFromCreateAcitivty = getCallingActivity() != null &&
        getCallingActivity().getClassName().equals(CreateActivity.class.getCanonicalName());
    final Uri incomingUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
    mFromChangeHotspotRequest = intent.getBooleanExtra(Params.FROM_CHANGE_HOTSPOT_REQUEST, false);

    SzAnalytics.newSelectImageEvent()
        .withPackageName(incomingUri.toString().replace("content://", ""))
        .log(this);

    try {
      if (UriUtils.isResUri(incomingUri)) {
        // TODO(clocksmith): Make generic method in BitmapUtils and replace BitmapSet fork.
        mBitmap = new BitmapSet(this, incomingUri, 80).get(80);
      } else {
        mBitmap = BitmapUtils.readScaledBitmap(incomingUri, this.getContentResolver());
      }

      if (BuildFlags.USE_DEBUG_HOTSPOT) {
        RectF debugCropRect = BuildFlags.DEBUG_HOTSPOT;
        Log.d(TAG, "using debug cropRect: " + debugCropRect.toString());
        finishWithCropRectF(debugCropRect, incomingUri);
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
        finishWithCropRect(mImageCropView.getCropRect(), incomingUri);
      }
    });

    if (BuildFlags.ACT_AS_FIRST_OPEN || Preferences.isFirstHotspotOpen(this)) {
      showFirstOpenDialog();
      Preferences.setFirstHotspotOpen(this, false);
    }
  }

  private void showFirstOpenDialog() {
    final ImageView view = new ImageView(this);
    view.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.sz_pinch_to_zoom));
    view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    DismissableDialogPresenter.show(this, getString(R.string.hotspot_chooser_first_open_title), view);
  }

  private void finishWithCropRectF(RectF hotspot, Uri imageUri) {
    if (mFromCreateAcitivty) {
      Intent returnIntent = new Intent();
      returnIntent.putExtra(Params.IMAGE_URI, imageUri);
      returnIntent.putExtra(Params.HOTSPOT, hotspot);
      returnIntent.putExtra(Params.FROM_CHANGE_HOTSPOT_REQUEST, mFromChangeHotspotRequest);
      setResult(RESULT_OK, returnIntent);
      finish();
    } else {
      Intent intent = new Intent(HotspotChooserActivity.this, CreateActivity.class);
      intent.putExtra(Params.IMAGE_URI, Uri.fromFile(BitmapUtils.saveBitmapToDiskPrivatelyAsJpeg(mBitmap, "temp")));
      intent.putExtra(Params.HOTSPOT, hotspot);
      intent.putExtra(Params.FROM_CHANGE_HOTSPOT_REQUEST, mFromChangeHotspotRequest);
      startActivity(intent);
    }
  }

  private void finishWithCropRect(Rect hotspot, Uri imageUri) {
    RectF normalizedHotspot = new RectF(
        (float) hotspot.left / mBitmap.getWidth(),
        (float) hotspot.top / mBitmap.getHeight(),
        (float) hotspot.right / mBitmap.getWidth(),
        (float) hotspot.bottom / mBitmap.getHeight());

   finishWithCropRectF(normalizedHotspot, imageUri);
  }

  @Override
  public void onBackPressed() {
    finish();
  }
}
