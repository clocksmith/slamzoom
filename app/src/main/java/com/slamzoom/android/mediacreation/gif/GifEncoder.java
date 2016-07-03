package com.slamzoom.android.mediacreation.gif;

import android.os.AsyncTask;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.singletons.ExecutorProvider;
import com.slamzoom.android.common.utils.SzLog;
import com.slamzoom.android.mediacreation.MultiPhaseStopwatch;
import com.slamzoom.android.mediacreation.MediaEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by clocksmith on 3/9/16.
 */
public class GifEncoder implements MediaEncoder<GifFrame, GifCreator.CreateGifCallback> {
  private static final String TAG = GifEncoder.class.getSimpleName();

  public interface ProgressUpdateListener {
    void onProgressUpdate(double amountToUpdate);
  }

  public static final String STOPWATCH_ENCODING = "encoding";
  public static final String STOPWATCH_QUANTING = "quanting";
  public static final String STOPWATCH_MAPPING = "mapping";
  public static final String STOPWATCH_WRITING= "writing";

  private static final int COLOR_DEPTH = 8; // number of bit planes
  private static final int PAL_SIZE = 7; // color table size (bits-1)
  private static final int SAMPLE = 20;

  private int mWidth;
  private int mHeight;

  private ByteArrayOutputStream mOut;
  private List<GifFrame> mFrames;
  private boolean mUserLocalColorTables;
  private NeuQuant mGloabalNq;
  private byte[] mGlobalColorTable;

  private Set<AsyncTask> mTasks = Sets.newConcurrentHashSet();
  private List<Runnable> mFrameWriters;
  private AtomicInteger mTotalNumFrameWritersToAdd;
  private GifCreator.CreateGifCallback mCallback;

  private ProgressUpdateListener mProgressUpdateListener;
  private long mEncodingFramesStart;

  private MultiPhaseStopwatch mTracker;

  public GifEncoder() {
    this(Constants.DEFAULT_USE_LOCAL_COLOR_PALETTE);
  }

  public GifEncoder(boolean useLocalColorTables) {
    mOut = new ByteArrayOutputStream();
    mFrames = Lists.newArrayList();
    mUserLocalColorTables = useLocalColorTables;
  }

  public void setTracker(MultiPhaseStopwatch tracker) {
    mTracker = tracker;
  }

  public void setProgressUpdateListener(ProgressUpdateListener listener) {
    mProgressUpdateListener = listener;
  }

  @Override
  public void cancel() {
    for (AsyncTask task : mTasks) {
      task.cancel(true);
    }
  }

  @Override
  public void addFrames(Iterable<GifFrame> frames) {
    for (GifFrame frame : frames) {
      addFrame(frame);
    }
  }

  public void addFrame(GifFrame frame) {
    setOrVerifyGifDimensions(frame);
    mFrames.add(frame);
  }

  public void encodeAsync(GifCreator.CreateGifCallback callback) {
    mCallback = callback;
    mEncodingFramesStart = System.currentTimeMillis();

    mFrameWriters = Lists.newArrayListWithCapacity(mFrames.size());
    for (int frameIndex = 0; frameIndex < mFrames.size(); frameIndex++) {
      mFrameWriters.add(null);
    }
    mTotalNumFrameWritersToAdd = new AtomicInteger(mFrameWriters.size());

    GetFirstFrameWriterTask getFirstFrameWriterTask = new GetFirstFrameWriterTask();
    mTasks.add(getFirstFrameWriterTask);
    mTracker.start(STOPWATCH_ENCODING);
    getFirstFrameWriterTask.executeOnExecutor(ExecutorProvider.getEncodeFramesExecutor());
  }

  private void setOrVerifyGifDimensions(GifFrame frame) {
    if (mWidth < 1 && mHeight < 1) {
      mWidth = frame.width;
      mHeight = frame.height;
    }
    if (mWidth > 0 && mWidth != frame.width || mHeight > 0 & mHeight != frame.height) {
      SzLog.e(TAG, "Bitmap does not have same dimensions as previous frames");
    }
  }

