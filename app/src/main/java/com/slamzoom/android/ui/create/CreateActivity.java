package com.slamzoom.android.ui.create;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.slamzoom.android.R;
import com.slamzoom.android.billing.GetBuyIntentCallback;
import com.slamzoom.android.billing.GetPurchasedPacksCallback;
import com.slamzoom.android.billing.IabUtils;
import com.slamzoom.android.common.BackInterceptingEditText;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.FileType;
import com.slamzoom.android.common.LifecycleLoggingActivity;
import com.slamzoom.android.common.utils.FileUtils;
import com.slamzoom.android.common.bus.BusProvider;
import com.slamzoom.android.common.utils.AnimationUtils;
import com.slamzoom.android.common.utils.BitmapUtils;
import com.slamzoom.android.common.utils.DebugUtils;
import com.slamzoom.android.common.utils.KeyboardUtils;
import com.slamzoom.android.common.SzAnalytics;
import com.slamzoom.android.common.SzLog;
import com.slamzoom.android.common.utils.MathUtils;
import com.slamzoom.android.common.utils.PermissionUtils;
import com.slamzoom.android.effects.Effects;
import com.slamzoom.android.effects.EffectTemplate;
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
import java.util.Queue;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

// TODO(clocksmith): make this class smaller. extract services, etc.
public class CreateActivity extends LifecycleLoggingActivity {
  private static final String TAG = CreateActivity.class.getSimpleName();

  private enum ExportType {
    SAVE, SHARE
  }

