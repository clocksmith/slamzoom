package com.slamzoom.android.common;

/**
 * Created by clocksmith on 8/27/16.
 */
public enum FileType {
  GIF("image/gif", "gif"),
  VIDEO("video/mp4", "mp4"),
  PNG("image/png", "png"),
  JPEG("image/jpeg", "jpg"),
  BMP("image/bmp", "bmp");

  public String mime;
  public String ext;

  FileType(String mime, String ext) {
    this.mime = mime;
    this.ext = ext;
  }
}
