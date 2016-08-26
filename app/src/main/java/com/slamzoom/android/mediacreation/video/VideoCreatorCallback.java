package com.slamzoom.android.mediacreation.video;

import com.slamzoom.android.mediacreation.MediaCreatorCallback;

/**
 * Created by clocksmith on 8/25/16.
 */
public abstract class VideoCreatorCallback implements MediaCreatorCallback {
  @Override
  public void onCreateGif(byte[] gifBytes) {
    // no op
  }
}
