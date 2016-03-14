package com.slamzoom.android.gif.encoder;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.common.collect.Lists;
import com.slamzoom.android.common.ByteUtils;
import com.slamzoom.android.common.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by clocksmith on 3/9/16.
 */
public class GifEncoder {
  private static final String TAG = GifEncoder.class.getSimpleName();

  public static final int DEFAULT_FPS = 15;

  private static final int COLOR_DEPTH = 8; // number of bit planes
  private static final int PAL_SIZE = 7; // color table size (bits-1)
  private static final int SAMPLE = 30;

  private ByteArrayOutputStream mOut;
  private List<Frame> mFrames;

  private int mFps = DEFAULT_FPS;
  private int mDelayHundreths = Math.round(100f / mFps);

  private int mWidth;
  private int mHeight;

  public GifEncoder() {
    mOut = new ByteArrayOutputStream();
    mFrames = Lists.newArrayList();
  }

  public void addFrame(Bitmap bitmap) throws InvalidObjectException {
    addFrame(bitmap, 0);
  }

  public void addFrame(Bitmap bitmap, int extraDelayMillis) throws InvalidObjectException {
    setOrVerifyGifDimensions(bitmap);
    mFrames.add(new Frame(bitmap, mDelayHundreths + Math.round(extraDelayMillis / 10f)));
  }

  public byte[] encode() throws IOException {
    long start = System.currentTimeMillis();
    Log.d(TAG, "Starting GIF encode...");

    writeString("GIF89a");

    List<FutureTask<Runnable>> getPixelsFutureTasks = Lists.newArrayList();
    getPixelsFutureTasks.add(new FutureTask<>(new Callable<Runnable>() {
      @Override
      public Runnable call() throws Exception {
        return getFrameWriter(mFrames.get(0), true);
      }
    }));
    for (final Frame frame : mFrames.subList(1, mFrames.size())) {
      getPixelsFutureTasks.add(new FutureTask<>(new Callable<Runnable>() {
        @Override
        public Runnable call() throws Exception {
          return getFrameWriter(frame, false);
        }
      }));
    }

    ExecutorService executorService = Executors.newFixedThreadPool(Constants.DEFAULT_THREAD_POOL_SIZE);
    for (FutureTask<Runnable> futureTask : getPixelsFutureTasks) {
      executorService.execute(futureTask);
    }

    for (FutureTask<Runnable> futureTask : getPixelsFutureTasks) {
      try {
        futureTask.get().run();
      } catch (ExecutionException | InterruptedException e) {
        Log.e(TAG, "Could not get runnable from future");
      }
    }

    mOut.write(0x3b); // gif trailer
    mOut.flush();

    Log.d(TAG, "Finished GIF encode in " + (System.currentTimeMillis() - start) + "ms");
    return mOut.toByteArray();
  }


  private void setOrVerifyGifDimensions(Bitmap bitmap) throws InvalidObjectException {
    if (mWidth < 1 && mHeight < 1) {
      mWidth = bitmap.getWidth();
      mHeight = bitmap.getHeight();
    }
    if (mWidth > 0 && mWidth != bitmap.getWidth() || mHeight > 0 & mHeight != bitmap.getHeight()) {
      throw new InvalidObjectException("Bitmap does not have same dimensions as previous frames");
    }
  }

