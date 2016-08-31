package com.slamzoom.android.ui.create;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.slamzoom.android.R;
import com.slamzoom.android.billing.GetBuyIntentCallback;
import com.slamzoom.android.billing.GetPurchasedPacksCallback;
import com.slamzoom.android.billing.IabUtils;
import com.slamzoom.android.common.BackInterceptingEditText;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.FileType;
import com.slamzoom.android.common.utils.FileUtils;
import com.slamzoom.android.common.bus.BusProvider;
import com.slamzoom.android.common.utils.AnimationUtils;
import com.slamzoom.android.common.utils.BitmapUtils;
import com.slamzoom.android.common.utils.DebugUtils;
import com.slamzoom.android.common.utils.KeyboardUtils;
import com.slamzoom.android.common.SzAnalytics;
import com.slamzoom.android.common.SzLog;
import com.slamzoom.android.common.utils.PermissionUtils;
import com.slamzoom.android.effects.EffectPacks;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.EffectTemplates;
import com.slamzoom.android.mediacreation.MediaConfig;
import com.slamzoom.android.mediacreation.gif.GifCreator;
import com.slamzoom.android.mediacreation.gif.GifService;
import com.slamzoom.android.mediacreation.video.VideoCreator;
import com.slamzoom.android.mediacreation.video.VideoCreatorCallback;
import com.slamzoom.android.ui.create.effectchooser.EffectChooser;
import com.slamzoom.android.ui.create.effectchooser.EffectModel;
import com.slamzoom.android.ui.create.effectchooser.EffectThumbnailViewHolder;
import com.slamzoom.android.ui.create.hotspotchooser.HotspotChooserActivity;
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

  @Bind(R.id.coordinatatorLayout) CoordinatorLayout mCoordinatorLayout;
  @Bind(R.id.actionBar) Toolbar mActionBar;
  @Bind(R.id.gifImageView) GifImageView mGifImageView;
  @Bind(R.id.progressBar) ProgressBar mProgressBar;
  @Bind(R.id.zeroStateMessage) TextView mZeroStateMessage;
  @Bind(R.id.effectChooser) EffectChooser mEffectChooser;
  private AddTextView mAddTextView; // action bar custom view.

  private View mGifAreaView;
  private boolean mIsAddTextViewShowing = false;

  private Uri mSelectedUri;
  private byte[] mSelectedGifBytes;
  private Bitmap mSelectedBitmap;
  private Bitmap mSelectedBitmapForThumbnail;
  private Rect mSelectedHotspot;
  private Rect mSelectedHotspotForThumbnail;
  private String mSelectedEffectName;
  private String mSelectedEndText;
  private Map<String, Double> mGifProgresses;
  private boolean mGeneratingGif = false;

  private GifService mGifService;
  private GifServiceConnection mGifServiceConnection;
  private IInAppBillingService mBillingService;
  private BillingServiceConnection mBillingServiceConnection;

  private List<String> mPurchasedPackNames;
  private boolean mNeedsUpdatePurchasePackNames;
  private ImmutableList<EffectModel> mEffectModels;

  private ProgressDialog mShareProgressDialog;
  private FileType mSelectedFileType;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SzLog.f(TAG, "onCreate()");

    setContentView(R.layout.activity_create);
    ButterKnife.bind(this);
    BusProvider.getInstance().register(this);

    initServices();
    initEffects();
    initActionBar();
    initGifArea();

    handleIncomingIntent(getIntent());
  }

  @Override protected void onStart() {
    super.onStart();
    SzLog.f(TAG, "onStart()");
  }

  @Override protected void onResume() {
    super.onResume();
    SzLog.f(TAG, "onResume()");

    if (mSelectedEffectName == null) {
      mSelectedEffectName = EffectPacks.listEffectTemplatesByPack().get(0).getName();
    }
    if (mSelectedBitmap == null) {
      launchImageChooser();
    }
  }

  @Override protected void onNewIntent(Intent intent) {
    SzLog.f(TAG, "onNewIntent()");

    mGifService.clear();
    handleIncomingIntent(intent);
  }

  @Override
  public void onPause() {
    super.onPause();
    SzLog.f(TAG, "onPause()");
  }

  @Override
  public void onStop() {
    super.onStop();
    SzLog.f(TAG, "onStop()");
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    SzLog.f(TAG, "onDestroy()");

    ButterKnife.unbind(this);
    BusProvider.getInstance().unregister(this);

    if (mGifService != null) {
      unbindService(mGifServiceConnection);
    }
    if (mBillingService != null) {
      unbindService(mBillingServiceConnection);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == Constants.REQUEST_PICK_IMAGE) {
      if (resultCode == RESULT_OK) {
        handleIncomingUri(data.getData());
      } else if (mSelectedBitmap == null) {
        finish();
      }
    } else if (requestCode == Constants.REQUEST_CROP_IMAGE) {
      if (resultCode == RESULT_OK) {
        handleCropRectSelected(
            (Rect) data.getParcelableExtra(Constants.CROP_RECT),
            (Uri) data.getParcelableExtra(Constants.IMAGE_URI));
      } else if (mSelectedHotspot == null) {
        launchImageChooser();
      }
    } else if (requestCode == Constants.REQUEST_BUY_PACK) {
      if (resultCode == RESULT_OK) {
        updatePurchasedPackNamesAndEffectModels();
        shareCurrentEffect();
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull  int[] grantResults) {
    if (requestCode == Constants.REQUEST_SHARE_GIF_PERMISSIONS) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        shareCurrentEffect();
      } else {
        // TODO(clocksmith): show a message saying they must have these permissions to share.
      }
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
        onBackPressed();
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
      case R.id.action_share_gif:
        handleSharePressed(FileType.GIF);
        return true;
      case R.id.action_share_video:
        handleSharePressed(FileType.VIDEO);
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
      KeyboardUtils.hideKeyboard(this);
      showAddTextView(false);
    } else {
      super.onBackPressed();
    }
  }

  @Subscribe
  public void on(EffectThumbnailViewHolder.ItemClickEvent event) throws IOException {
    mSelectedEffectName = event.effectName;
    updateProgressBar();
    updateMainGif();
  }

  @Subscribe
  public void on(GifService.GifReadyEvent event) throws IOException {
    if (!event.thumbnail) {
      SzLog.f(TAG, event.effectName);
      if (mSelectedEffectName.equals(event.effectName)) {
        mZeroStateMessage.setVisibility(View.GONE);
        mSelectedGifBytes = event.gifBytes;
        mGeneratingGif = false;
        showCurrentGif();
      }
    }
  }

  @Subscribe
  public void on(GifService.GifGenerationStartEvent event) {
    mGeneratingGif = true;
    showProgressBar();
  }

  @Subscribe
  public void on(GifCreator.ProgressUpdateEvent event) throws IOException {
    mGifProgresses.put(event.effectName, mGifProgresses.get(event.effectName) + event.amountToUpdate);
    updateProgressBar();
  }

  @Subscribe
  public void on(BackInterceptingEditText.OnBackPressedEvent event) {
    onBackPressed();
  }

  @Subscribe
  public void on(final BuyToUnlockDialogFragment.OnBuyClickedEvent event) {
    IabUtils.getBuyIntentByPack(event.packName, mBillingService, new GetBuyIntentCallback() {
      @Override
      public void onSuccess(PendingIntent buyIntent) {
        try {
          startIntentSenderForResult(buyIntent.getIntentSender(), Constants.REQUEST_BUY_PACK, new Intent(), 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
          SzLog.e(TAG, "Could not start buy intent", e);
        }
      }

      @Override
      public void onFailure() {
        SzLog.e(TAG, "Could not get buy intent for pack: " + event.packName);
      }
    });
  }

  // TODO(clocksmith): The buy dialog should not call this, it should be passed in.
  public List<EffectModel> getEffectModelsForPack(final String packName) {
    return FluentIterable.from(mEffectModels).filter(new Predicate<EffectModel>() {
      @Override
      public boolean apply(EffectModel input) {
        return packName.equals(input.getEffectTemplate().getPackName());
      }
    }).toList();
  }

  private void initServices() {
    bindGifService();
    bindBillingService();
  }

  private void initActionBar() {
    setSupportActionBar(mActionBar);
    assert getSupportActionBar() != null;
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    getSupportActionBar().setTitle(getString(R.string.app_name));
    mAddTextView = new AddTextView(this);
    getSupportActionBar().setCustomView(mAddTextView,
        new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

  }

  private void initGifArea() {
    mGifProgresses = Maps.newHashMap();
    mGifAreaView = mZeroStateMessage;
  }

  private void initEffects() {
    mEffectModels = EffectPacks.listEffectModelsByPack();
    mEffectChooser.init(mEffectModels, true);
  }

  private void launchImageChooser() {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("image/*");
    startActivityForResult(Intent.createChooser(intent, "Select Image"), Constants.REQUEST_PICK_IMAGE);
  }

  private void launchHotspotChooser() {
    Intent intent = new Intent(CreateActivity.this, HotspotChooserActivity.class);
    intent.putExtra(Constants.IMAGE_URI, mSelectedUri);
    startActivityForResult(intent, Constants.REQUEST_CROP_IMAGE);
  }

  private void handleIncomingIntent(Intent intent) {
    if (Intent.ACTION_SEND.equals(intent.getAction())) {
      resetEffectModelsAndProgresses();
      handleIncomingUri((Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM));
    }
  }

  private void handleIncomingUri(Uri uri) {
    try {
      mSelectedUri = uri;
      getSelectedBitmapsFromUri();
      mSelectedEndText = "";
      mAddTextView.getEditText().setText("");
      launchHotspotChooser();
    } catch (FileNotFoundException e) {
      SzLog.e(TAG, "Cannot consume bitmap for path: " + uri.toString());
    }
  }

  private void handleCropRectSelected(Rect selectedHotspot, Uri uri) {
    // If our activity was destroyed while selection while selecting hotspot...
    if (mSelectedBitmap == null) {
      mSelectedUri = uri;
      try {
        getSelectedBitmapsFromUri();
      } catch (FileNotFoundException e) {
        Log.e(TAG, "Cannot read selected image", e);
        launchImageChooser();
      }
    }

    mSelectedHotspot = selectedHotspot;
    float ratio = (float) mSelectedBitmap.getWidth() / mSelectedBitmapForThumbnail.getWidth();
    mSelectedHotspotForThumbnail = new Rect(
        (int) (mSelectedHotspot.left / ratio),
        (int) (mSelectedHotspot.top / ratio),
        (int) (mSelectedHotspot.right / ratio),
        (int) (mSelectedHotspot.bottom / ratio));

    resetAndUpdateAll();
  }

  private void handleAddTextPressed() {
    mAddTextView.getEditText().requestFocus();
    KeyboardUtils.showKeyboard(this);
    showAddTextView(true);
  }

  private void handleAddTextConfirmed() {
    mSelectedEndText = mAddTextView.getEditText().getText().toString();
    resetAndUpdateAll();
    KeyboardUtils.hideKeyboard(this);
    showAddTextView(false);
  }

  private void handleSharePressed(FileType fileType) {
    mSelectedFileType = fileType;
    EffectModel effectModel = getEffectModelForSelectedEffect();
    if (effectModel == null) {
      // TODO(clocksmith): tell the user they must have an effect, or just disable share button.
      Snackbar snackbar = Snackbar.make(mCoordinatorLayout, "Choose an effect first!", Snackbar.LENGTH_SHORT);
      snackbar.show();
    } else if (mGeneratingGif) {
      Snackbar snackbar = Snackbar.make(
          mCoordinatorLayout, "Please wait till gif preview is finished!", Snackbar.LENGTH_SHORT);
      snackbar.show();
    } else if (!effectModel.isLocked()) {
      shareCurrentEffect();
    } else {
      String effectName = effectModel.getEffectTemplate().getName();
      String packName = effectModel.getEffectTemplate().getPackName();
      SzLog.f(TAG, "Showing dialog for effectName: " + effectName + " and packName: " + packName);
      showBuyDialog(effectName, packName  );
    }
  }

  private void updateMainGif() {
    mGifProgresses.put(mSelectedEffectName, 0d);
    mGifService.requestMainGif(getMainMediaConfig());
  }

  private void updateThumbnailGifs() {
    if (mPurchasedPackNames == null || mNeedsUpdatePurchasePackNames) {
      updatePurchasedPacksThenUpdateThumbnailGifs();
    } else {
      mGifService.requestThumbnailGifs(Lists.transform(EffectPacks.listEffectTemplatesByPack(),
          new Function<EffectTemplate, MediaConfig>() {
            @Override
            public MediaConfig apply(EffectTemplate effectTemplate) {
              return getThumbnailMediaConfig(effectTemplate);
            }
          }));
    }
  }

  private void resetAndUpdateAll() {
    resetEffectModelsAndProgresses();
    updateThumbnailGifs();

    mSelectedGifBytes = null;
    mGifImageView.setImageBitmap(null);
    if (mSelectedEffectName != null) {
      updateMainGif();
    }
  }

  private void resetEffectModelsAndProgresses() {
    for (EffectModel model : mEffectModels) {
      mGifProgresses.put(model.getEffectTemplate().getName(), 0d);
      model.setGifThumbnailBytes(null);
    }
    mEffectChooser.update(mEffectModels);
  }

  private void updatePurchasedPacksThenUpdateThumbnailGifs() {
    updatePurchasedPackNamesAndEffectModels(new Runnable() {
      @Override
      public void run() {
        updateThumbnailGifs();
      }
    });
  }

  private void updatePurchasedPackNamesAndEffectModels() {
    updatePurchasedPackNamesAndEffectModels(null);
  }

  private void updatePurchasedPackNamesAndEffectModels(final Runnable onDone) {
    IabUtils.getPurchasedPackNames(mBillingService, new GetPurchasedPacksCallback() {
      @Override
      public void onSuccess(List<String> packNames) {
        mPurchasedPackNames = packNames;
        mNeedsUpdatePurchasePackNames = false;
        updateEffectModels();
        if (onDone != null) {
          onDone.run();
        }
      }

      @Override
      public void onFailure() {
        if (onDone != null) {
          onDone.run();
        }
      }
    });
  }

  private void updateEffectModels() {
    for (EffectModel model : mEffectModels) {
      model.setLocked(!mPurchasedPackNames.contains(model.getEffectTemplate().getPackName()));
    }
    mEffectChooser.update(mEffectModels);
  }

  private void updateProgressBar() {
    mProgressBar.setVisibility(View.VISIBLE);
    if (mSelectedEffectName != null) {
      mProgressBar.setProgress((int) Math.round(100 * mGifProgresses.get(mSelectedEffectName)));
    }
  }

  private void showAddTextView(boolean show) {
    assert getSupportActionBar() != null;
    getSupportActionBar().setDisplayShowCustomEnabled(show);
    getSupportActionBar().setDisplayShowTitleEnabled(!show);
    mIsAddTextViewShowing = show;
    invalidateOptionsMenu();
  }

  private void showCurrentGif() {
    if (mGifAreaView != mProgressBar) {
      mProgressBar.setVisibility(View.GONE);
    } else {
      mGifImageView.setImageDrawable(null);
    }
    if (mGifAreaView != mZeroStateMessage) {
      mZeroStateMessage.setVisibility(View.GONE);
    } else {
      mGifImageView.setImageDrawable(null);
    }

    final Runnable scaleUp = new Runnable() {
      @Override
      public void run() {
        try {
          mProgressBar.setVisibility(View.GONE);
          mGifImageView.setImageDrawable(new GifDrawable(mSelectedGifBytes));
        } catch (IOException e) {
          SzLog.e(TAG, "Could not init gif", e);
        }
        AnimationUtils.getScaleUpSet(mGifImageView).start();
        mGifAreaView = mGifImageView;
      }
    };

    if (mGifAreaView != mZeroStateMessage) {
      final AnimatorSet scaleDown = AnimationUtils.getScaleDownSet(mGifAreaView);
      scaleDown.addListener(new AnimationUtils.OnAnimationEndOnlyListener() {
        @Override
        public void onAnimationEnd(Animator animation) {
          scaleUp.run();
        }
      });
      scaleDown.start();
    } else {
      scaleUp.run();
    }
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

  private void showBuyDialog(String effectName, String packName) {
    DialogFragment newFragment = BuyToUnlockDialogFragment.newInstance(effectName, packName);
    newFragment.show(getSupportFragmentManager(), BuyToUnlockDialogFragment.class.getSimpleName());
  }

  private void showShareProgressDialog(String message) {
    mShareProgressDialog = new ProgressDialog(this);
    mShareProgressDialog.setMessage(message);
    mShareProgressDialog.show();
  }

  private void dismissShareProgressDialog() {
    mShareProgressDialog.dismiss();
  }

  private void shareCurrentEffect() {
    if (mSelectedGifBytes != null) {
      if (mSelectedFileType == FileType.VIDEO) {
        shareVideoAsync();
      } else if (mSelectedFileType == FileType.GIF){
        shareGifAsync();
      }
    }
    // TODO(clocksmith): Put a message here telling the user they need to make a gif first.
  }

  private void shareGifAsync() {
    if (PermissionUtils.checkReadwriteExternalStorage(this)) {
      PermissionUtils.requestReadWriteExternalStorage(this);
    } else {
      showShareProgressDialog("Preparing gif...");
      new ShareGifTask().execute();
    }
  }

  private void shareVideoAsync() {
    if (PermissionUtils.checkReadwriteExternalStorage(this)) {
      PermissionUtils.requestReadWriteExternalStorage(this);
    } else {
      showShareProgressDialog("Preparing video...");
      mShareProgressDialog.show();
      new VideoCreator(this, getMainMediaConfig()).createAsync(new VideoCreatorCallback() {
        @Override
        public void onCreateVideo(File videoFile) {
          if (videoFile != null) {
            Intent baseIntent = new Intent(android.content.Intent.ACTION_SEND);
            baseIntent.setType(FileType.VIDEO.mime);
            Uri uri = Uri.fromFile(videoFile);
            baseIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(baseIntent, "Share via"));
          }
          // TODO(clocksmith): handle null
          dismissShareProgressDialog();
        }});
    }
  }

  private File saveCurrentGifToDisk() {
    File gifFile = FileUtils.createTimestampedFileWithId(FileType.GIF, mSelectedEffectName);
    if (mSelectedGifBytes != null) {
      try {
        // TODO(clocksmith): make sure external apps can't destroy this (read only)
        FileOutputStream gifOutputStream = new FileOutputStream(gifFile);
        gifOutputStream.write(mSelectedGifBytes);
        gifOutputStream.close();

        SzAnalytics.newGifSavedEvent()
            .withItemId(mSelectedEffectName)
            .withEndScale(mSelectedHotspot.width() / mSelectedBitmap.getWidth())
            .withEndTextLength(mSelectedEndText == null ? 0 : mSelectedEndText.length())
            .log(this);

        return gifFile;
      } catch (IOException e) {
        SzLog.e(TAG, "cannot save gif", e);
      }
    }

    return null;
  }

  private void getSelectedBitmapsFromUri() throws FileNotFoundException {
    mSelectedBitmap = BitmapUtils.readScaledBitmap(mSelectedUri, this.getContentResolver());
    if (DebugUtils.SAVE_SRC_AS_PNG) {
      BitmapUtils.saveBitmapToDiskAsPng(mSelectedBitmap, "src_0");
    }
    mSelectedBitmapForThumbnail = BitmapUtils.readScaledBitmap(
        mSelectedUri,
        this.getContentResolver(),
        Constants.MAX_DIMEN_FOR_MIN_SELECTED_DIMEN_PX / Constants.MEDIA_THUMBNAIL_DIVIDER);
  }

  private EffectModel getEffectModelForSelectedEffect() {
    for (EffectModel effectModel : mEffectModels) {
      if (effectModel.getEffectTemplate().getName().equals(mSelectedEffectName)) {
        return effectModel;
      }
    }
    return null;
  }

  private MediaConfig getMainMediaConfig() {
    return MediaConfig.newBuilder()
        .withHotspot(mSelectedHotspot)
        .withBitmap(mSelectedBitmap)
        .withEffectTemplate(EffectTemplates.get(mSelectedEffectName))
        .withEndText(mSelectedEndText)
        .withSize(Constants.MAIN_SIZE_PX)
        .withFps(Constants.MAIN_FPS)
        .build();
  }

  private MediaConfig getThumbnailMediaConfig(EffectTemplate effectTemplate) {
    return MediaConfig.newBuilder()
        .withHotspot(mSelectedHotspotForThumbnail)
        .withBitmap(mSelectedBitmapForThumbnail)
        .withEffectTemplate(effectTemplate)
        .withEndText(mSelectedEndText)
        .withSize(Constants.THUMBNAIL_SIZE_PX)
        .withFps(Constants.THUMBNAIL_FPS)
        .build();
  }

  private void bindGifService() {
    if (mGifServiceConnection == null) {
      mGifServiceConnection = new GifServiceConnection();
    }
    bindService(new Intent(this, GifService.class), mGifServiceConnection, Context.BIND_AUTO_CREATE);
  }

  private void bindBillingService() {
    if (mBillingServiceConnection == null) {
      mBillingServiceConnection = new BillingServiceConnection();
    }
    Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
    serviceIntent.setPackage("com.android.vending");
    bindService(serviceIntent, mBillingServiceConnection, Context.BIND_AUTO_CREATE);
  }

  private class GifServiceConnection implements ServiceConnection {
    @Override
    public void onServiceConnected(ComponentName className, IBinder iBinder) {
      mGifService = ((GifService.GifServiceBinder) iBinder).getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {
      mGifService = null;
    }
  }

  private class BillingServiceConnection implements ServiceConnection {
    @Override
    public void onServiceConnected(ComponentName name,
        IBinder service) {
      mBillingService = IInAppBillingService.Stub.asInterface(service);
      updatePurchasedPackNamesAndEffectModels();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      mBillingService = null;
    }
  }

  private class ShareGifTask extends AsyncTask<Void, Void, Boolean> {
    private Intent mBaseIntent;

    @Override
    protected Boolean doInBackground(Void... params) {
      File gifFile = saveCurrentGifToDisk();
      if (gifFile != null) {
        mBaseIntent = new Intent(android.content.Intent.ACTION_SEND);
        mBaseIntent.setType(FileType.GIF.mime);
        Uri uri = Uri.fromFile(gifFile);
        mBaseIntent.putExtra(Intent.EXTRA_STREAM, uri);
      } else {
        SzLog.e(TAG, "gif file is null");
        return false;
      }
      return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
      if (result) {
        startActivity(Intent.createChooser(mBaseIntent, "Share via"));
        dismissShareProgressDialog();
      }
    }
  }
}
