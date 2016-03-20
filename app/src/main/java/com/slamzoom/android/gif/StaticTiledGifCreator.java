package com.slamzoom.android.gif;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.slamzoom.android.ui.effect.EffectModel;

/**
 * Created by clocksmith on 3/18/16.
 */
public class StaticTiledGifCreator extends GifCreator {
  private int mNumTilesInRow;

  public StaticTiledGifCreator(
      Bitmap selectedBitmap,
      EffectModel effectModel,
      int gifSize,
      boolean isFinalGif,
      CreateGifCallback callback) {
    super(selectedBitmap, effectModel, gifSize, isFinalGif, callback);
    mNumTilesInRow = effectModel.getNumTilesInRow();
  }

  @Override
  protected Frame getFrame(Matrix matrix, int delayMillis) {
    // Transform the selected bitmap
    Bitmap targetBitmap = Bitmap.createBitmap(
        mSelectedBitmap.getWidth(), mSelectedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(targetBitmap);
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setDither(true);
    canvas.drawBitmap(mSelectedBitmap, matrix, paint);

    // Write subframes into new bitmap
    int tileWidth = targetBitmap.getWidth() / mNumTilesInRow;
    int tileHeight = targetBitmap.getHeight() / mNumTilesInRow;
    Bitmap bitmapTile = Bitmap.createScaledBitmap(targetBitmap, tileWidth, tileHeight, true);
    Bitmap tiledTargetBitmap = Bitmap.createBitmap(
        targetBitmap.getWidth(), targetBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas tiledCanvas = new Canvas(tiledTargetBitmap);
    for (int i = 0; i < mNumTilesInRow; i++) {
      for (int j = 0; j < mNumTilesInRow; j++) {
        tiledCanvas.drawBitmap(bitmapTile, i * tileWidth, j * tileHeight, paint);
      }
    }
    Bitmap scaledTiledBitmap = Bitmap.createScaledBitmap(tiledTargetBitmap, mGifWidth, mGifHeight, true);
    Frame frame = new Frame(scaledTiledBitmap, delayMillis);
    return frame;
  }
}
