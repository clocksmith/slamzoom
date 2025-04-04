package com.slamzoom.android.mediacreation;

import java.io.File;

/**
 * Created by clocksmith on 3/25/16.
 */
public interface MediaCreatorCallback {
  void onCreateVideo(File videoFile);
  void onCreateGif(byte[] gifBytes);
}
