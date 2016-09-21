package com.slamzoom.android.common.bitmaps;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.logging.SzLog;
import com.slamzoom.android.common.BuildFlags;
import com.slamzoom.android.common.data.UriUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by clocksmith on 9/15/16.
 *
 * Set of the same bitmaps of various sizes to try to optimize the gif creation process.
 */
public class BitmapSet {
  private static final String TAG = BitmapSet.class.getSimpleName();

  private int[] mSizes = new int[Constants.NUM_BITMAPS_IN_SET];
  private Bitmap[] mBitmaps = new Bitmap[Constants.NUM_BITMAPS_IN_SET];
  private float mAspectRatio;

  public BitmapSet(Context context, Uri imageUri, int startSize) {
    int size = startSize;
    for (int i = 0; i < Constants.NUM_BITMAPS_IN_SET; i++) {
      try {
        Bitmap bitmap;
        if (UriUtils.isResUri(imageUri)) {
          try {
            InputStream boundsStream = context.getContentResolver().openInputStream(imageUri);
            InputStream finalStream = context.getContentResolver().openInputStream(imageUri);
            bitmap = BitmapUtils.readScaledBitmap(boundsStream, finalStream, size);
          } catch (Exception e) {
            SzLog.e(TAG, "Cannot read in stream", e);
            return;
          }
        } else {
          bitmap = BitmapUtils.readScaledBitmap(imageUri, context.getContentResolver(), size);
        }

        if (mAspectRatio == 0) {
          mAspectRatio = (float) bitmap.getWidth() / bitmap.getHeight();
        }

        mSizes[i] = Math.max(bitmap.getWidth(), bitmap.getHeight());
        mBitmaps[i] = bitmap;

        if (BuildFlags.SAVE_SRC_AS_PNG) {
          BitmapUtils.saveBitmapToDiskAsPng(bitmap, "src_" + size);
        }
      } catch (FileNotFoundException e) {
        SzLog.e(TAG, "Could not read in bitmap", e);
      }
      size *= 2;
    }
  }


  public Bitmap get(int targetSize) {
    // TODO(clocksmith): fine tune this multiplier (should be 1?)
    targetSize *= 2;
    if (mBitmaps == null || mBitmaps.length == 0 || mSizes == null || mSizes.length == 0) {
      return null;
    } else {
      for (int i = mSizes.length - 1; i > 0; i--) {
        if (targetSize > mSizes[i]) {
          return mBitmaps[i];
        }
      }
      return mBitmaps[0];
    }
  }

  public float getAspectRatio() {
    return mAspectRatio;
  }
}
