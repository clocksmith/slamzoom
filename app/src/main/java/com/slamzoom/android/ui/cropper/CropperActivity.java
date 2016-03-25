package com.slamzoom.android.ui.cropper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.slamzoom.android.global.utils.BitmapUtils;
import com.slamzoom.android.global.Constants;
import com.slamzoom.android.R;

import java.io.FileNotFoundException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by clocksmith on 3/5/16.
 */
public class CropperActivity extends AppCompatActivity {
  private static final String TAG = CropperActivity.class.getSimpleName();

  @Bind(R.id.imageCropView) CropRectProvidingImaeCropView mImageCropView;
  @Bind(R.id.doneButton) Button mDoneButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cropper);
    ButterKnife.bind(this);


    Uri uri = getIntent().getParcelableExtra(Constants.IMAGE_URI);
    try {
      Bitmap bitmap = BitmapUtils.readScaledBitmap(uri, this.getContentResolver(), Constants.MAX_SELECTED_DIMEN_PX);
      mImageCropView.setAspectRatio(bitmap.getWidth(), bitmap.getHeight());
      mImageCropView.setImageBitmap(bitmap, new Matrix(), 1f, 1000f);
//      mImageCropView.setImageBitmap(bitmap);
    } catch (FileNotFoundException e) {
      Log.e(TAG, "Cannot read bitmap", e);
      finish();
    }

    mDoneButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent returnIntent = new Intent();
        Log.d(TAG, mImageCropView.getCropRect().toString());
        returnIntent.putExtra(Constants.CROP_RECT, mImageCropView.getCropRect());
        setResult(RESULT_OK, returnIntent);
        finish();
      }
    });
  }
}
