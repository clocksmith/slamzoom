package com.slamzoom.android.media;

/**
 * Created by clocksmith on 3/25/16.
 */
public interface MediaEncoder<F extends MediaFrame, C extends CreateMediaCallback> {
  void addFrames(Iterable<F> frame);

  void encodeAsync(C callback);
}
