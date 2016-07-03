package com.slamzoom.android.mediacreation;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by clocksmith on 6/13/16.
 */
public class MultiPhaseStopwatch {
  private LinkedHashMap<String, Stopwatch> mStopwatches;

  public MultiPhaseStopwatch() {
    mStopwatches = Maps.newLinkedHashMap();
  }

  public synchronized void start(String name) {
    if (mStopwatches.containsKey(name)) {
      Stopwatch stopwatch = mStopwatches.get(name);
      if (!stopwatch.isRunning()) {
        stopwatch.start();
      }
    } else {
      mStopwatches.put(name, Stopwatch.createStarted());
    }
  }

  public synchronized void stop(String name) {
    if (mStopwatches.containsKey(name)) {
      Stopwatch stopwatch = mStopwatches.get(name);
      if (stopwatch.isRunning()) {
        stopwatch.stop();
      }
    }
  }

  public synchronized void stopAll() {
    for (String name : mStopwatches.keySet()) {
      stop(name);
    }
  }

  public String getReport() {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, Stopwatch> entry : mStopwatches.entrySet()) {
      sb.append(entry.getKey() + ": " + entry.getValue().elapsed(TimeUnit.MILLISECONDS) + "ms\n");
    }
    sb.append(getTotalString());
    return sb.toString();
  }

  public String getTotalString() {
    return getTotal() + "ms";
  }

  public long getTotal() {
    long totalMs = 0;
    for (Stopwatch stopwatch : mStopwatches.values()) {
      totalMs += stopwatch.elapsed(TimeUnit.MILLISECONDS);
    }
    return totalMs;
  }
}
