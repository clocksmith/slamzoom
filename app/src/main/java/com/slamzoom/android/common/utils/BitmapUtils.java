package com.slamzoom.android.common.utils;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;

import com.slamzoom.android.common.Constants;

import java.io.FileNotFoundException;

/**
 * Created by clocksmith on 3/7/16.
 */
public class BitmapUtils {
  public static Bitmap readScaledBitmap(Uri uri , ContentResolver contentResolver) throws FileNotFoundException  {
    return readScaledBitmap(uri, contentResolver, Constants.MAX_DIMEN_FOR_MIN_SELECTED_DIMEN_PX);
  }

  public static Bitmap readScaledBitmap(
      Uri uri, ContentResolver contentResolver, int maxDimen) throws FileNotFoundException {
    AssetFileDescriptor assetFileDescriptor = contentResolver.openAssetFileDescriptor(uri, "r");
    if (assetFileDescriptor != null) {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeFileDescriptor(assetFileDescriptor.getFileDescriptor(), null, options);
      int selectedBitmapWidth = options.outWidth;
      int selectedBitmapHeight = options.outHeight;
      float aspectRatio = (float) selectedBitmapWidth / selectedBitmapHeight;
      int scaledSelectedBitmapWidth =
          aspectRatio > 1 ? maxDimen : Math.round(maxDimen * aspectRatio);
      int scaledSelectedBitmapHeight =
          aspectRatio > 1 ? Math.round(maxDimen / aspectRatio) : maxDimen;
      int inSampleSize =
          BitmapUtils.calculateInSampleSize(options, scaledSelectedBitmapWidth, scaledSelectedBitmapHeight);
      options.inSampleSize = inSampleSize;
      options.inJustDecodeBounds = false;
      return BitmapFactory.decodeFileDescriptor(assetFileDescriptor.getFileDescriptor(), null, options);
    } else {
      throw new FileNotFoundException("Could not load bitmap for path: " + uri.toString());
    }
  }

  // TODO(clocksmith): trying to make this better than Bitmap.createScaledBitmap,
  // but right now it is the same quality but slower.
  public static Bitmap createScaledBitmap(Bitmap sourceBitmap, int newWidth, int newHeight) {
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(scaledBitmap);
    canvas.drawBitmap(
        sourceBitmap,
        new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()),
        new Rect(0, 0, newWidth, newHeight),
        paint);
    return scaledBitmap;
  }

  private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

      final int halfHeight = height / 2;
      final int halfWidth = width / 2;

      // Calculate the largest inSampleSize value that is a power of 2 and keeps both
      // height and width larger than the requested height and width.
      while ((halfHeight / inSampleSize) > reqHeight
          && (halfWidth / inSampleSize) > reqWidth) {
        inSampleSize *= 2;
      }
    }

    return inSampleSize;
  }
}
