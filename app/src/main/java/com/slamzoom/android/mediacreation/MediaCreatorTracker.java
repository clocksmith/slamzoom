package com.slamzoom.android.mediacreation;

import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

/**
 * Created by clocksmith on 6/13/16.
 */
public class MediaCreatorTracker {
  private Stopwatch mTransforming;

  private Stopwatch mFiltering;

  private Stopwatch mPixelizing;
  private Stopwatch mGettingPixels;
  private Stopwatch mConvertingToBytes;

  private Stopwatch mEncoding;
  private Stopwatch mNeoQuanting;
  private Stopwatch mMapping;

  private Stopwatch mWriting;

  public MediaCreatorTracker() {
    mTransforming = Stopwatch.createUnstarted();

    mFiltering = Stopwatch.createUnstarted();

    mPixelizing = Stopwatch.createUnstarted();
    mGettingPixels = Stopwatch.createUnstarted();
    mConvertingToBytes = Stopwatch.createUnstarted();

    mEncoding = Stopwatch.createUnstarted();
    mNeoQuanting = Stopwatch.createUnstarted();
    mMapping = Stopwatch.createUnstarted();

    mWriting = Stopwatch.createUnstarted();
  }

  public synchronized void startTransforming() {
    if (!mTransforming.isRunning()) {
      mTransforming.start();
    }
  }

  public synchronized void stopTransforming() {
    if (mTransforming.isRunning()) {
      mTransforming.stop();
    }
  }

  public synchronized void startFiltering() {
    if (!mFiltering.isRunning()) {
      mFiltering.start();
    }
  }

  public synchronized void stopFiltering() {
    if (mFiltering.isRunning()) {
      mFiltering.stop();
    }
  }

  public synchronized void startPixelizing() {
    if (!mPixelizing.isRunning()) {
      mPixelizing.start();
    }
  }

  public synchronized void stopPixelizing() {
    if (mPixelizing.isRunning()) {
      mPixelizing.stop();
    }
  }

  public synchronized void startGettingPixels() {
    if (!mGettingPixels.isRunning()) {
      mGettingPixels.start();
    }
  }

  public synchronized void stopGettingPixels() {
    if (mGettingPixels.isRunning()) {
      mGettingPixels.stop();
    }
  }

  public synchronized void startConvertingToBytes() {
    if (!mConvertingToBytes.isRunning()) {
      mConvertingToBytes.start();
    }
  }

  public synchronized void stopConvertingToBytes() {
    if (mConvertingToBytes.isRunning()) {
      mConvertingToBytes.stop();
    }
  }

  public synchronized void startEncoding() {
    if (!mEncoding.isRunning()) {
      mEncoding.start();
    }
  }

  public synchronized void stopEncoding() {
    if (mEncoding.isRunning()) {
      mEncoding.stop();
    }
  }

  public synchronized void startNeoQuanting() {
    if (!mNeoQuanting.isRunning()) {
      mNeoQuanting.start();
    }
  }

  public synchronized void stopNeoQuanting() {
    if (mNeoQuanting.isRunning()) {
      mNeoQuanting.stop();
    }
  }

  public synchronized void startMapping() {
    if (!mMapping.isRunning()) {
      mMapping.start();
    }
  }

  public synchronized void stopMapping() {
    if (mMapping.isRunning()) {
      mMapping.stop();
    }
  }

  public synchronized void startWriting() {
    if (!mWriting.isRunning()) {
      mWriting.start();
    }
  }

  public synchronized void stopWriting() {
    if (mWriting.isRunning()) {
      mWriting.stop();
    }
  }
  public String getReport() {
    return "transforming: " + mTransforming.elapsed(TimeUnit.MILLISECONDS) + "ms" +
        "\n\tfiltering: " + mFiltering.elapsed(TimeUnit.MILLISECONDS) + "ms" +
        "\n\tpixelizing: " + mPixelizing.elapsed(TimeUnit.MILLISECONDS) + "ms" +
        "\n\t\tgettingPixels: " + mGettingPixels.elapsed(TimeUnit.MILLISECONDS) + "ms" +
        "\n\t\tconvertingToBytes: " + mConvertingToBytes.elapsed(TimeUnit.MILLISECONDS) + "ms" +
        "\n\tencoding: " + mEncoding.elapsed(TimeUnit.MILLISECONDS) + "ms" +
        "\n\t\tneoQuanting: " + mNeoQuanting.elapsed(TimeUnit.MILLISECONDS) + "ms" +
        "\n\t\tmapping: " + mMapping.elapsed(TimeUnit.MILLISECONDS) + "ms" +
        "\n\twriting: " + mWriting.elapsed(TimeUnit.MILLISECONDS) + "ms" +
        "\ntotal: " + getTotal();
  }

  public String getTotal() {
    return (mTransforming.elapsed(TimeUnit.MILLISECONDS) +
        mFiltering.elapsed(TimeUnit.MILLISECONDS) +
        mPixelizing.elapsed(TimeUnit.MILLISECONDS) +
        mEncoding.elapsed(TimeUnit.MILLISECONDS) +
        mWriting.elapsed(TimeUnit.MILLISECONDS) + "ms");
  }
}
