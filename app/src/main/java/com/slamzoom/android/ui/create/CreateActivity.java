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
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
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
import com.google.common.collect.Lists;
import com.slamzoom.android.R;
import com.slamzoom.android.billing.GetBuyIntentCallback;
import com.slamzoom.android.billing.GetPurchasedPacksCallback;
import com.slamzoom.android.billing.IabHelper;
import com.slamzoom.android.common.intents.Intents;
import com.slamzoom.android.common.intents.Params;
import com.slamzoom.android.common.intents.RequestCodes;
import com.slamzoom.android.common.ui.widgets.BackInterceptingEditText;
import com.slamzoom.android.mediacreation.MediaConstants;
import com.slamzoom.android.common.files.FileType;
import com.slamzoom.android.common.fonts.FontProvider;
import com.slamzoom.android.common.files.FileUtils;
import com.slamzoom.android.common.events.BusProvider;
import com.slamzoom.android.common.ui.AnimationUtils;
import com.slamzoom.android.common.ui.KeyboardUtils;
import com.slamzoom.android.common.logging.SzAnalytics;
import com.slamzoom.android.common.logging.SzLog;
import com.slamzoom.android.common.settings.Permissions;
import com.slamzoom.android.common.ui.SnackbarPresenter;
import com.slamzoom.android.effects.Effects;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.common.bitmaps.BitmapSet;
import com.slamzoom.android.mediacreation.MediaConfig;
import com.slamzoom.android.mediacreation.StopwatchTracker;
import com.slamzoom.android.mediacreation.gif.DeferrableGifService;
import com.slamzoom.android.mediacreation.gif.GifCreator;
import com.slamzoom.android.mediacreation.gif.GifService;
import com.slamzoom.android.mediacreation.video.VideoCreator;
import com.slamzoom.android.mediacreation.video.VideoCreatorCallback;
import com.slamzoom.android.ui.create.effectchooser.EffectChooser;
import com.slamzoom.android.ui.create.effectchooser.EffectModel;
import com.slamzoom.android.ui.create.effectchooser.EffectThumbnailViewHolder;
import com.slamzoom.android.ui.start.CreateTemplate;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

// TODO(clocksmith): make this class smaller. extract services, etc.
public class CreateActivity extends AppCompatActivity {
  private static final String TAG = CreateActivity.class.getSimpleName();

  private enum ExportType {
    SAVE, SHARE
  }

  private enum CustomToolbarView {
    ADD_TEXT, TITLE
  }

  // Model
  private CreateModel mModel;

