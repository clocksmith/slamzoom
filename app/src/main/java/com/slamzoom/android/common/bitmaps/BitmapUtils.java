package com.slamzoom.android.common.bitmaps;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;

import com.slamzoom.android.mediacreation.MediaConstants;
import com.slamzoom.android.BuildFlags;
import com.slamzoom.android.common.files.FileType;
import com.slamzoom.android.common.logging.SzLog;
import com.slamzoom.android.common.files.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by clocksmith on 3/7/16.
 */
public class BitmapUtils {
  // TODO(clocksmith): change this value, make it easier to config.
  public static final int NUM_BITMAPS_IN_SET = 5; // doubles every change.
  private static final String TAG = BitmapUtils.class.getSimpleName();

  public static Bitmap readScaledBitmap(Uri uri , ContentResolver contentResolver) throws FileNotFoundException  {
    return readScaledBitmap(uri, contentResolver, MediaConstants.MAX_DIMEN_FOR_MIN_SELECTED_DIMEN_PX);
  }

  public static void recycleIfSupposedTo(Bitmap bitmap) {
    if (!BuildFlags.SKIP_RECYCLE_BITMAP) {
      bitmap.recycle();
    }
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
      options.inSampleSize =
          BitmapUtils.calculateInSampleSize(options, scaledSelectedBitmapWidth, scaledSelectedBitmapHeight);;
      options.inJustDecodeBounds = false;
      options.inPreferredConfig = Bitmap.Config.ARGB_8888;
      return BitmapFactory.decodeFileDescriptor(assetFileDescriptor.getFileDescriptor(), null, options);
    } else {
      throw new FileNotFoundException("Could not load bitmap for path: " + uri.toString());
    }
  }

  public static Bitmap readScaledBitmap(InputStream boundsStream, InputStream finalStream, int maxDimen)
      throws IOException {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeStream(boundsStream, null, options);
    boundsStream.close();
    int selectedBitmapWidth = options.outWidth;
    int selectedBitmapHeight = options.outHeight;
    float aspectRatio = (float) selectedBitmapWidth / selectedBitmapHeight;
    int scaledSelectedBitmapWidth =
        aspectRatio > 1 ? maxDimen : Math.round(maxDimen * aspectRatio);
    int scaledSelectedBitmapHeight =
        aspectRatio > 1 ? Math.round(maxDimen / aspectRatio) : maxDimen;
    options.inSampleSize =
        BitmapUtils.calculateInSampleSize(options, scaledSelectedBitmapWidth, scaledSelectedBitmapHeight);;
    options.inJustDecodeBounds = false;
    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    Bitmap bitmap = BitmapFactory.decodeStream(finalStream, null, options);
    finalStream.close();
    return bitmap;
  }

  // TODO(clocksmith): trying to make this better than Bitmap.createScaledBitmap,
  // but right now it is the same quality but slower.
  public static Bitmap createScaledBitmap(Bitmap sourceBitmap, int newWidth, int newHeight) {
    Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
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

  public static File saveBitmapToDiskAsPng(Bitmap finalBitmap, String id) {
    File file = FileUtils.createFileWithId(FileType.PNG, id);
    FileOutputStream out = null;

    try {
      out = new FileOutputStream(file);
      finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
    } catch (Exception e) {
      SzLog.e(TAG, "Cannot compress bitmap to png", e);
    } finally {
      try {
        if (out != null) {
          out.close();
        }
      } catch (IOException e) {
        SzLog.e(TAG, "Cannot save bitmap", e);
      }
    }

    return file;
  }

  public static File saveBitmapToDiskAsJpeg(Bitmap finalBitmap, String id) {
    File file = FileUtils.createFileWithId(FileType.JPEG, id);
    FileOutputStream out = null;

    try {
      out = new FileOutputStream(file);
      finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
    } catch (Exception e) {
      SzLog.e(TAG, "Cannot compress bitmap to jpeg", e);
    } finally {
      try {
        if (out != null) {
          out.close();
        }
      } catch (IOException e) {
        SzLog.e(TAG, "Cannot save bitmap", e);
      }
    }

    return file;
  }

  // TODO(clocksmith): merge with other methods in this file
  public static File saveBitmapToDiskPrivatelyAsJpeg(Bitmap finalBitmap, String id) {
    File file = FileUtils.createPrivateFileWithFilename(id + "." + FileType.JPEG.ext);
    FileOutputStream out = null;

    try {
      out = new FileOutputStream(file);
      finalBitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
    } catch (Exception e) {
      SzLog.e(TAG, "Cannot compress bitmap to jpeg", e);
    } finally {
      try {
        if (out != null) {
          out.close();
        }
      } catch (IOException e) {
        SzLog.e(TAG, "Cannot save bitmap", e);
      }
    }

    return file;
  }
}
