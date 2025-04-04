package com.slamzoom.android.common.threads;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by clocksmith on 3/17/16.
 */
public class ExecutorFactory {
  public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
  private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
  private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
  private static final int KEEP_ALIVE = 1;

  public static ThreadPoolExecutor create(int corePoolSize, int maxPoolSize) {
    BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE);

    ThreadFactory sThreadFactory = new ThreadFactory() {
      private final AtomicInteger mCount = new AtomicInteger(1);

      public Thread newThread(Runnable r) {
        return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
      }
    };

    return new ThreadPoolExecutor(
        corePoolSize,
        maxPoolSize,
        KEEP_ALIVE,
        TimeUnit.SECONDS,
        sPoolWorkQueue,
        sThreadFactory);
  }

  public static ThreadPoolExecutor create() {
    return create(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE);
  }
}

