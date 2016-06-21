package com.slamzoom.android.ui.create;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.TextView;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.slamzoom.android.common.utils.AnimationUtils;
import com.slamzoom.android.common.utils.SzLog;
import com.slamzoom.android.effects.EffectModelProvider;
import com.slamzoom.android.common.BackInterceptingEditText;
import com.slamzoom.android.common.utils.BitmapUtils;
import com.slamzoom.android.common.singletons.BusProvider;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.R;
import com.slamzoom.android.common.utils.KeyboardUtils;
import com.slamzoom.android.mediacreation.gif.GifConfig;
import com.slamzoom.android.mediacreation.gif.GifCreator;
import com.slamzoom.android.mediacreation.gif.GifService;
import com.slamzoom.android.ui.create.effectchooser.EffectModel;
import com.slamzoom.android.ui.cropper.CropperActivity;
import com.slamzoom.android.ui.create.effectchooser.EffectChooser;
import com.slamzoom.android.ui.create.effectchooser.EffectThumbnailViewHolder;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class CreateActivity extends AppCompatActivity {
  private static final String TAG = CreateActivity.class.getSimpleName();

  private static final String SELECTED_EFFECT_NAME = "selectedEffectName";

  @Bind(R.id.actionBar) Toolbar mActionBar;
  @Bind(R.id.gifImageView) GifImageView mGifImageView;
  @Bind(R.id.progressBar) ProgressBar mProgressBar;
  @Bind(R.id.zeroStateMessage) TextView mZeroStateMessage;
  @Bind(R.id.effectChooser) EffectChooser mEffectChooser;
  private AddTextView mAddTextView;

  private View mGifAreaView;
  private boolean mIsAddTextViewShowing = false;

  private Uri mSelectedUri;
  private byte[] mSelectedGifBytes;

  private Bitmap mSelectedBitmap;
  private Bitmap mSelectedBitmapForPreview;
  private Rect mSelectedHotspot;
  private Rect mSelectedHotspotForPreview;

  private String mSelectedEffectName;
  private String mSelectedEndText;

  private GifService mGifService;
  private GifServiceConnection mConnection;
  private boolean mBound = false;
  private Map<String, Double> mGifProgresses;

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

    mProgressBar.setVisibility(View.GONE);
    mProgressBar.setScaleX(0);
    mProgressBar.setScaleY(0);
    mZeroStateMessage.setVisibility(View.VISIBLE);
    mGifAreaView = mZeroStateMessage;
    mGifProgresses = Maps.newHashMap();

    Intent intent = getIntent();
    if (Intent.ACTION_SEND.equals(intent.getAction())) {
      handleIncomingUri((Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM));
    }

    bindService(new Intent(this, GifService.class), mConnection, Context.BIND_AUTO_CREATE);

    if (mSelectedBitmap == null) {
      launchImageChooser();
    }
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
        float ratio = (float) mSelectedBitmap.getWidth() / mSelectedBitmapForPreview.getWidth();
        mSelectedHotspotForPreview = new Rect(
            (int) (mSelectedHotspot.left / ratio),
            (int) (mSelectedHotspot.top / ratio),
            (int) (mSelectedHotspot.right / ratio),
            (int) (mSelectedHotspot.bottom / ratio));

        mSelectedEndText = "";
        mSelectedGifBytes = null;
        mGifImageView.setImageBitmap(null);
        resetProgresses();
        updateGifPreviews();
        if (mSelectedEffectName != null) {
          updateGif();
        }
      } else if (mSelectedHotspot == null) {
        launchImageChooser();
      }
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
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
    mZeroStateMessage.setVisibility(View.GONE);
    updateProgressBar();
    updateGif();
  }

  @Subscribe
  public void on(GifService.GifReadyEvent event) throws IOException {
    if (!event.preview) {
      if (mSelectedEffectName.equals(event.effectName)) {
        mZeroStateMessage.setVisibility(View.GONE);
        mSelectedGifBytes = event.gifBytes;
        showCurrentGif();
      }
    }
  }

  @Subscribe
  public void on(GifService.GifGenerationSartEvent event) {
    showProgressBar();
  }

  @Subscribe
  public void on(GifCreator.ProgressUpdateEvent event) throws IOException {
    mGifProgresses.put(event.effectName, mGifProgresses.get(event.effectName) + event.amountToUpdate);
    updateProgressBar();
  }

  @Subscribe
  public void on(BackInterceptingEditText.OnBackPressedEvent event) {
    handleUpOrBackPressed();
  }

  private void resetProgresses() {
    for (EffectModel model : EffectModelProvider.getEffectModels()) {
      mGifProgresses.put(model.getEffectTemplate().getName(), 0d);
    }
  }

  private void updateProgressBar() {
    mProgressBar.setVisibility(View.VISIBLE);
    if (mSelectedEffectName != null) {
      mProgressBar.setProgress((int) Math.round(100 * mGifProgresses.get(mSelectedEffectName)));
    }
  }

  private void updateGif() {
    mGifProgresses.put(mSelectedEffectName, 0d);
    mGifService.requestGif(GifConfig.newBuilder()
        .withHotspot(mSelectedHotspot)
        .withBitmap(mSelectedBitmap)
        .withEffectModel(EffectModelProvider.getEffectModel(mSelectedEffectName))
        .withEndText(mSelectedEndText)
        .build());
  }

  private void updateGifPreviews() {
    List<GifConfig> configs = Lists.transform(EffectModelProvider.getEffectModels(),
        new Function<EffectModel, GifConfig>() {
          @Override
          public GifConfig apply(EffectModel model) {
            return GifConfig.newBuilder()
                .withHotspot(mSelectedHotspotForPreview)
                .withBitmap(mSelectedBitmapForPreview)
                .withEffectModel(model)
                .withEndText(mSelectedEndText)
                .build();
          }
        });

    mEffectChooser.set(EffectModelProvider.getEffectModels());
    mGifService.resetWithConfigs(configs);
  }

  private void  launchImageChooser() {
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
      mSelectedBitmap = BitmapUtils.readScaledBitmap(mSelectedUri, this.getContentResolver());
      mSelectedBitmapForPreview = BitmapUtils.readScaledBitmap(
          mSelectedUri,
          this.getContentResolver(),
          Constants.MAX_DIMEN_FOR_MIN_SELECTED_DIMEN_PX / Constants.GIF_PREVIEW_DIVIDER);
      launchHotspotChooser();
    } catch (FileNotFoundException e) {
      SzLog.e(TAG, "Cannot get bitmap for path: " + uri.toString());
    }
  }

  private void handleUpOrBackPressed() {
    KeyboardUtils.hideKeyboard(this);
    showAddTextView(false);
  }

  private void handleAddTextPressed() {
//    if (mSelectedEndText != null) {
//      mAddTextView.getEditText().setText(mSelectedEndText);
//    }
    mAddTextView.getEditText().requestFocus();
    KeyboardUtils.showKeyboard(this);
    showAddTextView(true);
  }

  private void handleAddTextConfirmed() {
    mSelectedEndText = mAddTextView.getEditText().getText().toString();
    mSelectedGifBytes = null;
    mGifImageView.setImageBitmap(null);
    resetProgresses();
    updateGifPreviews();
    if (mSelectedEffectName != null) {
      updateGif();
    }


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

  private void showCurrentGif() {
    if (mGifAreaView != mProgressBar) {
      mProgressBar.setVisibility(View.GONE);
    }
    AnimatorSet scaleDown = AnimationUtils.getScaleDownSet(mGifAreaView);
    scaleDown.addListener(new AnimationUtils.OnAnimationEndOnlyListener() {
      @Override
      public void onAnimationEnd(Animator animation) {
        try {
          mProgressBar.setVisibility(View.GONE);
          mGifImageView.setImageDrawable(new GifDrawable(mSelectedGifBytes));
        } catch (IOException e) {
          SzLog.e(TAG, "Could not set gif", e);
        }
        AnimationUtils.getScaleUpSet(mGifImageView).start();
      }
    });
    scaleDown.start();
    mGifAreaView = mGifImageView;
  }

  private void showProgressBar() {
    AnimatorSet scaleDown = AnimationUtils.getScaleDownSet(mGifAreaView);
    scaleDown.addListener(new AnimationUtils.OnAnimationEndOnlyListener() {
      @Override
      public void onAnimationEnd(Animator animation) {
        mGifImageView.setImageBitmap(null);
        mProgressBar.setVisibility(View.VISIBLE);
        AnimationUtils.getScaleUpSet(mProgressBar).start();
      }
    });
    scaleDown.start();
    mGifAreaView = mProgressBar;
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
      Log.d(TAG, "No directory exitsts: " + gifFile.getParentFile());
      if (!gifFile.getParentFile().mkdirs()) {
        SzLog.e(TAG, "Cannot make directory: " + gifFile.getParentFile());
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
        SzLog.e(TAG, "cannot save gif", e);
      }
    }

    return null;
  }

  private void shareCurrentGif() {
    if (mSelectedGifBytes != null) {
      new ShareGifTask().execute();
    }
    // TODO(clocksmith): Put a message here telling the user they need to make a gif first.
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

  private class ShareGifTask extends AsyncTask<Void, Void, Boolean> {
    private Intent mShareIntent;

    @Override
    protected Boolean doInBackground(Void... params) {
      mShareIntent = new Intent(android.content.Intent.ACTION_SEND);
      mShareIntent.setType("image/gif");

      File gifFile = addCurrentGifToLibrary();
      if (gifFile != null) {
        mShareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(gifFile));
      } else {
        SzLog.e(TAG, "gif file is null");
        return false;
      }
      return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
      if (result) {
        startActivity(Intent.createChooser(mShareIntent, "Share via"));
      }
    }
  }
}
