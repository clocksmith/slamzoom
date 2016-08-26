package com.slamzoom.android.mediacreation.video;

import android.graphics.Bitmap;

import com.slamzoom.android.common.utils.BitmapUtils;
import com.slamzoom.android.mediacreation.MediaFrame;

import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Picture;

/**
 * Created by clocksmith on 3/25/16.
 */
public class VideoFrame extends MediaFrame {
  public Picture picture;

  public VideoFrame(Bitmap bitmap, int delayMillis) {
    super(bitmap, delayMillis);
    picture = getPictureFromBitmap();
  }

  private Picture getPictureFromBitmap() {
    picture = Picture.create(bitmap.getWidth(), bitmap.getHeight(), ColorSpace.RGB);
    int[] data = picture.getPlaneData(0);
    int[] pixelInts = new int[bitmap.getWidth() * bitmap.getHeight()];
    bitmap.getPixels(pixelInts, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
    BitmapUtils.recycleIfSupposedTo(bitmap);

    int offset = 0;
    for (int i = 0; i < pixelInts.length; i++) {
      int pix = pixelInts[i];
      data[offset] = pix & 0xff;
      data[offset + 1] = (pix >> 8 & 0xff);
      data[offset + 2] = (pix >> 16 & 0xff);
      offset += 3;
    }

    return picture;
  }
}
