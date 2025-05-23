package com.slamzoom.android.mediacreation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.slamzoom.android.R;
import com.slamzoom.android.common.fonts.FontProvider;

import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRenderer;
import jp.co.cyberagent.android.gpuimage.PixelBuffer;

/**
 * Created by clocksmith on 3/20/16.
 */
public class PostProcessorUtils {
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

  public static void renderText(Bitmap original, String text) {
    Canvas textCanvas = new Canvas(original);
    Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    textPaint.setColor(Color.WHITE);
    int desiredWidth = original.getWidth() - 20;
    int maxTextSize = 60;
    textPaint.setTextSize(Math.min(maxTextSize, getTextSizeToFitWidth(text, original.getWidth(), desiredWidth)));
    Rect bounds = new Rect();
    textPaint.getTextBounds(text, 0, text.length(), bounds);
    textPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
    textPaint.setTextAlign(Paint.Align.CENTER);
    textPaint.setTypeface(FontProvider.getInstance().getDefaultFont());
    int x = original.getWidth() / 2;
    int y = (original.getHeight() + bounds.height()) / 2;
    textCanvas.drawText(text, x, y, textPaint);
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

  public static void renderWatermark(Context context, Bitmap original) {
    Canvas canvas = new Canvas(original);

    Paint textPaint = new Paint(Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    textPaint.setColor(Color.WHITE); // Text Color
    textPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.max_watermark_text_size));
    textPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
    textPaint.setTextAlign(Paint.Align.LEFT);
    textPaint.setTypeface(FontProvider.getInstance().getTitleFont());
    textPaint.setShadowLayer(2, 0, 0, Color.BLACK);

    Rect textBounds = new Rect();
    String watermarkText = context.getString(R.string.app_name_lowercase);
    textPaint.getTextBounds(watermarkText, 0, watermarkText.length(), textBounds);

    float margin = context.getResources().getDimensionPixelOffset(R.dimen.watermark_margin);
    canvas.drawText(
        watermarkText,
        original.getWidth() - textBounds.width() - margin,
        original.getHeight() - margin,
        textPaint);
  }
}