  // View
  @BindView(R.id.coordinatatorLayout) CoordinatorLayout mCoordinatorLayout;
  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.toolbarTitle) TextView mToolbarTitle;
  @BindView(R.id.gifViewCoordinatatorLayout) CoordinatorLayout mGifViewCoordinatorLayout;
  @BindView(R.id.gifImageView) GifImageView mGifImageView;
  @BindView(R.id.progressBar) ProgressBar mProgressBar;
  @BindView(R.id.zeroStateMessage) TextView mZeroStateMessage;
  @BindView(R.id.effectChooser) EffectChooser mEffectChooser;

  private AddTextView mAddTextView; // action bar custom view.
  private View mGifAreaView;
  private UnlockPackDialogFragment mUnlockPackDialogFragment;
  private ProgressDialog mProgressDialog;

  // Services
  // TODO(clocksmith): try to encapsulate this
  private DeferrableGifService mDeferredGifService;
  private IInAppBillingService mBillingService;
  private BillingServiceConnection mBillingServiceConnection;

  // Receivers
  private GifSharedReceiver mGifSharedReceiver;
  private VideoSharedReceiver mVideoSharedReceiver;

  // Media helpers
  private VideoCreator mVideoCreator;
  private ExportGifTask mExportGifTask;

  // Other temporary state
  // TODO(clocksmith): get rid of need for these.
  private FileType mSelectedFileType;
  private ExportType mSelectedExportType;
  private boolean mSkipResume;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    SzLog.f(TAG, "onCreate");
    super.onCreate(savedInstanceState);

    initModel(getIntent(), savedInstanceState);
    initView();
    initServices();
    initReceivers();
    initEventBus();
  }

  private void initModel(Intent intent, Bundle savedInstanceState) {
    mModel = new CreateModel();
    if (intent.getParcelableExtra(Params.HOTSPOT) != null) {
      handleHotspotSelected(intent);
    } else if (intent.getParcelableExtra(Params.CREATE_TEMPLATE) != null) {
      handleCreateTemplateIntent(getIntent());
    } else if (savedInstanceState != null) {
      unpackSavedInstanceState(savedInstanceState);
    }
  }

  private void initView() {
    setContentView(R.layout.activity_create);
    ButterKnife.bind(this);
    initToolbar();
    initGifArea();
    initEffectChooser();
  }

  private void initToolbar() {
    setSupportActionBar(mToolbar);
    assert getSupportActionBar() != null;

    mToolbarTitle.setTypeface(FontProvider.getInstance().getTitleFont());

    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    mAddTextView = new AddTextView(this);
    getSupportActionBar().setCustomView(mAddTextView,
        new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    showCustomToolbarView(CustomToolbarView.TITLE);
  }

  private void initGifArea() {
    mGifAreaView = mZeroStateMessage;
  }

  private void initEffectChooser() {
    mEffectChooser.initForCreateActivity();
    mEffectChooser.setSelectedEffect(mModel.getSelectedEffectName());
  }

  private void initServices() {
    mDeferredGifService = new DeferrableGifService(this);
    mDeferredGifService.bind();
    bindBillingService();
  }

  private void initReceivers() {
    mGifSharedReceiver = new GifSharedReceiver();
    mVideoSharedReceiver = new VideoSharedReceiver();
    registerReceiver(mGifSharedReceiver, new IntentFilter(Intent.ACTION_SEND));
    registerReceiver(mVideoSharedReceiver, new IntentFilter(Intent.ACTION_SEND));
  }

  private void initEventBus() {
    BusProvider.getInstance().register(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_create, menu);

    // Set icon colors for older devices that do not support tint.
    // TODO(clocksmith): Drop this and just change the icon png files to the accent color.
    if (Build.VERSION.SDK_INT < 21) {
      int color = ContextCompat.getColor(this, R.color.colorAccent);
      menu.findItem(R.id.action_add_text).getIcon().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
      menu.findItem(R.id.action_change_hotspot).getIcon().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
      menu.findItem(R.id.action_change_image).getIcon().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
      menu.findItem(R.id.action_share).getIcon().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
      menu.findItem(R.id.action_ok).getIcon().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
      menu.findItem(R.id.action_share_gif).getIcon().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
      menu.findItem(R.id.action_share_video).getIcon().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
      menu.findItem(R.id.action_save_gif).getIcon().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
      menu.findItem(R.id.action_save_video).getIcon().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    menu.findItem(R.id.action_add_text).setVisible(!mModel.isAddTextViewShowing());
    menu.findItem(R.id.action_change_hotspot).setVisible(!mModel.isAddTextViewShowing());
    menu.findItem(R.id.action_change_image).setVisible(!mModel.isAddTextViewShowing());
    menu.findItem(R.id.action_share).setVisible(!mModel.isAddTextViewShowing());
    menu.findItem(R.id.action_ok).setVisible(mModel.isAddTextViewShowing());
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    SzLog.f(TAG, "onNewIntent");
    super.onNewIntent(intent);
    initModel(intent, null);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    SzLog.f(TAG, "onActivityResult(): requestCode: " + requestCode + " resultCode: " + resultCode);

    if (requestCode == RequestCodes.REQUEST_SELECT_IMAGE) {
      if (resultCode == RESULT_OK) {
        handleImageSelected(data);
      } else {
        handleImageChooserCancelled();
      }
    } else if (requestCode == RequestCodes.REQUEST_SELECT_HOTSPOT) {
      if (resultCode == RESULT_OK) {
        handleHotspotSelected(data);
      } else {
        handleHotspotChooserCancelled();
      }
    } else if (requestCode == RequestCodes.REQUEST_BUY_PACK) {
      if (resultCode == RESULT_OK) {
        handleBuyPackSuccess();
      } else {
        handleBuyPackFailure();
      }
    } else {
      SzLog.e(TAG, "onActivityResult(): requestCode: " + requestCode + " resultCode: " + resultCode);
      finish();
    }
  }

  private void handleImageSelected(Intent intent) {
    // The next step after choosing an image is always to start the hotspot chooser.
    mSkipResume = true;
    Intents.startHotspotChooser(this, intent.getData(), false);
  }

  private void handleImageChooserCancelled() {
    // If there is no selcted uri, assume the user does not want to resume this activity.
    // Else this was a cancel from a change image request and we can simply resume.
    if (mModel.getSelectedUri() == null) {
      finish();
    }
  }

  private void handleHotspotSelected(Intent intent) {
    // Once the hotspot is selected, both the uri and the hotspot become valid.
    mModel.setSelectedUri((Uri) intent.getParcelableExtra(Params.IMAGE_URI));
    mModel.setSelectedHotspot((RectF) intent.getParcelableExtra(Params.HOTSPOT));

    // Invalidate the bitmap set to force the gif creation process when resumed.
    mModel.setSelectedBitmapSet(null);
    // Invalidate the selected text since the image/hotspot has changed.

    // Clear the text only if it was not from a hotspot change.
    if (!intent.getBooleanExtra(Params.FROM_CHANGE_HOTSPOT_REQUEST, false)) {
      mModel.setSelectedEndText(null);
    }
  }

  private void handleHotspotChooserCancelled() {
    // If either the selected uri or hotspot is null, the user has not yet confirmed an image so reluanch image chooser.
    // This is overly cautious since if one of these is null, they should both be null anyways.
    if (mModel.getSelectedUri() == null) {
      mSkipResume = true;
      Intents.startImageChooser(this);
    }
  }

  private void handleCreateTemplateIntent(Intent intent) {
    CreateTemplate createTemplate = intent.getParcelableExtra(Params.CREATE_TEMPLATE);
    mModel.setSelectedUri(createTemplate.uri);
    mModel.setSelectedHotspot(createTemplate.hotspot);
  }

  @Override protected void onResume() {
    SzLog.f(TAG, "onResume");
    super.onResume();
    if (!mSkipResume) {
      if (mModel.getSelectedUri() == null) {
        Intents.startImageChooser(this);
      } else if (mModel.getSelectedBitmapSet() == null) {
        mModel.setSelectedBitmapSet(createBitmapSet());
        refreshMainAndThumbnailGifs();
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull  int[] grantResults) {
    if (requestCode == RequestCodes.REQUEST_SHARE_GIF_PERMISSIONS) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        exportCurrentEffect();
      } else {
        final DialogFragment newFragment = PermissionsWarningDialogFragment.newInstance();
        newFragment.show(getSupportFragmentManager(), PermissionsWarningDialogFragment.class.getSimpleName());
      }
    }
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
      case R.id.action_share_gif:
        handleSharePressed(FileType.GIF);
        return true;
      case R.id.action_share_video:
        handleSharePressed(FileType.VIDEO);
        return true;
      case R.id.action_save_gif:
        handleSavePressed(FileType.GIF);
        return true;
      case R.id.action_save_video:
        handleSavePressed(FileType.VIDEO);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onBackPressed() {
    if (mModel.isAddTextViewShowing()) {
      KeyboardUtils.hideKeyboard(this);
      showCustomToolbarView(CustomToolbarView.TITLE);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    super.onSaveInstanceState(savedInstanceState);
    saveInstanceState(savedInstanceState);
  }

  private void saveInstanceState(Bundle savedInstanceState) {
    savedInstanceState.putParcelable(Params.CREATE_MODEL, mModel);
  }

  private void unpackSavedInstanceState(Bundle savedInstanceState) {
    mModel = savedInstanceState.getParcelable(Params.CREATE_MODEL);
  }

  @Override
  protected void onPause() {
    super.onPause();
    mSkipResume = false;
  }
  @Override
  public void onDestroy() {
    super.onDestroy();

    BusProvider.getInstance().unregister(this);

    mDeferredGifService.unbind();
    if (mBillingService != null) {
      unbindService(mBillingServiceConnection);
    }

    unregisterReceiver(mGifSharedReceiver);
    unregisterReceiver(mVideoSharedReceiver);
  }

  @Subscribe
  public void on(EffectThumbnailViewHolder.ItemClickEvent event) throws IOException {
    // TODO(clocksmith): this could be decoupled.
    mModel.setSelectedEffectName(event.effectName);
    mEffectChooser.setSelectedEffect(event.effectName);
    updateProgressBar();
    updateMainGif();
  }

  @Subscribe
  public void on(GifService.GifReadyEvent event) throws IOException {
    if (!event.thumbnail) {
      if (mModel.getSelectedEffectName().equals(event.effectName)) {
        mZeroStateMessage.setVisibility(View.GONE);
        mModel.setGeneratingGif(false);
        showCurrentGif();
      }
    }
  }

  @Subscribe
  public void on(GifService.GifGenerationStartEvent event) {
    mModel.setGeneratingGif(true);
    showProgressBar();
  }

  @Subscribe
  public void on(GifCreator.ProgressUpdateEvent event) throws IOException {
    mModel.getGifProgresses().put(
        event.effectName,
        mModel.getGifProgresses().get(event.effectName) + event.amountToUpdate);
    updateProgressBar();
  }

  @Subscribe
  public void on(BackInterceptingEditText.OnBackPressedEvent event) {
    onBackPressed();
  }

  @Subscribe
  public void on(final UnlockPackDialogView.OnBuyClickedEvent event) {
    IabHelper.getBuyIntentByPack(event.packName, mBillingService, new GetBuyIntentCallback() {
      @Override
      public void onSuccess(PendingIntent buyIntent) {
        try {
          startIntentSenderForResult(buyIntent.getIntentSender(), RequestCodes.REQUEST_BUY_PACK, new Intent(), 0, 0, 0);
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
  public void on(final UnlockPackDialogView.OnCancelClickedEvent event) {
    mUnlockPackDialogFragment.dismiss();
  }

  private void handleBuyPackSuccess() {
    updatePurchasedPackNamesAndEffectModels();
    // continue exporting the current effect since the buy had to come from an export.
    exportCurrentEffect();
  }

  private void handleBuyPackFailure() {
    // TODO(clocksmith): something;
  }

  private void handleChangeImagePressed() {
    SzAnalytics.newSelectChangeImageEvent().log(this);
    Intents.startImageChooser(this);
  }

  private void handleChangeHotspotPressed() {
    SzAnalytics.newSelectChangeHotspotEvent().log(this);
    Intents.startHotspotChooser(this, mModel.getSelectedUri(), true);
  }

  private void handleAddTextPressed() {
    mAddTextView.getEditText().requestFocus();
    KeyboardUtils.showKeyboard(this);
    showCustomToolbarView(CustomToolbarView.ADD_TEXT);
  }

  private void handleAddTextConfirmed() {
    mModel.setSelectedEndText(mAddTextView.getEditText().getText().toString());
    updateMainGif();
    KeyboardUtils.hideKeyboard(this);
    showCustomToolbarView(CustomToolbarView.TITLE);
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
    EffectModel effectModel = mEffectChooser.getEffectModel(mModel.getSelectedEffectName());
    if (effectModel == null) {
      SnackbarPresenter.showErrorMessage(mGifViewCoordinatorLayout, "Choose an effect first!");
      // TODO(clocksmith): tell the user they must have an effect, or just disable share button.
    } else if (mModel.isGeneratingGif()) {
      SnackbarPresenter.showErrorMessage(mGifViewCoordinatorLayout, "Please wait till gif preview is finished!");
    } else if (!effectModel.isLocked()) {
      exportCurrentEffect();
    } else {
      String effectName = effectModel.getEffectTemplate().getName();
      String packName = effectModel.getEffectTemplate().getPackName();
      SzLog.f(TAG, "Showing dialog for effectName: " + effectName + " and packName: " + packName);
      showUnlockPackDialog(effectName, packName);
    }
  }

  private void refreshMainAndThumbnailGifs() {
    mDeferredGifService.clearGifService();
    updateThumbnailGifs();
    updateMainGif();
  }

  private void updateThumbnailGifs() {
    if (mModel.needsUpdatePurchasePackNames()) {
      updatePurchasedPacksThenUpdateThumbnailGifs();
    } else {
      clearGifsFromThumbnails();
      mDeferredGifService.requestThumbnailGifs(new Callable<List<MediaConfig>>() {
        @Override
        public List<MediaConfig> call() throws Exception {
          return Lists.transform(Effects.listEffectTemplates(),
              new Function<EffectTemplate, MediaConfig>() {
                @Override
                public MediaConfig apply(EffectTemplate effectTemplate) {
                  return getThumbnailMediaConfig(effectTemplate);
                }
              });
        }
      });
    }
  }

  private void updateMainGif() {
    clearMainGif();

    // TODO(clocksmith): localize this and some other methods to model
    mModel.getGifProgresses().put(mModel.getSelectedEffectName(), 0d);

    mDeferredGifService.requestMainGif(new Callable<MediaConfig>() {
      @Override
      public MediaConfig call() {
        return getMainMediaConfig();
      }
    });
  }

  private void clearGifsFromThumbnails() {
    mEffectChooser.clearAllGifBytes();
    mModel.setGifProgresses(null);
  }

  private void clearMainGif() {
    mGifImageView.setImageBitmap(null);
  }

  private MediaConfig getThumbnailMediaConfig(EffectTemplate effectTemplate) {
    return getBaseMediaConfigBuilder()
        .withEffectTemplate(effectTemplate)
        .withSize(MediaConstants.THUMBNAIL_SIZE_PX)
        .withFps(MediaConstants.THUMBNAIL_FPS)
        .build();
  }

  private MediaConfig getMainMediaConfig() {
    return getBaseMediaConfigBuilder()
        .withEffectTemplate(Effects.getEffectTemplate(mModel.getSelectedEffectName()))
        .withEndText(mModel.getSelectedEndText())
        .withSize(MediaConstants.MAIN_SIZE_PX)
        .withFps(MediaConstants.MAIN_FPS)
        .build();
  }

  private MediaConfig.Builder getBaseMediaConfigBuilder() {
    return MediaConfig.newBuilder()
        .withHotspot(mModel.getSelectedHotspot())
        .withBitmapSet(mModel.getSelectedBitmapSet());
  }

  // TODO(clocksmith): make this defer properly by using event bus.
  private byte[] getSelectedGifBytes() {
    return mDeferredGifService.getGifBytes(mModel.getSelectedEffectName(), mModel.getSelectedEndText());
  }

  private BitmapSet createBitmapSet() {
    return new BitmapSet(this, mModel.getSelectedUri(), MediaConstants.THUMBNAIL_SIZE_PX);
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
    IabHelper.getPurchasedPackNames(mBillingService, new GetPurchasedPacksCallback() {
      @Override
      public void onSuccess(List<String> packNames) {
        mModel.setPurchasedPackNames(packNames);
        mEffectChooser.setLockStatuses(packNames);
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
    if (mModel.getSelectedEffectName() != null) {
      // TODO(clocksmith): localize model method
      mProgressBar.setProgress((int) Math.round(100 * mModel.getGifProgresses().get(mModel.getSelectedEffectName())));
    }
  }

  private void showCustomToolbarView(CustomToolbarView toolbarView) {
    assert getSupportActionBar() != null;

    if (toolbarView == CustomToolbarView.TITLE){
      mModel.setAddTextViewShowing(false);
      mToolbarTitle.setVisibility(View.VISIBLE);
      getSupportActionBar().setDisplayShowCustomEnabled(false);
    } else  if (toolbarView == CustomToolbarView.ADD_TEXT) {
      mModel.setAddTextViewShowing(true);
      mToolbarTitle.setVisibility(View.GONE);
      getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

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
          byte[] gifBytes = getSelectedGifBytes();
          if (gifBytes != null) {
            mGifImageView.setImageDrawable(new GifDrawable(gifBytes));
          }
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

  private void showUnlockPackDialog(String effectName, String packName) {
    mUnlockPackDialogFragment = UnlockPackDialogFragment.newInstance(effectName, packName);
    mUnlockPackDialogFragment.show(getSupportFragmentManager(), UnlockPackDialogFragment.class.getSimpleName());
  }

  private void showProgressDialog(String message) {
    mProgressDialog = new ProgressDialog(this);
    mProgressDialog.setMessage(message);
    mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
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
    mProgressDialog.show();
  }

  private void dismissProgressDialog() {
    if (mProgressDialog != null) {
      mProgressDialog.dismiss();
    }
  }

  private void exportCurrentEffect() {
    if (getSelectedGifBytes() != null) {
      if (mSelectedFileType == FileType.VIDEO) {
        exportVideoAsync();
      } else if (mSelectedFileType == FileType.GIF){
        exportGifAsync();
      }
    }
    // TODO(clocksmith): Put a message here telling the user they need to make a gif first.
  }

  private void exportGifAsync() {
    if (Permissions.checkReadwriteExternalStorage(this)) {
      Permissions.requestReadWriteExternalStorage(this);
    } else {
      mExportGifTask = new ExportGifTask();
      mExportGifTask.execute();
    }
  }

  private void exportVideoAsync() {
    if (Permissions.checkReadwriteExternalStorage(this)) {
      Permissions.requestReadWriteExternalStorage(this);
    } else {
      showProgressDialog("Preparing video...");
      final StopwatchTracker tracker = new StopwatchTracker();
      mVideoCreator = new VideoCreator(this, getMainMediaConfig(), tracker);
      mVideoCreator.createAsync(new VideoCreatorCallback() {
        @Override
        public void onCreateVideo(File videoFile) {
          if (videoFile != null) {
            Log.wtf(TAG, tracker.getReport());

            SzAnalytics.newVideoSavedEvent()
                .withItemId(mModel.getSelectedEffectName())
                .withHotspotScale(mModel.getSelectedHotspot().width())
                .withEndTextLength(mModel.getSelectedEndTextLength())
                .log(CreateActivity.this);

            if (mSelectedExportType == ExportType.SHARE) {
              Intent baseIntent = new Intent(Intent.ACTION_SEND);
              baseIntent.setType(FileType.VIDEO.mime);
              Uri uri = Uri.fromFile(videoFile);
              baseIntent.putExtra(Intent.EXTRA_STREAM, uri);

              if (Build.VERSION.SDK_INT >= 22) {
                Intent receiver = new Intent(CreateActivity.this, VideoSharedReceiver.class);
                receiver.putExtra(Params.SELECTED_EFFECT_NAME, mModel.getSelectedEffectName());
                receiver.putExtra(Params.HOTSPOT_SCALE, mModel.getSelectedHotspot().width());
                receiver.putExtra(Params.END_TEXT_LENGTH, mModel.getSelectedEndTextLength());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    CreateActivity.this, RequestCodes.REQUEST_SHARE_VIDEO, receiver, PendingIntent.FLAG_UPDATE_CURRENT);
                Intent chooserIntent = Intent.createChooser(baseIntent, "Share via", pendingIntent.getIntentSender());
                startActivity(chooserIntent);
              } else {
                startActivity(Intent.createChooser(baseIntent, "Share via"));
              }
            } else if (mSelectedExportType == ExportType.SAVE) {
              SnackbarPresenter.showSuccessMessage(mGifViewCoordinatorLayout, "Video Saved!");
            }
          }
          // TODO(clocksmith): handle null
          dismissProgressDialog();
        }});
    }
  }

  private File saveCurrentGifToDisk() {
    File gifFile = FileUtils.createTimestampedFileWithId(FileType.GIF, mModel.getSelectedEffectName());
    if (getSelectedGifBytes() != null) {
      try {
        // TODO(clocksmith): make sure external apps can't destroy this (read only)
        FileOutputStream gifOutputStream = new FileOutputStream(gifFile);
        gifOutputStream.write(getSelectedGifBytes());
        gifOutputStream.close();

        // TODO(clocksmith): refactor with newVideoSavedEvent
        SzAnalytics.newGifSavedEvent()
            .withItemId(mModel.getSelectedEffectName())
            .withHotspotScale(mModel.getSelectedHotspot().width())
            .withEndTextLength(mModel.getSelectedEndTextLength())
            .log(this);

        return gifFile;
      } catch (IOException e) {
        SzLog.e(TAG, "cannot save gif", e);
      }
    }

    return null;
  }

  private void bindBillingService() {
    if (mBillingServiceConnection == null) {
      mBillingServiceConnection = new BillingServiceConnection();
    }
    Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
    serviceIntent.setPackage("com.android.vending");
    bindService(serviceIntent, mBillingServiceConnection, Context.BIND_AUTO_CREATE);
  }

  private class BillingServiceConnection implements ServiceConnection {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
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
            // TODO(clocksmith): refactor with other receiver.
            receiver.putExtra(Params.SELECTED_EFFECT_NAME, mModel.getSelectedEffectName());
            receiver.putExtra(Params.HOTSPOT_SCALE, mModel.getSelectedHotspot().width());
            receiver.putExtra(Params.END_TEXT_LENGTH, mModel.getSelectedEndTextLength());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                CreateActivity.this, RequestCodes.REQUEST_SHARE_GIF, receiver, PendingIntent.FLAG_UPDATE_CURRENT);
            Intent chooserIntent = Intent.createChooser(mBaseIntent, "Share via", pendingIntent.getIntentSender());
            startActivity(chooserIntent);
          } else {
            startActivity(Intent.createChooser(mBaseIntent, "Share via"));
          }
        } else {
          SnackbarPresenter.showSuccessMessage(mGifViewCoordinatorLayout, "GIF Saved!");
        }
      } else {
        SnackbarPresenter.showErrorMessage(mGifViewCoordinatorLayout, "Unable to save GIF");
      }
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

    String selectedEffectName = intent.getStringExtra(Params.SELECTED_EFFECT_NAME);
    float hotspotScale = intent.getFloatExtra(Params.HOTSPOT_SCALE, 0);
    int endTextLength = intent.getIntExtra(Params.END_TEXT_LENGTH, -1);

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
