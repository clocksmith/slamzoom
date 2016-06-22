package com.slamzoom.android.mediacreation.gif;

import android.graphics.Bitmap;

import com.slamzoom.android.mediacreation.MediaCreatorTracker;
import com.slamzoom.android.mediacreation.MediaFrame;

import java.nio.ByteBuffer;

/**
 * Created by clocksmith on 3/11/16.
 */
public class GifFrame extends MediaFrame {
//  public Bitmap bitmap;
  public int delayMillis;
  byte[] pixelBytes;
  int width;
  int height;
  private MediaCreatorTracker mTracker;

  public GifFrame(Bitmap bitmap, int delayMillis, MediaCreatorTracker tracker) {
    width = bitmap.getWidth();
    height = bitmap.getHeight();
    mTracker = tracker;

    mTracker.startGettingPixels();
    int[] pixelInts = new int[width * height];
    bitmap.getPixels(pixelInts, 0, width, 0, 0, width, height);
    bitmap.recycle();
    mTracker.stopGettingPixels();
    mTracker.startConvertingToBytes();
    this.pixelBytes = getPixelBytes(pixelInts);
    mTracker.stopConvertingToBytes();

    this.delayMillis = delayMillis;
  }

//  private byte[] getPixelBytes(int[] pixelInts) {
//    byte[] bytes = new byte[pixelInts.length * 3];
//
//    int byteIdx = 0;
//    for(int i = 0; i < pixelInts.length; i++) {
//      int thisPixel = pixelInts[i];
//      byte[] theseBytes = ByteBuffer.allocate(4).putInt(thisPixel).array();
//      // RGB --> BGR
//      bytes[byteIdx] = theseBytes[3];
//      bytes[byteIdx + 1] = theseBytes[2];
//      bytes[byteIdx + 2] = theseBytes[1];
//      byteIdx += 3;
//    }
//    return bytes;
//  }

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

//  private byte[] getPixelBytes(int[] pixelInts) {
//    byte[] bytes = new byte[pixelInts.length * 3];
//
//    int byteIdx = 0;
//    for(int i = 0; i < pixelInts.length; i++) {
//      int thisPixel = pixelInts[i];
//      byte[] theseBytes = intToByteArray(thisPixel);
//      bytes[byteIdx] = theseBytes[3];
//      bytes[byteIdx + 1] = theseBytes[2];
//      bytes[byteIdx + 2] = theseBytes[1];
//      byteIdx += 3;
//    }
//    return bytes;
//  }
//
//  public static final byte[] intToByteArray(int value) {
//    return new byte[] {
//        (byte)(value >>> 24),
//        (byte)(value >>> 16),
//        (byte)(value >>> 8),
//        (byte)value};
//  }
}