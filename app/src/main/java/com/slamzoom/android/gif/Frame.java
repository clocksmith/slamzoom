package com.slamzoom.android.gif;

import android.graphics.Bitmap;

import java.nio.ByteBuffer;

/**
 * Created by clocksmith on 3/11/16.
 */
public class Frame {
//  public Bitmap bitmap;
  public int delayMillis;
  byte[] pixelBytes;
  int width;
  int height;

  public Frame(Bitmap bitmap, int delayMillis) {
//    this.bitmap = bitmap;
    width = bitmap.getWidth();
    height = bitmap.getHeight();

    int[] pixelInts = new int[width * height];
    bitmap.getPixels(pixelInts, 0, width, 0, 0, width, height);
    this.pixelBytes = getPixelBytes(pixelInts);

    this.delayMillis = delayMillis;
  }

  private byte[] getPixelBytes(int[] pixelInts) {
    byte[] bytes = new byte[pixelInts.length * 3];

    int byteIdx = 0;
    for(int i=0; i < pixelInts.length; i++) {
      int thisPixel = pixelInts[i];
      byte[] theseBytes = ByteBuffer.allocate(4).putInt(thisPixel).array();
      // RGB --> BGR
      bytes[byteIdx] = theseBytes[3];
      bytes[byteIdx+1] = theseBytes[2];
      bytes[byteIdx+2] = theseBytes[1];
      byteIdx += 3;
    }
    return bytes;
  }
}