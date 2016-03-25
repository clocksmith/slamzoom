package com.slamzoom.android.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.slamzoom.android.common.utils.BitmapUtils;
import com.slamzoom.android.common.providers.BusProvider;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.R;
import com.slamzoom.android.ui.cropper.CropperActivity;
import com.slamzoom.android.ui.effect.EffectChooser;
import com.slamzoom.android.ui.effect.EffectModel;
import com.slamzoom.android.ui.effect.EffectModelsFactory;
import com.slamzoom.android.ui.effect.EffectViewHolder;
import com.slamzoom.android.media.gif.GifService;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {
  private static final String TAG = MainActivity.class.getSimpleName();

  @Bind(R.id.gifImageView) GifImageView mGifImageView;
  @Bind(R.id.progressBar) ProgressBar mProgressBar;
  @Bind(R.id.effectChooser) EffectChooser mEffectChooser;

  private String mSelectedEffectName;
  private byte[] mSelectedGifBytes;
  private Bitmap mSelectedBitmap;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    BusProvider.getInstance().register(this);

    GifService.getInstance().setContext(this);
    List<EffectModel> templates = EffectModelsFactory.getTemplates();
    GifService.getInstance().setEffectModels(templates);
    mEffectChooser.setEffectModels(templates);
    mSelectedEffectName = templates.get(0).getName();

    mGifImageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), Constants.REQUEST_PICK_IMAGE);
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_main, menu);
    return true;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
      case R.id.action_share:
        shareCurrentGif();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == Constants.REQUEST_PICK_IMAGE) {
      if (resultCode == RESULT_OK) {
        Uri uri = data.getData();
        try {
          mSelectedBitmap = BitmapUtils.readScaledBitmap(
              uri, this.getContentResolver(), Constants.MAX_SELECTED_DIMEN_PX);
          GifService.getInstance().setSelectedBitmap(mSelectedBitmap);
          Intent intent = new Intent(MainActivity.this, CropperActivity.class);
          intent.putExtra(Constants.IMAGE_URI, uri);
          startActivityForResult(intent, Constants.REQUEST_CROP_IMAGE);
        } catch (FileNotFoundException e) {
          Log.e(TAG, "Cannot get bitmap for path: " + uri.toString());
        }
      }
    } else if (requestCode == Constants.REQUEST_CROP_IMAGE) {
      if (resultCode == RESULT_OK) {
        Rect cropRect = data.getParcelableExtra(Constants.CROP_RECT);
        GifService.getInstance().setCropRect(cropRect, mSelectedEffectName);
        mEffectChooser.clearGifsAndShowSpinners();
        mSelectedGifBytes = null;
        mGifImageView.setImageBitmap(null);
      }
    }
  }

  @Subscribe
  public void on(EffectViewHolder.ItemClickEvent event) throws IOException {
    mSelectedEffectName = event.effectName;
    mProgressBar.setProgress(0);
    mProgressBar.setVisibility(View.VISIBLE);
    mGifImageView.setImageBitmap(null);
    GifService.getInstance().updateGifIfPossible(event.effectName);
  }

  @Subscribe
  public void on(GifService.GifPreviewReadyEvent event) throws IOException {
    mEffectChooser.setGifPreview(event.effectName, event.gifBytes);
  }

  @Subscribe
  public void on(GifService.GifReadyEvent event) throws IOException {
    if (mSelectedEffectName.equals(event.effectName)) {
      mSelectedGifBytes = event.gifBytes;
      mProgressBar.setVisibility(View.GONE);
      mGifImageView.setImageDrawable(new GifDrawable(mSelectedGifBytes));
    }
  }

  @Subscribe
  public void on(GifService.ProgressUpdateEvent event) throws IOException {
    if (event.effectName.equals(mSelectedEffectName)) {
      mProgressBar.setProgress(event.progress);
    }
  }

  private void shareCurrentGif() {
    if (ContextCompat.checkSelfPermission(
        this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

      ActivityCompat.requestPermissions(this,
          new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
          0);
    }

    File direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "/SlamZoom");

    long now = System.currentTimeMillis();
    String gifFilename = "slamzoom_" + now + ".gif";
    File gifFile = new File(direct, gifFilename);
    if (!gifFile.getParentFile().mkdirs()) {
      Log.e(TAG, "Cannot make directory: " + gifFile.getPath());
    }

    try {
      // TODO(clocksmith): make sure external apps can't destory this (read only)
      FileOutputStream gifOutputStream = new FileOutputStream(gifFile);
      gifOutputStream.write(mSelectedGifBytes);
      gifOutputStream.close();

      Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
      shareIntent.setType("image/gif");
      shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(gifFile));
      startActivity(Intent.createChooser(shareIntent, "Share via"));
    } catch (IOException e) {
      Log.e(TAG, "cannot share gif", e);
    }
  }
}
