package com.slamzoom.android.mediacreation;

import android.graphics.PointF;

/**
 * Created by clocksmith on 3/1/16.
 */
public class MediaConstants {
  // TODO(clocskmith): Refactor these into utils or classes they are used in.
  public static final int MAIN_FPS = 24;
  public static final int THUMBNAIL_FPS = 16;
  public static final int MAIN_SIZE_PX = 320; // must be even
  public static final int THUMBNAIL_SIZE_PX = 120; // must be even
  public static final boolean DEFAULT_USE_LOCAL_COLOR_PALETTE = true;
  // TODO(clocksmith): Figure out this value
  public static final int VIDEO_KBPS = 256;
  public static final int MAX_DIMEN_FOR_MIN_SELECTED_DIMEN_PX = MAIN_SIZE_PX * 4; // arbitrary
}