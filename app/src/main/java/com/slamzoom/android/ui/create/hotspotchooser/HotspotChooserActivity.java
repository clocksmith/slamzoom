package com.slamzoom.android.ui.create.hotspotchooser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.slamzoom.android.R;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.utils.BitmapUtils;
import com.slamzoom.android.common.utils.DebugUtils;
import com.slamzoom.android.ui.create.CreateActivity;

import java.io.FileNotFoundException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by clocksmith on 3/5/16.
 */
public class HotspotChooserActivity extends AppCompatActivity {
  private static final String TAG = HotspotChooserActivity.class.getSimpleName();

  @Bind(R.id.imageCropView)
  CropRectProvidingImageCropView mImageCropView;
  @Bind(R.id.doneButton) Button mDoneButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cropper);
    ButterKnife.bind(this);

    final Uri uri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);

    try {
      Bitmap bitmap = BitmapUtils.readScaledBitmap(uri, this.getContentResolver());
      if (DebugUtils.USE_STATIC_RECTANGLE) {
        Rect debugCropRect = DebugUtils.getDebugRect(bitmap);
        Log.d(TAG, "using debug cropRect: " + debugCropRect.toString());
        finishWithCropRect(debugCropRect, uri);
      } else {
        mImageCropView.setAspectRatio(bitmap.getWidth(), bitmap.getHeight());
        mImageCropView.setImageBitmap(bitmap, new Matrix(), 1f, 100f);
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
  }

  private void finishWithCropRect(Rect hotspot, Uri imageUri) {
    if (getCallingActivity() != null &&
        getCallingActivity().getClassName().equals(CreateActivity.class.getCanonicalName())) {
      Intent returnIntent = new Intent();
      returnIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
      returnIntent.putExtra(Constants.HOTSPOT, hotspot);
      setResult(RESULT_OK, returnIntent);
      finish();
    } else {
      Intent intent = new Intent(HotspotChooserActivity.this, CreateActivity.class);
      intent.putExtra(Intent.EXTRA_STREAM, imageUri);
      intent.putExtra(Constants.HOTSPOT, hotspot);
      startActivity(intent);
    }
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
//    if (getCallingActivity() != null &&
//        getCallingActivity().getClassName().equals(CreateActivity.class.getCanonicalName())) {
//      Intent returnIntent = new Intent();
//      setResult(RESULT_OK, returnIntent);
//    }
    finish();
  }
}
