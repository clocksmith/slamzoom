package com.slamzoom.android.mediacreation.gif;

import android.graphics.Bitmap;

import com.slamzoom.android.common.bitmaps.BitmapUtils;
import com.slamzoom.android.mediacreation.StopwatchTracker;
import com.slamzoom.android.mediacreation.MediaFrame;

import java.nio.ByteBuffer;

/**
 * Created by clocksmith on 3/11/16.
 */
public class GifFrame extends MediaFrame {
  public static final String STOPWATCH_CONVERTING_PIXELS_TO_BYTES = "pixel to byte converting";

  public byte[] pixelBytes;

  public GifFrame(Bitmap bitmap, int delayMillis, StopwatchTracker tracker) {
    super(bitmap, delayMillis);

    int[] pixelInts = new int[width * height];
    this.bitmap.getPixels(pixelInts, 0, width, 0, 0, width, height);
    BitmapUtils.recycleIfSupposedTo(this.bitmap);
    tracker.start(STOPWATCH_CONVERTING_PIXELS_TO_BYTES);
    this.pixelBytes = getPixelBytes(pixelInts);
    tracker.stop(STOPWATCH_CONVERTING_PIXELS_TO_BYTES);
  }

  private byte[] getPixelBytes(int[] pixelInts) {
    ByteBuffer buffer = ByteBuffer.allocate(3 * pixelInts.length);
    for (int i = 0; i < pixelInts.length; i++) {
      int pix = pixelInts[i];
      // drop the alpha
      buffer.put((byte) (pix & 0xff));
      buffer.put((byte) (pix >> 8 & 0xff));
      buffer.put((byte) (pix >> 16 & 0xff));
    }
    return buffer.array();
  }
}