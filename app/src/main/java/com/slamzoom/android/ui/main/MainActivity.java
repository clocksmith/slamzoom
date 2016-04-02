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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.slamzoom.android.global.BackInterceptingEditText;
import com.slamzoom.android.global.utils.BitmapUtils;
import com.slamzoom.android.global.singletons.BusProvider;
import com.slamzoom.android.global.Constants;
import com.slamzoom.android.R;
import com.slamzoom.android.global.utils.KeyboardUtils;
import com.slamzoom.android.ui.cropper.CropperActivity;
import com.slamzoom.android.ui.main.effectchooser.EffectChooser;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.EffectTemplateProvider;
import com.slamzoom.android.ui.main.effectchooser.EffectModel;
import com.slamzoom.android.ui.main.effectchooser.EffectThumbnailViewHolder;
import com.slamzoom.android.mediacreation.gif.GifService;
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

  @Bind(R.id.actionBar) Toolbar mActionBar;
  @Bind(R.id.gifImageView) GifImageView mGifImageView;
  @Bind(R.id.progressBar) ProgressBar mProgressBar;
  @Bind(R.id.effectChooser) EffectChooser mEffectChooser;
  private AddTextView mAddTextView;

  private String mSelectedEffectName;
  private byte[] mSelectedGifBytes;
  private Bitmap mSelectedBitmap;

  private boolean mIsAddTextViewShowing = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    BusProvider.getInstance().register(this);
    GifService.getInstance().setContext(this);

    setSupportActionBar(mActionBar);
    assert getSupportActionBar() != null;
    mAddTextView = new AddTextView(this);
    getSupportActionBar().setCustomView(mAddTextView,
        new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    setEffectModels();

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
    return super.onCreateOptionsMenu(menu);
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
      case android.R.id.home:
        handleUpOrBackPressed();
        return true;
      case R.id.action_add_text:
        handleAddTextPressed();
        return true;
      case R.id.action_ok:
        // TODO(clocksmith): something.
        handleAddTextConfirmed();
        return true;
      case R.id.action_share:
        shareCurrentGif();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    menu.findItem(R.id.action_add_text).setVisible(!mIsAddTextViewShowing);
    menu.findItem(R.id.action_share).setVisible(!mIsAddTextViewShowing);
    menu.findItem(R.id.action_ok).setVisible(mIsAddTextViewShowing);
    return super.onPrepareOptionsMenu(menu);
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
        // TODO(clocksmith): move this
        if (mSelectedBitmap != null) {
          mEffectChooser.clearGifsAndShowSpinners();
        }

        Rect cropRect = data.getParcelableExtra(Constants.CROP_RECT);
        setEffectModels();
        GifService.getInstance().setHotspot(cropRect);
        mSelectedGifBytes = null;
        mGifImageView.setImageBitmap(null);
      }
    }
  }

  @Override
  public void onBackPressed() {
    if (mIsAddTextViewShowing) {
      handleUpOrBackPressed();
    } else {
      super.onBackPressed();
    }
  }

  @Subscribe
  public void on(EffectThumbnailViewHolder.ItemClickEvent event) throws IOException {
    mSelectedEffectName = event.effectName;
    mProgressBar.setProgress(0);
    mProgressBar.setVisibility(View.VISIBLE);
    mGifImageView.setImageBitmap(null);
    GifService.getInstance().updateGifIfPossible(event.effectName);
  }

  @Subscribe
  public void on(GifService.GifPreviewReadyEvent event) {
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
  public void on(GifService.ProgressUpdateEvent event) {
    if (event.effectName.equals(mSelectedEffectName)) {
      mProgressBar.setProgress(event.progress);
    }
  }

  @Subscribe
  public void on(BackInterceptingEditText.OnBackPressedEvent event) {
    handleUpOrBackPressed();
  }

  private void setEffectModels() {
    List<EffectModel> models = Lists.newArrayList(Lists.transform(EffectTemplateProvider.getTemplates(),
        new Function<EffectTemplate, EffectModel>() {
          @Override
          public EffectModel apply(EffectTemplate input) {
            return new EffectModel(input);
          }
        }));
    GifService.getInstance().setEffectModels(models);
    mEffectChooser.setEffectModels(models);
    mSelectedEffectName = models.get(0).getEffectTemplate().getName();
  }

  private void handleUpOrBackPressed() {
    KeyboardUtils.hideKeyboard(this);
    showAddTextView(false);
  }

  private void handleAddTextPressed() {
    mAddTextView.getEditText().setText("");
    mAddTextView.getEditText().requestFocus();
    KeyboardUtils.showKeyboard(this);
    showAddTextView(true);
  }

  private void handleAddTextConfirmed() {
    if (mSelectedBitmap != null) {
      mEffectChooser.clearGifsAndShowSpinners();
    }

    GifService.getInstance().setEndText(mAddTextView.getEditText().getText().toString());
    KeyboardUtils.hideKeyboard(this);
    showAddTextView(false);
  }

  private void showAddTextView(boolean show) {
    assert getSupportActionBar() != null;
    getSupportActionBar().setDisplayHomeAsUpEnabled(show);
    getSupportActionBar().setHomeButtonEnabled(show);
    getSupportActionBar().setDisplayShowCustomEnabled(show);
    getSupportActionBar().setDisplayShowTitleEnabled(!show);
    mIsAddTextViewShowing = show;
    invalidateOptionsMenu();
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

    if (!gifFile.getParentFile().isDirectory()) {
      if (!gifFile.getParentFile().mkdirs()) {
        Log.e(TAG, "Cannot make directory: " + gifFile.getParentFile());
      }
    }

    if (mSelectedGifBytes != null) {
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
}