  // View.
  @Bind(R.id.coordinatatorLayout) CoordinatorLayout mCoordinatorLayout;
  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.gifImageView) GifImageView mGifImageView;
  @Bind(R.id.progressBar) ProgressBar mProgressBar;
  @Bind(R.id.zeroStateMessage) TextView mZeroStateMessage;
  @Bind(R.id.effectChooser) EffectChooser mEffectChooser;
  private AddTextView mAddTextView; // action bar custom view.
  private View mGifAreaView;
  private BuyToUnlockDialogFragment mBuyDialogFragment;
  private ProgressDialog mShareProgressDialog;

  // Model
  private Uri mSelectedUri;

  private Rect mSelectedHotspot;
  private Bitmap mSelectedBitmap;
  private Bitmap mSelectedBitmapForThumbnail;
  private Rect mSelectedHotspotForThumbnail;

  private String mSelectedEffectName = Effects.listEffectTemplates().get(0).getName();
  private String mSelectedEndText;

  private List<String> mPurchasedPackNames = Lists.newArrayList();

  private byte[] mSelectedGifBytes; // TODO(clocksmith): this should be queried from the service
  private Map<String, Double> mGifProgresses;
  private boolean mGeneratingGif = false;
  private boolean mNeedsUpdatePurchasePackNames;
  private boolean mIsAddTextViewShowing = false;

  // Media Exports
  private VideoCreator mVideoCreator;
  private ExportGifTask mExportGifTask;

  // Services
  private GifService mGifService;
  private GifServiceConnection mGifServiceConnection;
  private IInAppBillingService mBillingService;
  private BillingServiceConnection mBillingServiceConnection;
  private Queue<Runnable> mDeferredGifServiceRunnables = Queues.newConcurrentLinkedQueue();

  // Receivers
  private GifSharedReceiver mGifSharedReceiver;
  private VideoSharedReceiver mVideoSharedReceiver;

  // TODO(clocksmith): get rid of need for these.
  private FileType mSelectedFileType;
  private ExportType mSelectedExportType;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create);
    ButterKnife.bind(this);
    BusProvider.getInstance().register(this);
    initView();

    if (getIntent() != null && getIntent().getParcelableExtra(Constants.HOTSPOT) != null) {
      // If this intent is from hotspot chooser, then we need to handle it.
      handleIntentFromHotspotChooser(getIntent());
    } else if (savedInstanceState != null) {
      // Get the saved state is being recreated.
      unpackBundle(savedInstanceState);
      updateView(); // TODO(clocksmith): make this not have to be called before initServices().
    }

    initReceivers();
    initServices();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    SzLog.f(TAG, "onActivityResult(): requestCode: " + requestCode + " resultCode: " + resultCode);

    if (requestCode == Constants.REQUEST_PICK_IMAGE) {
      if (resultCode == RESULT_OK) {
        handleIntentFromImageChooser(data);
      } else {
        // If the image chooser was cancelled and there is no hotspot or no bitmap,
        // assume the user does not want to resume this activity.
        if (mSelectedUri == null && mSelectedHotspot == null || mSelectedBitmap == null) {
          finish();
        }
      }
    } else if (requestCode == Constants.REQUEST_CROP_IMAGE) {
      if (resultCode == RESULT_OK) {
        handleIntentFromHotspotChooser(data);
      } else {
        // If the hotspot chooser was cancelled, invalidate the uri and assume the user wants a different image.
        if (mSelectedHotspot == null) {
          mSelectedUri = null;
        }
      }
    } else if (requestCode == Constants.REQUEST_BUY_PACK) {
      if (resultCode == RESULT_OK) {
        updatePurchasedPackNamesAndEffectModels();
        // continue exporting the current effect since the buy had to come from an export.
        exportCurrentEffect();
      } else {
        // TODO(clocksmith): something;
      }
    } else {
      SzLog.e(TAG, "onActivityResult(): requestCode: " + requestCode + " resultCode: " + resultCode);
      finish();
    }
  }

  @Override protected void onResume() {
    super.onResume();

    if (mSelectedUri == null) {
      launchImageChooser();
    } else if (mSelectedHotspot == null) {
      launchHotspotChooser();
    } else if (mSelectedBitmap == null
        || mSelectedBitmapForThumbnail == null
        || mSelectedHotspotForThumbnail == null)  {
      initBitmapAndGifs();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull  int[] grantResults) {
    if (requestCode == Constants.REQUEST_SHARE_GIF_PERMISSIONS) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        exportCurrentEffect();
      } else {
        final DialogFragment newFragment = PermissionsWarningDialogFragment.newInstance();
        newFragment.show(getSupportFragmentManager(), PermissionsWarningDialogFragment.class.getSimpleName());
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
      case R.id.action_change_image:
        handleChangeImagePressed();
        return true;
      case R.id.action_change_hotspot:
        handleChangeHotspotPressed();
        return true;
      case R.id.action_add_text:
        handleAddTextPressed();
        return true;
      case R.id.action_ok:
        handleAddTextConfirmed();
        return true;
      case R.id.action_save_gif:
        handleSavePressed(FileType.GIF);
        return true;
      case R.id.action_save_video:
        handleSavePressed(FileType.VIDEO);
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
    menu.findItem(R.id.action_save).setVisible(!mIsAddTextViewShowing);
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

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    super.onSaveInstanceState(savedInstanceState);
    packBundle(savedInstanceState);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    ButterKnife.unbind(this);
    BusProvider.getInstance().unregister(this);

    if (mGifService != null) {
      unbindService(mGifServiceConnection);
    }
    if (mBillingService != null) {
      unbindService(mBillingServiceConnection);
    }

    unregisterReceiver(mGifSharedReceiver);
    unregisterReceiver(mVideoSharedReceiver);
  }

  @Subscribe
  public void on(EffectThumbnailViewHolder.ItemClickEvent event) throws IOException {
    // TODO(clocksmith): this could be decouples.
    mSelectedEffectName = event.effectName;
    mEffectChooser.setSelectedEffect(mSelectedEffectName);
    updateProgressBar();
    updateMainGif();
  }

  @Subscribe
  public void on(GifService.GifReadyEvent event) throws IOException {
    if (!event.thumbnail) {
      SzLog.f(TAG, "main gif ready: " + event.effectName);
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
  public void on(final BuyToUnlockDialogView.OnBuyClickedEvent event) {
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

  @Subscribe
  public void on(final BuyToUnlockDialogView.OnCancelClickedEvent event) {
    mBuyDialogFragment.dismiss();
  }

  private void init() {
    initView();
    initReceivers();
    initServices();
  }

  private void initView() {
    initToolbar();
    initGifArea();
    initEffectChooser();
  }

  private void initServices() {
    bindGifService();
    bindBillingService();
  }

  private void initReceivers() {
    mGifSharedReceiver = new GifSharedReceiver();
    mVideoSharedReceiver = new VideoSharedReceiver();
    registerReceiver(mGifSharedReceiver, new IntentFilter(Intent.ACTION_SEND));
    registerReceiver(mVideoSharedReceiver, new IntentFilter(Intent.ACTION_SEND));
  }

  private void initToolbar() {
    setSupportActionBar(mToolbar);
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
    for (EffectTemplate template : Effects.listEffectTemplates()) {
      mGifProgresses.put(template.getName(), 0d);
    }
    mGifAreaView = mZeroStateMessage;
  }

  private void initEffectChooser() {
    mEffectChooser.initForCreateActivity();
  }

  private void updateView() {
    mEffectChooser.setSelectedEffect(mSelectedEffectName);
  }

  private void launchImageChooser() {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("image/*");
    startActivityForResult(Intent.createChooser(intent, "Select Image"), Constants.REQUEST_PICK_IMAGE);
  }

  private void launchHotspotChooser() {
    Intent intent = new Intent(CreateActivity.this, HotspotChooserActivity.class);
    intent.putExtra(Intent.EXTRA_STREAM, mSelectedUri);
    startActivityForResult(intent, Constants.REQUEST_CROP_IMAGE);
  }

  private void initBitmapAndGifs() {
    try {
      setSelectedBitmapsFromUri();
      setSelectedHotspotForThumbnail();
      clearAndUpdateAllGifs();
    } catch (FileNotFoundException e) {
      SzLog.e(TAG, "Cannot consume bitmap for path: " + mSelectedUri.toString());
      launchImageChooser();
    }
  }

  private void handleIntentFromImageChooser(Intent intent) {
    Uri newUri = intent.getData();
    if (newUri != null) {
      mSelectedHotspot = null;
      mSelectedUri = newUri;
    }
  }

  private void handleIntentFromHotspotChooser(Intent intent) {
    Uri newUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
    if (newUri != null) {
      mSelectedUri = newUri;
    }

    Rect newHotspot = intent.getParcelableExtra(Constants.HOTSPOT);
    if (newHotspot != null) {
      mSelectedBitmap = null;
      mSelectedHotspot = newHotspot;
    }
  }

  private void handleChangeImagePressed() {
    SzAnalytics.newSelectChangeImageEvent().log(this);
    launchImageChooser();
  }

  private void handleChangeHotspotPressed() {
    SzAnalytics.newSelectChangeHotspotEvent().log(this);
    launchHotspotChooser();
  }

  private void handleAddTextPressed() {
    mAddTextView.getEditText().requestFocus();
    KeyboardUtils.showKeyboard(this);
    showAddTextView(true);
  }

  private void handleAddTextConfirmed() {
    mSelectedEndText = mAddTextView.getEditText().getText().toString();
    mSelectedGifBytes = null;
    updateMainGif();
    KeyboardUtils.hideKeyboard(this);
    showAddTextView(false);
  }

  private void handleSavePressed(FileType fileType) {
    mSelectedExportType = ExportType.SAVE;
    mSelectedFileType = fileType;
    handleExportPressed();
  }

  private void handleSharePressed(FileType fileType) {
    mSelectedExportType = ExportType.SHARE;
    mSelectedFileType = fileType;
    handleExportPressed();
  }

  private void handleExportPressed() {
    EffectModel effectModel = mEffectChooser.getEffectModel(mSelectedEffectName);
    if (effectModel == null) {
      // TODO(clocksmith): tell the user they must have an effect, or just disable share button.
      Snackbar.make(mCoordinatorLayout, "Choose an effect first!", Snackbar.LENGTH_SHORT).show();
    } else if (mGeneratingGif) {
      Snackbar.make(mCoordinatorLayout, "Please wait till gif preview is finished!", Snackbar.LENGTH_SHORT).show();
    } else if (!effectModel.isLocked()) {
      exportCurrentEffect();
    } else {
      String effectName = effectModel.getEffectTemplate().getName();
      String packName = effectModel.getEffectTemplate().getPackName();
      SzLog.f(TAG, "Showing dialog for effectName: " + effectName + " and packName: " + packName);
      showBuyDialog(effectName, packName);
    }
  }

  private void updateMainGif() {
    mGifProgresses.put(mSelectedEffectName, 0d);

    final MediaConfig mediaConfig = getMainMediaConfig();
    if (mGifService != null) {
      mGifService.requestMainGif(mediaConfig);
    } else {
      mDeferredGifServiceRunnables.add(new Runnable() {
        @Override
        public void run() {
          mGifService.requestMainGif(mediaConfig);
        }
      });
    }
  }

  private void updateThumbnailGifs() {
    if (mPurchasedPackNames == null || mNeedsUpdatePurchasePackNames) {
      updatePurchasedPacksThenUpdateThumbnailGifs();
    } else {
      final List<MediaConfig> mediaConfigs = Lists.transform(Effects.listEffectTemplates(),
          new Function<EffectTemplate, MediaConfig>() {
            @Override
            public MediaConfig apply(EffectTemplate effectTemplate) {
              return getThumbnailMediaConfig(effectTemplate);
            }
          });
      if (mGifService != null) {
        mGifService.requestThumbnailGifs(mediaConfigs);
      } else {
        mDeferredGifServiceRunnables.add(new Runnable() {
          @Override
          public void run() {
            mGifService.requestThumbnailGifs(mediaConfigs);
          }
        });
      }
    }
  }

  private void clearAndUpdateAllGifs() {
    clearAllGifs();
    updateThumbnailGifs();
    updateMainGif();
  }

  public void clearAllGifs() {
    clearGifService();
    clearGifsFromThumbnails();
    clearMainGif();
  }

  private void clearGifService() {
    if (mGifService != null) {
      mGifService.clear();
    } else {
      mDeferredGifServiceRunnables.add(new Runnable() {
        @Override
        public void run() {
          mGifService.clear();
        }
      });
    }
  }

  public void clearMainGif() {
    if (mGifService != null) {
      mGifService.clear();
    }
    mSelectedGifBytes = null;
    mGifImageView.setImageBitmap(null);
  }

  private void clearGifsFromThumbnails() {
    mEffectChooser.clearAllGifBytes();
    mGifProgresses = Maps.newHashMap();
    for (EffectTemplate template : Effects.listEffectTemplates()) {
      mGifProgresses.put(template.getName(), 0d);
    }
  }

//  private void resetEndText() {
//    mSelectedEndText = "";
//    mAddTextView.getEditText().setText("");
//  }

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
        mEffectChooser.setLockStatuses(mPurchasedPackNames);
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
    mBuyDialogFragment = BuyToUnlockDialogFragment.newInstance(effectName, packName);
    mBuyDialogFragment.show(getSupportFragmentManager(), BuyToUnlockDialogFragment.class.getSimpleName());
  }

  private void showShareProgressDialog(String message) {
    mShareProgressDialog = new ProgressDialog(this);
    mShareProgressDialog.setMessage(message);
    mShareProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
      @Override
      public void onDismiss(DialogInterface dialog) {
        if (mVideoCreator != null) {
          mVideoCreator.cancel();
          mVideoCreator = null;
        }
        if (mExportGifTask != null) {
          mExportGifTask.cancel(true);
          mExportGifTask = null;
        }
      }
    });
    mShareProgressDialog.show();
  }

  private void dismissShareProgressDialog() {
    mShareProgressDialog.dismiss();
  }

  private void exportCurrentEffect() {
    if (mSelectedGifBytes != null) {
      if (mSelectedFileType == FileType.VIDEO) {
        exportVideoAsync();
      } else if (mSelectedFileType == FileType.GIF){
        exportGifAsync();
      }
    }
    // TODO(clocksmith): Put a message here telling the user they need to make a gif first.
  }

  private void exportGifAsync() {
    if (PermissionUtils.checkReadwriteExternalStorage(this)) {
      PermissionUtils.requestReadWriteExternalStorage(this);
    } else {
      showShareProgressDialog("Preparing gif...");
      mExportGifTask = new ExportGifTask();
      mExportGifTask.execute();
    }
  }

  private void exportVideoAsync() {
    if (PermissionUtils.checkReadwriteExternalStorage(this)) {
      PermissionUtils.requestReadWriteExternalStorage(this);
    } else {
      showShareProgressDialog("Preparing video...");
      mVideoCreator = new VideoCreator(this, getMainMediaConfig());
      mVideoCreator.createAsync(new VideoCreatorCallback() {
        @Override
        public void onCreateVideo(File videoFile) {
          if (videoFile != null) {
            SzAnalytics.newVideoSavedEvent()
                .withItemId(mSelectedEffectName)
                .withHotspotScale(mSelectedHotspot.width() / mSelectedBitmap.getWidth())
                .withEndTextLength(mSelectedEndText == null ? 0 : mSelectedEndText.length())
                .log(CreateActivity.this);

            if (mSelectedExportType == ExportType.SHARE) {
              Intent baseIntent = new Intent(Intent.ACTION_SEND);
              baseIntent.setType(FileType.VIDEO.mime);
              Uri uri = Uri.fromFile(videoFile);
              baseIntent.putExtra(Intent.EXTRA_STREAM, uri);

              if (Build.VERSION.SDK_INT >= 22) {
                Intent receiver = new Intent(CreateActivity.this, GifSharedReceiver.class);
                receiver.putExtra(Constants.SELECTED_EFFECT_NAME, mSelectedEffectName);
                receiver.putExtra(Constants.HOTSPOT_SCALE, (float) mSelectedHotspot.width() / mSelectedBitmap.getWidth());
                receiver.putExtra(Constants.END_TEXT_LENGTH, mSelectedEndText == null ? 0 : mSelectedEndText.length());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    CreateActivity.this, Constants.REQUEST_SHARE_VIDEO, receiver, PendingIntent.FLAG_UPDATE_CURRENT);
                Intent chooserIntent = Intent.createChooser(baseIntent, "Share via", pendingIntent.getIntentSender());
                startActivity(chooserIntent);
              } else {
                startActivity(Intent.createChooser(baseIntent, "Share via"));
              }
            } else if (mSelectedExportType == ExportType.SAVE) {
              Snackbar.make(mCoordinatorLayout, "Video Saved!", Snackbar.LENGTH_SHORT).show();
            }
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
            .withHotspotScale(mSelectedHotspot.width() / mSelectedBitmap.getWidth())
            .withEndTextLength(mSelectedEndText == null ? 0 : mSelectedEndText.length())
            .log(this);

        return gifFile;
      } catch (IOException e) {
        SzLog.e(TAG, "cannot save gif", e);
      }
    }

    return null;
  }

  private void setSelectedBitmapsFromUri() throws FileNotFoundException {
    mSelectedBitmap = BitmapUtils.readScaledBitmap(mSelectedUri, this.getContentResolver());
    if (DebugUtils.SAVE_SRC_AS_PNG) {
      BitmapUtils.saveBitmapToDiskAsPng(mSelectedBitmap, "src_0");
    }
    mSelectedBitmapForThumbnail = BitmapUtils.readScaledBitmap(
        mSelectedUri,
        this.getContentResolver(),
        MathUtils.roundToEvenNumber(Constants.MAX_DIMEN_FOR_MIN_SELECTED_DIMEN_PX / Constants.MEDIA_THUMBNAIL_DIVIDER));
  }

  private MediaConfig getMainMediaConfig() {
    return MediaConfig.newBuilder()
        .withHotspot(mSelectedHotspot)
        .withBitmap(mSelectedBitmap)
        .withEffectTemplate(Effects.getEffectTemplate(mSelectedEffectName))
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

  private void setSelectedHotspotForThumbnail() {
    float ratio = (float) mSelectedBitmap.getWidth() / mSelectedBitmapForThumbnail.getWidth();
    mSelectedHotspotForThumbnail = new Rect(
        (int) (mSelectedHotspot.left / ratio),
        (int) (mSelectedHotspot.top / ratio),
        (int) (mSelectedHotspot.right / ratio),
        (int) (mSelectedHotspot.bottom / ratio));
  }

  private void unpackBundle(Bundle bundle) {
    mSelectedUri = bundle.getParcelable(Constants.SELECTED_URI);
    mSelectedHotspot = bundle.getParcelable(Constants.SELECTED_HOTSPOT);
    mSelectedEffectName = bundle.getString(Constants.SELECTED_EFFECT_NAME);
    mSelectedEndText = bundle.getString(Constants.SELECTED_END_TEXT);
    mPurchasedPackNames = bundle.getStringArrayList(Constants.PURCHASED_PACK_NAMES);
    mNeedsUpdatePurchasePackNames = bundle.getBoolean(Constants.NEEDS_UPDATE_PURCHASED_PACK_NAMES);
  }

  private void packBundle(Bundle bundle) {
    bundle.putParcelable(Constants.SELECTED_URI, mSelectedUri);
    bundle.putParcelable(Constants.SELECTED_HOTSPOT, mSelectedHotspot);
    bundle.putString(Constants.SELECTED_EFFECT_NAME, mSelectedEffectName);
    bundle.putString(Constants.SELECTED_END_TEXT, mSelectedEndText);
    bundle.putStringArrayList(Constants.PURCHASED_PACK_NAMES, Lists.newArrayList(mPurchasedPackNames));
    bundle.putBoolean(Constants.NEEDS_UPDATE_PURCHASED_PACK_NAMES, mNeedsUpdatePurchasePackNames);
  }

  private class GifServiceConnection implements ServiceConnection {
    @Override
    public void onServiceConnected(ComponentName className, IBinder iBinder) {
      mGifService = ((GifService.GifServiceBinder) iBinder).getService();
      while (!mDeferredGifServiceRunnables.isEmpty()) {
        mDeferredGifServiceRunnables.remove().run();
      }
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

  private class ExportGifTask extends AsyncTask<Void, Void, Boolean> {
    private Intent mBaseIntent;

    @Override
    protected Boolean doInBackground(Void... params) {
      File gifFile = saveCurrentGifToDisk();

      if (gifFile != null) {
        if (mSelectedExportType == ExportType.SHARE) {
          mBaseIntent = new Intent(Intent.ACTION_SEND);
          mBaseIntent.setType(FileType.GIF.mime);
          Uri uri = Uri.fromFile(gifFile);
          mBaseIntent.putExtra(Intent.EXTRA_STREAM, uri);
        }
        return true;
      } else {
        SzLog.e(TAG, "gif file is null");
        return false;
      }
    }

    @Override
    protected void onPostExecute(Boolean result) {
      if (result) {
        if (mSelectedExportType == ExportType.SHARE) {
          if (Build.VERSION.SDK_INT >= 22) {
            Intent receiver = new Intent(CreateActivity.this, GifSharedReceiver.class);
            receiver.putExtra(Constants.SELECTED_EFFECT_NAME, mSelectedEffectName);
            receiver.putExtra(Constants.HOTSPOT_SCALE, (float) mSelectedHotspot.width() / mSelectedBitmap.getWidth());
            receiver.putExtra(Constants.END_TEXT_LENGTH, mSelectedEndText == null ? 0 : mSelectedEndText.length());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                CreateActivity.this, Constants.REQUEST_SHARE_GIF, receiver, PendingIntent.FLAG_UPDATE_CURRENT);
            Intent chooserIntent = Intent.createChooser(mBaseIntent, "Share via", pendingIntent.getIntentSender());
            startActivity(chooserIntent);
          } else {
            startActivity(Intent.createChooser(mBaseIntent, "Share via"));
          }
        } else {
          Snackbar.make(mCoordinatorLayout, "GIF Saved!", Snackbar.LENGTH_SHORT).show();
        }
      } else {
        Snackbar.make(mCoordinatorLayout, "Unable to save GIF!", Snackbar.LENGTH_SHORT).show();
      }
      dismissShareProgressDialog();
    }
  }

  public static class GifSharedReceiver extends BroadcastReceiver {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onReceive(Context context, Intent intent) {
      handleOnSharedReceive(context, intent, FileType.GIF);
    }
  }

  public static class VideoSharedReceiver extends BroadcastReceiver {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onReceive(Context context, Intent intent) {
      handleOnSharedReceive(context, intent, FileType.VIDEO);
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
  private static void handleOnSharedReceive(Context context, Intent intent, FileType fileType) {
    ComponentName componentName = intent.getParcelableExtra(Intent.EXTRA_CHOSEN_COMPONENT);

    String selectedEffectName = intent.getStringExtra(Constants.SELECTED_EFFECT_NAME);
    float hotspotScale = intent.getFloatExtra(Constants.HOTSPOT_SCALE, 0);
    int endTextLength = intent.getIntExtra(Constants.END_TEXT_LENGTH, -1);

    SzAnalytics.Event event;
    if (fileType == FileType.GIF) {
      event = SzAnalytics.newGifSharedEvent();
    } else if (fileType == FileType.VIDEO) {
      event = SzAnalytics.newVideoSharedEvent();
    } else {
      SzLog.e(TAG, "handleOnSharedReceive unsupported fileType: " + fileType);
      return;
    }
    event
        .withItemId(selectedEffectName)
        .withHotspotScale(hotspotScale)
        .withEndTextLength(endTextLength)
        .withPackageName(componentName.getPackageName())
        .log(context);
  }
}
