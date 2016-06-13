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
  private Stopwatch mEncoding;
  private Stopwatch mWriting;

  public MediaCreatorTracker() {
    mTransforming = Stopwatch.createUnstarted();
    mFiltering = Stopwatch.createUnstarted();
    mPixelizing = Stopwatch.createUnstarted();
    mEncoding = Stopwatch.createUnstarted();
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
        "\nfiltering: " + mFiltering.elapsed(TimeUnit.MILLISECONDS) + "ms" +
        "\npixelizing: " + mPixelizing.elapsed(TimeUnit.MILLISECONDS) + "ms" +
        "\nencoding: " + mEncoding.elapsed(TimeUnit.MILLISECONDS) + "ms" +
        "\nwriting: " + mWriting.elapsed(TimeUnit.MILLISECONDS) + "ms" +
        "\ntotal: " +
        (mTransforming.elapsed(TimeUnit.MILLISECONDS) +
            mFiltering.elapsed(TimeUnit.MILLISECONDS) +
            mPixelizing.elapsed(TimeUnit.MILLISECONDS) +
            mEncoding.elapsed(TimeUnit.MILLISECONDS) +
            mWriting.elapsed(TimeUnit.MILLISECONDS) + "ms");
  }
}
