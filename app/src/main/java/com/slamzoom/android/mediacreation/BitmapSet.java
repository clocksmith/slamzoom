package com.slamzoom.android.mediacreation;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;

import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.SzLog;
import com.slamzoom.android.common.utils.BitmapUtils;
import com.slamzoom.android.common.utils.DebugUtils;

import java.io.FileNotFoundException;

/**
 * Created by clocksmith on 9/15/16.
 */
public class BitmapSet {
  private static final String TAG = BitmapSet.class.getSimpleName();

  private int[] mSizes = new int[Constants.NUM_BITMAPS_IN_SET];
  private Bitmap[] mBitmaps = new Bitmap[Constants.NUM_BITMAPS_IN_SET];
  private float mAspectRatio;

  public BitmapSet(Uri imageUri, ContentResolver contentResolver, int startSize) {
    int size = startSize;
    for (int i = 0; i < Constants.NUM_BITMAPS_IN_SET; i++) {
      try {
        Bitmap bitmap = BitmapUtils.readScaledBitmap(imageUri, contentResolver, size);
        if (mAspectRatio == 0) {
          mAspectRatio = (float) bitmap.getWidth() / bitmap.getHeight();
        }
        mSizes[i] = Math.max(bitmap.getWidth(), bitmap.getHeight());
        mBitmaps[i] = bitmap;

        if (DebugUtils.SAVE_SRC_AS_PNG) {
          BitmapUtils.saveBitmapToDiskAsPng(bitmap, "src_" + size);
        }
      } catch (FileNotFoundException e) {
        SzLog.e(TAG, "Could not read in bitmap", e);
      }
      size *= 2;
    }
  }

  public Bitmap getForNormalizedScaled(float normScale) {
    return get((int) (mSizes[mSizes.length - 1] * normScale));
  }

  public Bitmap get(int targetSize) {
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
