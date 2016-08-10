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


    Uri uri = getIntent().getParcelableExtra(Constants.IMAGE_URI);
    try {
      Bitmap bitmap = BitmapUtils.readScaledBitmap(uri, this.getContentResolver());
      if (DebugUtils.USE_STATIC_RECTANGLE) {
        Rect debugCropRect = DebugUtils.getDebugRect(bitmap);
        Log.d(TAG, "using debug cropRect: " + debugCropRect.toString());
        finishWithCropRect(debugCropRect);
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
        Rect cropRect = mImageCropView.getCropRect();
        Log.d(TAG, "selected cropRect: " + cropRect.toString());
        finishWithCropRect(cropRect);
      }
    });
  }

  private void finishWithCropRect(Rect cropRect) {
    Intent returnIntent = new Intent();
    returnIntent.putExtra(Constants.CROP_RECT, cropRect);
    setResult(RESULT_OK, returnIntent);
    finish();
  }

  @Override
  public void onBackPressed() {
    finish();
  }

}
