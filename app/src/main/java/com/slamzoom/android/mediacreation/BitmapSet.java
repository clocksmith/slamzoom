package com.slamzoom.android.mediacreation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.slamzoom.android.R;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.SzLog;
import com.slamzoom.android.common.utils.BitmapUtils;
import com.slamzoom.android.common.utils.DebugUtils;
import com.slamzoom.android.common.utils.UriUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by clocksmith on 9/15/16.
 */
public class BitmapSet {
  private static final String TAG = BitmapSet.class.getSimpleName();

  private int[] mSizes = new int[Constants.NUM_BITMAPS_IN_SET];
  private Bitmap[] mBitmaps = new Bitmap[Constants.NUM_BITMAPS_IN_SET];
  private float mAspectRatio;

  public BitmapSet(Context context, Uri imageUri, int startSize) {
    // Special hack case for mona lisa.
    if (imageUri.toString().equals("mona")) {
      try {
        InputStream stream =
            context.getContentResolver().openInputStream(UriUtils.getUriFromRes(context, R.drawable.mona_lisa_sz));
        mBitmaps = new Bitmap[1];
        mBitmaps[0] = BitmapFactory.decodeStream(stream);
        mAspectRatio = (float) mBitmaps[0].getWidth() / mBitmaps[0].getHeight();
        mSizes = new int[1];
        mSizes[0] = mBitmaps[0].getHeight();
      } catch (FileNotFoundException e) {
        SzLog.e(TAG, "Could not read in mona lisa", e);
      }
      return;
    }

    int size = startSize;
    for (int i = 0; i < Constants.NUM_BITMAPS_IN_SET; i++) {
      try {
        Bitmap bitmap = BitmapUtils.readScaledBitmap(imageUri, context.getContentResolver(), size);
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
