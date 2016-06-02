  package com.slamzoom.android.ui.create;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
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

import com.slamzoom.android.effects.EffectModelProvider;
import com.slamzoom.android.global.BackInterceptingEditText;
import com.slamzoom.android.global.utils.BitmapUtils;
import com.slamzoom.android.global.singletons.BusProvider;
import com.slamzoom.android.global.Constants;
import com.slamzoom.android.R;
import com.slamzoom.android.global.utils.KeyboardUtils;
import com.slamzoom.android.mediacreation.gif.GifConfig;
import com.slamzoom.android.mediacreation.gif.GifService;
import com.slamzoom.android.ui.cropper.CropperActivity;
import com.slamzoom.android.ui.create.effectchooser.EffectChooser;
import com.slamzoom.android.ui.create.effectchooser.EffectThumbnailViewHolder;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class CreateActivity extends AppCompatActivity {
  private static final String TAG = CreateActivity.class.getSimpleName();

  @Bind(R.id.actionBar) Toolbar mActionBar;
  @Bind(R.id.gifImageView) GifImageView mGifImageView;
  @Bind(R.id.progressBar) ProgressBar mProgressBar;
  @Bind(R.id.effectChooser) EffectChooser mEffectChooser;
  private AddTextView mAddTextView;

  private boolean mIsAddTextViewShowing = false;

  private Rect mSelectedHotspot;
  private Bitmap mSelectedBitmap;
  private String mSelectedEffectName;
  private String mSelectedEndText;

  private Uri mSelectedUri;
  private byte[] mSelectedGifBytes;

  private GifService mGifService;
  private GifServiceConnection mConnection;
  private boolean mBound = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create);
    ButterKnife.bind(this);
    BusProvider.getInstance().register(this);
    mConnection = new GifServiceConnection();

    setSupportActionBar(mActionBar);
    assert getSupportActionBar() != null;
    getSupportActionBar().setTitle(getString(R.string.app_name));
    mAddTextView = new AddTextView(this);
    getSupportActionBar().setCustomView(mAddTextView,
        new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    mEffectChooser.setEffectModels(EffectModelProvider.getEffectModels());
    mSelectedEffectName = EffectModelProvider.getEffectModels().get(0).getEffectTemplate().getName();
  }

  @Override
  public void onStart() {
    super.onStart();
    bindService(new Intent(this, GifService.class), mConnection, Context.BIND_AUTO_CREATE);

    if (mSelectedBitmap == null) {
      launchImageChooser();
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (mBound) {
      unbindService(mConnection);
      mBound = false;
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_create, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        handleUpOrBackPressed();
        return true;
      case R.id.action_add_text:
        handleAddTextPressed();
        return true;
      case R.id.action_change_hotspot:
        launchHotspotChooser();
        return true;
      case R.id.action_change_image:
        launchImageChooser();
        return true;
      case R.id.action_ok:
        handleAddTextConfirmed();
        return true;
      case R.id.action_add_to_library:
        addCurrentGifToLibrary();
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
    menu.findItem(R.id.action_change_hotspot).setVisible(!mIsAddTextViewShowing);
    menu.findItem(R.id.action_change_image).setVisible(!mIsAddTextViewShowing);
    menu.findItem(R.id.action_share).setVisible(!mIsAddTextViewShowing);
    menu.findItem(R.id.action_ok).setVisible(mIsAddTextViewShowing);
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == Constants.REQUEST_PICK_IMAGE) {
      if (resultCode == RESULT_OK) {
        handleIncomingUri(data.getData());
      } else {
        if (mSelectedBitmap == null) {
          finish();
        }
      }
    } else if (requestCode == Constants.REQUEST_CROP_IMAGE) {
      if (resultCode == RESULT_OK) {
        mSelectedHotspot = data.getParcelableExtra(Constants.CROP_RECT);
        mSelectedEndText = null;
        mSelectedGifBytes = null;
        mGifImageView.setImageBitmap(null);
        mProgressBar.setProgress(0);
        mProgressBar.setVisibility(View.VISIBLE);
        updateGif();
      } else {
        if (mSelectedHotspot == null) {
          finish();
        }
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
    updateGif();
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

  private void updateGif() {
    mGifService.update(GifConfig.newBuilder()
        .withHotspot(mSelectedHotspot)
        .withBitmap(mSelectedBitmap)
        .withEffectModel(EffectModelProvider.getEffectModel(mSelectedEffectName))
        .withEndText(mSelectedEndText)
        .build());
  }

  private void launchImageChooser() {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("image/*");
    startActivityForResult(Intent.createChooser(intent, "Select Image"), Constants.REQUEST_PICK_IMAGE);
  }

  private void launchHotspotChooser() {
    Intent intent = new Intent(CreateActivity.this, CropperActivity.class);
    intent.putExtra(Constants.IMAGE_URI, mSelectedUri);
    startActivityForResult(intent, Constants.REQUEST_CROP_IMAGE);
  }

  private void handleIncomingUri(Uri uri) {
    try {
      mSelectedUri = uri;
      mSelectedBitmap = BitmapUtils.readScaledBitmap(
          mSelectedUri, this.getContentResolver(), Constants.MAX_SELECTED_DIMEN_PX);
      launchHotspotChooser();
    } catch (FileNotFoundException e) {
      Log.e(TAG, "Cannot get bitmap for path: " + uri.toString());
    }
  }

  private void handleUpOrBackPressed() {
    KeyboardUtils.hideKeyboard(this);
    showAddTextView(false);
  }

  private void handleAddTextPressed() {
    mAddTextView.getEditText().setText(mSelectedEndText == null ? "" : mSelectedEndText);
    mAddTextView.getEditText().requestFocus();
    KeyboardUtils.showKeyboard(this);
    showAddTextView(true);
  }

  private void handleAddTextConfirmed() {
    mSelectedEndText = mAddTextView.getEditText().getText().toString();
    mProgressBar.setProgress(0);
    mProgressBar.setVisibility(View.VISIBLE);
    mGifImageView.setImageBitmap(null);
    updateGif();
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

  private File addCurrentGifToLibrary() {
    if (ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions
          (this,
              new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
              0);
    }

    File direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "/SlamZoom");

    long now = System.currentTimeMillis();
    String gifFilename = "slamzoom_" + now + ".gif";
    File gifFile = new File(direct, gifFilename);

    if (!gifFile.getParentFile().isDirectory()) {
      Log.e(TAG, "No directory exitsts: " + gifFile.getParentFile());
      if (!gifFile.getParentFile().mkdirs()) {
        Log.e(TAG, "Cannot make directory: " + gifFile.getParentFile());
      } {
        Log.d(TAG, direct + " successfully created." );
      }
    } else {
      Log.d(TAG, direct + " already exists." );
    }

    if (mSelectedGifBytes != null) {
      try {
        // TODO(clocksmith): make sure external apps can't destroy this (read only)
        FileOutputStream gifOutputStream = new FileOutputStream(gifFile);
        gifOutputStream.write(mSelectedGifBytes);
        gifOutputStream.close();
        return gifFile;
      } catch (IOException e) {
        Log.e(TAG, "cannot save gif", e);
      }
    }

    return null;
  }

  private void shareCurrentGif() {
    if (mSelectedGifBytes != null) {
      File gifFile = addCurrentGifToLibrary();
      if (gifFile != null) {

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("image/gif");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(gifFile));
        startActivity(Intent.createChooser(shareIntent, "Share via"));
      }
    }
  }

  private class GifServiceConnection implements ServiceConnection {
    @Override
    public void onServiceConnected(ComponentName className, IBinder iBinder) {
      GifService.GifServiceBinder binder = (GifService.GifServiceBinder) iBinder;
      mGifService = binder.getService();
      mBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {
      mBound = false;
    }
  }
}
