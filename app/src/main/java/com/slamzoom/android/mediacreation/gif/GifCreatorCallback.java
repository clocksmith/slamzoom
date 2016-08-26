package com.slamzoom.android.mediacreation.gif;

import com.slamzoom.android.mediacreation.MediaCreatorCallback;

import java.io.File;

/**
 * Created by clocksmith on 8/25/16.
 */
public abstract class GifCreatorCallback implements MediaCreatorCallback {
  @Override
  public void onCreateVideo(File videoFile) {
    // no op
  }
}
