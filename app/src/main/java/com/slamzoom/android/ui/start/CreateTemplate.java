package com.slamzoom.android.ui.start;

import android.graphics.RectF;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

/**
 * Created by clocksmith on 9/17/16.
 */
public class CreateTemplate implements Parcelable {
  public final Uri uri;
  public final RectF hotspot;

  public CreateTemplate(Uri uri, RectF hotspot) {
    this.uri = uri;
    this.hotspot = hotspot;
  }

  protected CreateTemplate(Parcel in) {
    uri = in.readParcelable(Uri.class.getClassLoader());
    hotspot = in.readParcelable(RectF.class.getClassLoader());
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(uri, flags);
    dest.writeParcelable(hotspot, flags);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<CreateTemplate> CREATOR = new Creator<CreateTemplate>() {
    @Override
    public CreateTemplate createFromParcel(Parcel in) {
      return new CreateTemplate(in);
    }

    @Override
    public CreateTemplate[] newArray(int size) {
      return new CreateTemplate[size];
    }
  };

}