  private Runnable getFrameWriter(final GifFrame frame, final int frameIndex) {
    final boolean firstFrame = frameIndex == 0;

    int len = frame.pixelBytes.length;
    int nPix = len / 3;
//    final ByteBuffer indexedPixelsBuffer = ByteBuffer.allocate(nPix);
    final byte[] indexedPixels = new byte[nPix];

    mTracker.start(STOPWATCH_QUANTING);
    final NeuQuant nq;
    final byte[] colorTable;
    if (mUserLocalColorTables || firstFrame) {
      nq = new NeuQuant(frame.pixelBytes, len, SAMPLE);
      colorTable = nq.process(); // create reduced palette
      if (firstFrame) {
        mGloabalNq = nq;
        mGlobalColorTable = colorTable;
      }

      for (int i = 0; i < colorTable.length; i += 3) {
        byte temp = colorTable[i];
        colorTable[i] = colorTable[i + 2];
        colorTable[i + 2] = temp;
      }
    } else {
      nq = mGloabalNq;
      colorTable = mGlobalColorTable;
    }
    mTracker.stop(STOPWATCH_QUANTING);

    mTracker.start(STOPWATCH_MAPPING);
    int k = 0;
    for (int i = 0; i < nPix; i++) {
      int index =
          nq.map(frame.pixelBytes[k++] & 0xff,
              frame.pixelBytes[k++] & 0xff,
              frame.pixelBytes[k++] & 0xff);
//      indexedPixelsBuffer.put((byte) index);
      indexedPixels[i] = (byte) index;
    }
    mTracker.stop(STOPWATCH_MAPPING);

    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        try {
          if (firstFrame) {
            writeLSD();
            writePalette(colorTable);
            writeNetscapeExt();
          }
          writeGraphicCtrlExt(Math.round(frame.delayMillis / 10f)); // delay is in hundredths
          writeImageDesc(firstFrame);
          if (!firstFrame) {
            writePalette(colorTable);
          }
//          writePixels(indexedPixelsBuffer.array());
          writePixels(indexedPixels);
        } catch (IOException e) {
          SzLog.e(TAG, "Could not write to output stream", e);
        }
      }
    };

//    Log.d(TAG, "Finished getting frameWriter for frame "
//        + frameIndex + " in " + (System.currentTimeMillis() - start) + "ms");
    if (mProgressUpdateListener != null) {
      mProgressUpdateListener.onProgressUpdate(1.0 / 3 / mFrames.size());
    }

    return runnable;
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
   * Write 16-bit value to output stream, LSB first
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

  private class GetFirstFrameWriterTask extends AsyncTask<Void, Void, Runnable> {
    @Override
    protected Runnable doInBackground(Void... params) {
      return getFrameWriter(mFrames.get(0), 0);
    }

    @Override
    protected void onPostExecute(Runnable frameWriter) {
      mTotalNumFrameWritersToAdd.decrementAndGet();
      mFrameWriters.set(0, frameWriter);
        for (int frameIndex = 1; frameIndex < mFrames.size(); frameIndex++) {
//          Log.wtf(TAG, "create frame writer task for frame: " + frameIndex);
          GetFrameWriterTask getFrameWriterTask = new GetFrameWriterTask(frameIndex);
          mTasks.add(getFrameWriterTask);
          getFrameWriterTask.executeOnExecutor(ExecutorProvider.getEncodeFramesExecutor());
//          Log.wtf(TAG, "frame writer task for frame added to exec: " + frameIndex);
        }
      }
  }

  private class GetFrameWriterTask extends AsyncTask<Void, Void, Runnable> {
    private int mFrameIndex;

    public GetFrameWriterTask(int frameIndex) {
      mFrameIndex = frameIndex;
    }

    @Override
    protected Runnable doInBackground(Void... params) {
//      Log.wtf(TAG, "executing frame writer task for frame: " + mFrameIndex);
      return getFrameWriter(mFrames.get(mFrameIndex), mFrameIndex);
    }

    @Override
    protected void onPostExecute(Runnable frameWriter) {
      mFrameWriters.set(mFrameIndex, frameWriter);

//      Log.wtf(TAG, "executed frame writer task for frame: " + mFrameIndex);

      if (mTotalNumFrameWritersToAdd.decrementAndGet() == 0) {
        mTracker.stop(STOPWATCH_ENCODING);
//        Log.wtf(TAG, "Finished encoding frames in " + (System.currentTimeMillis() - mEncodingFramesStart) + "ms");
        WriteFramesTask writeFramesTask = new WriteFramesTask();
        mTasks.add(writeFramesTask);
        mTracker.start(STOPWATCH_WRITING);
        new WriteFramesTask().execute();
      }
    }

    private class WriteFramesTask extends AsyncTask<Void, Void, Void> {
      @Override
      protected Void doInBackground(Void... params) {
        try {
          writeString("GIF89a");
        } catch (IOException e) {
          SzLog.e(TAG, "Could not write gif header", e);
        }

        for (Runnable frameWriterToRun : mFrameWriters) {
          frameWriterToRun.run();
          if (mProgressUpdateListener != null) {
            mProgressUpdateListener.onProgressUpdate(1.0 / 3 / mFrameWriters.size());
          }
        }

        mOut.write(0x3b); // gif trailer
        try {
          mOut.flush();
        } catch (IOException e) {
          SzLog.e(TAG, "Unable to flush output stream", e);
        }

        byte[] gifBytes = mOut.toByteArray();
        mTracker.stop(STOPWATCH_WRITING);
//        Log.wtf(TAG, "Finished writing frames in " + (System.currentTimeMillis() - start) + "ms");
        mCallback.onCreateGif(gifBytes);
        return null;
      }
    }
  }
}
