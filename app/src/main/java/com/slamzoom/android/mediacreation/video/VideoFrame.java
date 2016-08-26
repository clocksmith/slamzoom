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
      data[offset] = (pix >> 16 & 0xff);
      data[offset + 1] = (pix >> 8 & 0xff);
      data[offset + 2] = pix & 0xff;
      offset += 3;
    }

    return picture;
  }

//  private Picture getPictureFromBitmap2() {
//    picture = Picture.create(bitmap.getWidth(), bitmap.getHeight(), ColorSpace.YUV420J);
//    int[] data = picture.getPlaneData(0);
//    int[] pixelInts = new int[bitmap.getWidth() * bitmap.getHeight()];
//    bitmap.getPixels(pixelInts, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
//
//    data = colorconvertRGB_IYUV_I420(pixelInts, bitmap.getWidth(), bitmap.getHeight());
//
//    BitmapUtils.recycleIfSupposedTo(bitmap);
//
//    int offset = 0;
//    for (int i = 0; i < pixelInts.length; i++) {
//      int pix = pixelInts[i];
//      data[offset] = (pix >> 16 & 0xff);
//      data[offset + 1] = (pix >> 8 & 0xff);
//      data[offset + 2] = pix & 0xff;
//      offset += 3;
//    }
//
//    return picture;
//  }
//
//  private static byte[] colorconvertRGB_IYUV_I420(int[] aRGB, int width, int height) {
//    final int frameSize = width * height;
//    final int chromasize = frameSize / 4;
//
//    int yIndex = 0;
//    int uIndex = frameSize;
//    int vIndex = frameSize + chromasize;
//    byte [] yuv = new byte[width*height*3/2];
//
//    int a, R, G, B, Y, U, V;
//    int index = 0;
//    for (int j = 0; j < height; j++) {
//      for (int i = 0; i < width; i++) {
//
//        //a = (aRGB[index] & 0xff000000) >> 24; //not using it right now
//        R = (aRGB[index] & 0xff0000) >> 16;
//        G = (aRGB[index] & 0xff00) >> 8;
//        B = (aRGB[index] & 0xff) >> 0;
//
//        Y = ((66 * R + 129 * G +  25 * B + 128) >> 8) +  16;
//        U = (( -38 * R -  74 * G + 112 * B + 128) >> 8) + 128;
//        V = (( 112 * R -  94 * G -  18 * B + 128) >> 8) + 128;
//
//        yuv[yIndex++] = (byte) ((Y < 0) ? 0 : ((Y > 255) ? 255 : Y));
//
//        if (j % 2 == 0 && index % 2 == 0)
//        {
//          yuv[uIndex++] = (byte)((U < 0) ? 0 : ((U > 255) ? 255 : U));
//          yuv[vIndex++] = (byte)((V < 0) ? 0 : ((V > 255) ? 255 : V));
//        }
//
//        index ++;
//      }
//    }
//    return yuv;
//  }

}
