package com.slamzoom.android.common.executor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by clocksmith on 6/9/16.
 */
public class ExecutorProvider {
  private static ThreadPoolExecutor COLLECT_FRAMES_EXECUTOR;
  private static ThreadPoolExecutor ENCODE_FRAMES_EXECUTOR;

  public static ThreadPoolExecutor getCollectFramesExecutor() {
    if (COLLECT_FRAMES_EXECUTOR == null) {
      COLLECT_FRAMES_EXECUTOR = ExecutorFactory.create(2, 4);
    }
    return COLLECT_FRAMES_EXECUTOR;
  }

  public static ThreadPoolExecutor getEncodeFramesExecutor() {
    if (ENCODE_FRAMES_EXECUTOR == null) {
      ENCODE_FRAMES_EXECUTOR = ExecutorFactory.create();
    }
    return ENCODE_FRAMES_EXECUTOR;
  }
}
