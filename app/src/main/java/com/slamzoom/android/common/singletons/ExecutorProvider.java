package com.slamzoom.android.common.singletons;

import com.slamzoom.android.common.utils.ExecutorFactory;

import java.util.concurrent.Executor;

/**
 * Created by clocksmith on 6/9/16.
 */
public class ExecutorProvider {
  private static Executor COLLECT_FRAMES_EXECUTOR;
  private static Executor ENCODE_FRAMES_EXECUTOR;

  public static Executor getCollectFramesExecutor() {
    if (COLLECT_FRAMES_EXECUTOR == null) {
      COLLECT_FRAMES_EXECUTOR = ExecutorFactory.create();
    }
    return COLLECT_FRAMES_EXECUTOR;
  }

  public static Executor getEncodeFramesExecutor() {
    if (ENCODE_FRAMES_EXECUTOR == null) {
      ENCODE_FRAMES_EXECUTOR = ExecutorFactory.create();
    }
    return ENCODE_FRAMES_EXECUTOR;
  }
}