  private Runnable getFrameWriter(final Frame frame, final boolean firstFrame) {
    int[] pixelInts = new int[mWidth * mHeight * 3];
    frame.bitmap.getPixels(pixelInts, 0, mWidth, 0, 0, mWidth, mHeight);
//    frame.bitmap.recycle();
//    frame.bitmap = null;
    byte[] pixelBytes = getPixelBytes(pixelInts);

    int len = pixelBytes.length;
    int nPix = len / 3;
    final byte[] indexedPixels = new byte[nPix];
    NeuQuant nq = new NeuQuant(pixelBytes, len, SAMPLE);
    final byte[] colorTable = nq.process(); // create reduced palette

    for (int i = 0; i < colorTable.length; i += 3) {
      byte temp = colorTable[i];
      colorTable[i] = colorTable[i + 2];
      colorTable[i + 2] = temp;
    }

    int k = 0;
    for (int i = 0; i < nPix; i++) {
      int index =
          nq.map(pixelBytes[k++] & 0xff,
              pixelBytes[k++] & 0xff,
              pixelBytes[k++] & 0xff);
      indexedPixels[i] = (byte) index;
    }

    return new Runnable() {
      @Override
      public void run() {
        try {
          if (firstFrame) {
            writeLSD();
            writePalette(colorTable);
            writeNetscapeExt();
          }
          writeGraphicCtrlExt(frame.delay);
          writeImageDesc(firstFrame);
          if (!firstFrame) {
            writePalette(colorTable);
          }
          writePixels(indexedPixels);
        }
        catch (IOException e) {
          Log.e(TAG, "Could not write to output stream", e);
        }
      }
    };
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

  /**
   * Writes Logical Screen Descriptor
   */
  private void writeLSD() throws IOException {
    // logical screen size
    writeShort(mWidth);
    writeShort(mHeight);
    // packed fields
    // 1   : global color table flag = 1 (gct used)
    // 2-4 : color resolution = 7
    // 5   : gct sort flag = 0
    // 6-8 : gct size
    mOut.write((0x80 | 0x70 | PAL_SIZE)); // 6-8 : gct size

    mOut.write(0); // background color index
    mOut.write(0); // pixel aspect ratio - assume 1:1
  }

  /**
   * Writes color table
   */
  private void writePalette(byte[] colorTab) throws IOException {
    mOut.write(colorTab, 0, colorTab.length);
    int n = (3 * 256) - colorTab.length;
    for (int i = 0; i < n; i++) {
      mOut.write(0);
    }
  }

  /**
   * Writes Netscape application extension to define
   * repeat count.
   */
  protected void writeNetscapeExt() throws IOException {
    mOut.write(0x21); // extension introducer
    mOut.write(0xff); // app extension label
    mOut.write(11); // block size
    writeString("NETSCAPE" + "2.0"); // app id + auth code
    mOut.write(3); // sub-block size
    mOut.write(1); // loop sub-block id
    writeShort(0); // loop count (extra iterations, 0=repeat forever)
    mOut.write(0); // block terminator
  }

  /**
   * Writes Graphic Control Extension
   */
  protected void writeGraphicCtrlExt(int delay) throws IOException {
    mOut.write(0x21); // extension introducer
    mOut.write(0xf9); // GCE label
    mOut.write(4); // data block size

    // packed fields
    // 1:3 reserved
    // 4:6 disposal
    // 7   user input - 0 = none
    // 8   transparency flag
    mOut.write(0);

    writeShort(delay); // delay x 1/100 sec
    mOut.write(0); // transparent color index
    mOut.write(0); // block terminator
  }

  /**
   * Writes Image Descriptor
   */
  protected void writeImageDesc(boolean firstFrame) throws IOException {
    mOut.write(0x2c); // image separator
    writeShort(0); // image position x,y = 0,0
    writeShort(0);
    writeShort(mWidth); // image size
    writeShort(mHeight);
    // packed fields
    if (firstFrame) {
      // no LCT  - GCT is used for first (or only) frame
      mOut.write(0);
    } else {
      // specify normal LCT
      // 1 local color table  1=yes
      // 2 interlace - 0=no
      // 3 sorted - 0=no
      // 4-5 reserved
      // 6-8 size of color table
      mOut.write(0x80 | PAL_SIZE); // 6-8 size of color table
    }
  }


  /**
   * Encodes and writes pixel data
   */
  private void writePixels(byte[] indexedPixels) throws IOException {
    LzwEncoder encoder = new LzwEncoder(mWidth, mHeight, indexedPixels, COLOR_DEPTH);
    encoder.encode(mOut);
  }

  /**
   *    Write 16-bit value to output stream, LSB first
   */
  private void writeShort(int value) throws IOException {
    mOut.write(value & 0xff);
    mOut.write((value >> 8) & 0xff);
  }

  /**
   * Writes string to output stream
   */
  protected void writeString(String s) throws IOException {
    for (int i = 0; i < s.length(); i++) {
      mOut.write((byte) s.charAt(i));
    }
  }
}
