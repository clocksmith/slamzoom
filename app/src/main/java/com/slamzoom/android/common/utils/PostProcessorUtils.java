package com.slamzoom.android.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import com.google.common.collect.ImmutableList;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.FontLoader;
import com.slamzoom.android.mediacreation.WatermarkProvider;

import java.util.List;

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
      renderer.setImageBitmap(src, false);
      buffer.setRenderer(renderer);
      src = buffer.getBitmap();
      renderer.deleteImage();
      filter.destroy();
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
    BitmapUtils.recycleIfSupposedTo(original);
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
      textPaint.setTypeface(FontLoader.getInstance().getDefaultFont());
      textPaint.setShadowLayer(2, 0, 0, Color.BLACK);

//      Paint strokePaint = new Paint(textPaint);
//      strokePaint.setStyle(Paint.Style.STROKE);
//      strokePaint.setColor(Color.BLACK);
//      strokePaint.setStrokeWidth(1);

      Rect textBounds = new Rect();
      textPaint.getTextBounds(Constants.WATERMARK_TEXT, 0, Constants.WATERMARK_TEXT.length(), textBounds);

      canvas.drawText(
          Constants.WATERMARK_TEXT,
          original.getWidth() - textBounds.width() - Constants.WATERMARK_TEXT_PADDING,
          original.getHeight() - Constants.WATERMARK_TEXT_PADDING,
          textPaint);
//      canvas.drawText(
//          Constants.WATERMARK_TEXT,
//          original.getWidth() - textBounds.width() - Constants.WATERMARK_TEXT_PADDING,
//          original.getHeight() - Constants.WATERMARK_TEXT_PADDING,
//          strokePaint);
    } else if (Constants.USE_IMAGE_WATERMARK) {
      Bitmap watermarkBitmap = WatermarkProvider.getWatermarkBitmap(context);
      Canvas watermarkCanvas = new Canvas(original);
      Paint watermarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
      int watermarkWidth = Math.min(original.getWidth() / 2, Constants.DEFAULT_GIF_SIZE_PX / 3);
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
