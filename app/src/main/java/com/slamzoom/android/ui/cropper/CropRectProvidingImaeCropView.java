package com.slamzoom.android.ui.cropper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import com.naver.android.helloyako.imagecrop.util.BitmapLoadUtils;
import com.naver.android.helloyako.imagecrop.view.ImageCropView;
import com.naver.android.helloyako.imagecrop.view.ImageCropViewBase;

import java.lang.reflect.Field;

/**
 * Created by clocksmith on 3/24/16.
 *
 * Hack to get the damn crop rect.
 */
public class CropRectProvidingImaeCropView extends ImageCropView {
  public CropRectProvidingImaeCropView(Context context) {
    this(context, null);
  }

  public CropRectProvidingImaeCropView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CropRectProvidingImaeCropView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public Rect getCropRect() {
    try {
      Field field = ImageCropViewBase.class.getDeclaredField("baseScale");
      field.setAccessible(true);
      Object baseScale = field.get(this);
      field.setAccessible(false);

      float scale = (Float) baseScale * getScale();

      RectF viewImageRect = getBitmapRect();

      float x = Math.abs(viewImageRect.left - mCropRect.left) / scale;
      float y = Math.abs(viewImageRect.top - mCropRect.top) / scale;
      float actualCropWidth = mCropRect.width() / scale;
      float actualCropHeight = mCropRect.height() / scale;

      if (x < 0) {
        x = 0;
      }

      if (y < 0) {
        y = 0;
      }

      if (y + actualCropHeight > getViewBitmap().getHeight()) {
        actualCropHeight = getViewBitmap().getHeight() - y;
      }

      if (x + actualCropWidth > getViewBitmap().getWidth()) {
        actualCropWidth = getViewBitmap().getWidth() - x;
      }

      Rect rect = new Rect();
      new RectF(x, y, x + actualCropWidth, y + actualCropHeight).round(rect);
      return rect;
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
