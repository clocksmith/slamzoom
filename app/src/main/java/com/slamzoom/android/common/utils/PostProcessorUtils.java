package com.slamzoom.android.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Log;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.mediacreation.WatermarkProvider;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageRenderer;
import jp.co.cyberagent.android.gpuimage.PixelBuffer;

/**
 * Created by clocksmith on 3/20/16.
 */
public class PostProcessorUtils {
  private static final String TAG = PostProcessorUtils.class.getSimpleName();

  public static Bitmap applyTiling(int numTilesInRow, Bitmap original) {
    int tileWidth = original.getWidth() / numTilesInRow;
    int tileHeight = original.getHeight() / numTilesInRow;
    Bitmap singleTileBitmap = Bitmap.createScaledBitmap(original, tileWidth, tileHeight, true);
    Bitmap tiledBitmap = Bitmap.createBitmap(
        original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas tiledCanvas = new Canvas(tiledBitmap);
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    for (int i = 0; i < numTilesInRow; i++) {
      for (int j = 0; j < numTilesInRow; j++) {
        tiledCanvas.drawBitmap(singleTileBitmap, i * tileWidth, j * tileHeight, paint);
      }
    }
    return tiledBitmap;
  }

  public static Bitmap applyFilters(Bitmap src, List<GPUImageFilter> filters) {
    if (filters.isEmpty()) {
      return src;
    }

    PixelBuffer buffer = new PixelBuffer(src.getWidth(), src.getHeight());
    for (GPUImageFilter filter : filters) {
      GPUImageRenderer renderer = new GPUImageRenderer(filter);
      renderer.setImageBitmap(src, true);
      buffer.setRenderer(renderer);
      src = buffer.getBitmap();
//      renderer.deleteImage();
//      filter.destroy();
    }
    buffer.destroy();
    return src;
  }

  // TODO(clocksmith): figure out if this can be used
  public static Bitmap applyFiltersAsGroup(Context context, Bitmap original, ImmutableList<GPUImageFilter> filters) {
    GPUImageFilterGroup group = new GPUImageFilterGroup(filters);
    GPUImage gpuImage = new GPUImage(context);
    gpuImage.setFilter(group);
    Bitmap filtered = gpuImage.getBitmapWithFilterApplied(original);
    original.recycle();
    return filtered;
  }

  public static void renderText(Bitmap original, String text) {
    Canvas textCanvas = new Canvas(original);
    Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    textPaint.setColor(Color.WHITE); // Text Color
    textPaint.setTextSize(getTextSizeToFitWidth(text, original.getWidth(), 3 * original.getWidth() / 4));
    textPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
    textPaint.setTextAlign(Paint.Align.CENTER);
    textCanvas.drawText(text, original.getWidth() / 2, original.getHeight() / 2, textPaint);
  }

  public static void renderWatermark(Context context, Bitmap original) {
    if (Constants.USE_TEXT_WATERMARK) {
      Canvas canvas = new Canvas(original);

      Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
      textPaint.setColor(Color.WHITE); // Text Color
      textPaint.setTextSize(Constants.MAX_WATERMARK_TEXT_SIZE);
      textPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
      textPaint.setTextAlign(Paint.Align.LEFT);

      Rect textBounds = new Rect();
      textPaint.getTextBounds(Constants.WATERMARK_TEXT, 0, Constants.WATERMARK_TEXT.length(), textBounds);

      Paint rectPaint = new Paint();
      rectPaint.setColor(Color.BLACK);
      rectPaint.setAlpha(128);

      canvas.drawRect(
          0,
          original.getHeight() - textBounds.height() - Constants.WATERMARK_TEXT_PADDING,
          original.getWidth(),
          original.getHeight(),
          rectPaint);

      canvas.drawText(
          Constants.WATERMARK_TEXT,
          (original.getWidth() - textBounds.width()) / 2,
          original.getHeight() - Constants.WATERMARK_TEXT_PADDING,
          textPaint);
    } else if (Constants.USE_IMAGE_WATERMARK) {
      Bitmap watermarkBitmap = WatermarkProvider.getWatermarkBitmap(context);
      Canvas watermarkCanvas = new Canvas(original);
      Paint watermarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
      int watermarkWidth = original.getWidth();
      int watermarkHeight = watermarkBitmap.getHeight() * watermarkWidth / watermarkBitmap.getWidth();
      watermarkCanvas.drawBitmap(
          watermarkBitmap,
          null,
          new Rect(
              original.getWidth() - watermarkWidth,
              original.getHeight() - watermarkHeight,
              original.getWidth(),
              original.getHeight()),
          watermarkPaint);
    }
  }

  private static int getTextSizeToFitWidth(String text, int textSize, int desiredWidth) {
    Paint paint = new Paint();
    Rect bounds = new Rect();

    paint.setTextSize(textSize);
    paint.getTextBounds(text, 0, text.length(), bounds);

    while (bounds.width() > desiredWidth) {
      textSize--;
      paint.setTextSize(textSize);
      paint.getTextBounds(text, 0, text.length(), bounds);
    }

    return textSize;
  }
}
