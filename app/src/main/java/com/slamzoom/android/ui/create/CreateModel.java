package com.slamzoom.android.ui.create;

import android.graphics.RectF;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.slamzoom.android.SzApp;
import com.slamzoom.android.common.bitmaps.BitmapSet;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.Effects;
import com.slamzoom.android.mediacreation.MediaConstants;

import java.util.List;
import java.util.Map;

/**
 * Created by antrob on 9/23/16.
 */

public class CreateModel implements Parcelable {
  private Uri mSelectedUri;
  private RectF mSelectedHotspot;
  private String mSelectedEffectName = Effects.listEffectTemplates().get(0).getName();
  private String mSelectedEndText;

  private boolean mGeneratingGif = false;
  private List<String> mPurchasedPackNames = Lists.newArrayList();
  private boolean mNeedsUpdatePurchasePackNames;
  private boolean mAddTextViewShowing = false;

  private Map<String, Double> mGifProgresses;
  private BitmapSet mSelectedBitmapSet;

  public BitmapSet getSelectedBitmapSet() {
    return mSelectedBitmapSet;
  }

  public void setSelectedBitmapSet(BitmapSet selectedBitmapSet) {
    mSelectedBitmapSet = selectedBitmapSet;
  }

  public Uri getSelectedUri() {
    return mSelectedUri;
  }

  public void setSelectedUri(Uri mSelectedUri) {
    this.mSelectedUri = mSelectedUri;
  }

  public RectF getSelectedHotspot() {
    return mSelectedHotspot;
  }

  public void setSelectedHotspot(RectF mSelectedHotspot) {
    this.mSelectedHotspot = mSelectedHotspot;
  }

  public String getSelectedEffectName() {
    if (mSelectedEffectName == null) {
      mSelectedEffectName = Effects.listEffectTemplates().get(0).getName();
    }
    return mSelectedEffectName;
  }

  public void setSelectedEffectName(String mSelectedEffectName) {
    this.mSelectedEffectName = mSelectedEffectName;
  }

  public String getSelectedEndText() {
    return mSelectedEndText;
  }

  public int getSelectedEndTextLength() {
    return mSelectedEndText == null ? 0 : mSelectedEndText.length();
  }

  public void setSelectedEndText(String mSelectedEndText) {
    this.mSelectedEndText = mSelectedEndText;
  }

  public boolean isGeneratingGif() {
    return mGeneratingGif;
  }

  public void setGeneratingGif(boolean mGeneratingGif) {
    this.mGeneratingGif = mGeneratingGif;
  }

  public List<String> getPurchasedPackNames() {
    return mPurchasedPackNames;
  }

  public void setPurchasedPackNames(List<String> mPurchasedPackNames) {
    mNeedsUpdatePurchasePackNames = false;
    this.mPurchasedPackNames = mPurchasedPackNames;
  }

  public boolean needsUpdatePurchasePackNames() {
    return getPurchasedPackNames() == null || mNeedsUpdatePurchasePackNames;
  }

  public void setNeedsUpdatePurchasePackNames(boolean mNeedsUpdatePurchasePackNames) {
    this.mNeedsUpdatePurchasePackNames = mNeedsUpdatePurchasePackNames;
  }

  public boolean isAddTextViewShowing() {
    return mAddTextViewShowing;
  }

  public void setAddTextViewShowing(boolean mAddTextViewShowing) {
    this.mAddTextViewShowing = mAddTextViewShowing;
  }

  public Map<String, Double> getGifProgresses() {
    if (mGifProgresses == null) {
      mGifProgresses = Maps.newHashMap();
      for (EffectTemplate template : Effects.listEffectTemplates()) {
        mGifProgresses.put(template.getName(), 0d);
      }
    }
    return mGifProgresses;
  }

  public void setGifProgresses(Map<String, Double> mGifProgresses) {
    this.mGifProgresses = mGifProgresses;
  }

  public CreateModel() {}

  protected CreateModel(Parcel in) {
    mSelectedUri = in.readParcelable(Uri.class.getClassLoader());
    mSelectedHotspot = in.readParcelable(RectF.class.getClassLoader());
    mSelectedEffectName = in.readString();
    mSelectedEndText = in.readString();
    mGeneratingGif = in.readByte() != 0;
    mPurchasedPackNames = in.createStringArrayList();
    mNeedsUpdatePurchasePackNames = in.readByte() != 0;
    mAddTextViewShowing = in.readByte() != 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(mSelectedUri, flags);
    dest.writeParcelable(mSelectedHotspot, flags);
    dest.writeString(mSelectedEffectName);
    dest.writeString(mSelectedEndText);
    dest.writeByte((byte) (mGeneratingGif ? 1 : 0));
    dest.writeStringList(mPurchasedPackNames);
    dest.writeByte((byte) (mNeedsUpdatePurchasePackNames ? 1 : 0));
    dest.writeByte((byte) (mAddTextViewShowing ? 1 : 0));
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<CreateModel> CREATOR = new Creator<CreateModel>() {
    @Override
    public CreateModel createFromParcel(Parcel in) {
      return new CreateModel(in);
    }

    @Override
    public CreateModel[] newArray(int size) {
      return new CreateModel[size];
    }
  };
}
