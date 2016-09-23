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
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.slamzoom.android.billing.IabHelper;
import com.slamzoom.android.common.intents.Params;
import com.slamzoom.android.common.intents.RequestCodes;
import com.slamzoom.android.common.widgets.BackInterceptingEditText;
import com.slamzoom.android.mediacreation.MediaConstants;
import com.slamzoom.android.common.files.FileType;
import com.slamzoom.android.common.fonts.FontProvider;
import com.slamzoom.android.common.activities.LifecycleLoggingActivity;
import com.slamzoom.android.BuildFlags;
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
import com.slamzoom.android.mediacreation.gif.GifCreator;
import com.slamzoom.android.mediacreation.gif.GifService;
import com.slamzoom.android.mediacreation.video.VideoCreator;
import com.slamzoom.android.mediacreation.video.VideoCreatorCallback;
import com.slamzoom.android.ui.create.effectchooser.EffectChooser;
import com.slamzoom.android.ui.create.effectchooser.EffectModel;
import com.slamzoom.android.ui.create.effectchooser.EffectThumbnailViewHolder;
import com.slamzoom.android.ui.create.hotspotchooser.HotspotChooserActivity;
import com.slamzoom.android.ui.start.CreateTemplate;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import butterknife.BindView;
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
  @BindView(R.id.coordinatatorLayout) CoordinatorLayout mCoordinatorLayout;
  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.toolbarTitle) TextView mToolbarTitle;
  @BindView(R.id.gifViewCoordinatatorLayout) CoordinatorLayout mGifViewCoordinatorLayout;
  @BindView(R.id.gifImageView) GifImageView mGifImageView;
  @BindView(R.id.progressBar) ProgressBar mProgressBar;
  @BindView(R.id.zeroStateMessage) TextView mZeroStateMessage;
  @BindView(R.id.effectChooser) EffectChooser mEffectChooser;
  private ImageView mLogoView; // action bar custom view.
  private AddTextView mAddTextView; // action bar custom view.
  private View mGifAreaView;
  private UnlockPackDialogFragment mUnlockPackDialogFragment;
  private ProgressDialog mShareProgressDialog;

  // Model
  private Uri mSelectedUri;
  private RectF mSelectedHotspot;
  private BitmapSet mSelectedBitmapSet;
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
    SzLog.f(TAG, "onCreate");
    setSubTag(TAG);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create);
    ButterKnife.bind(this);
    BusProvider.getInstance().register(this);
    initView();

    if (getIntent() != null && getIntent().getParcelableExtra(Params.HOTSPOT) != null) {
      // If this intent is from hotspot chooser, then we need to handle it.
      handleHotspotSelectedFromChooser(getIntent());
    } else if (getIntent() != null && getIntent().getParcelableExtra(Params.CREATE_TEMPLATE) != null) {
      CreateTemplate createTemplate = getIntent().getParcelableExtra(Params.CREATE_TEMPLATE);
      mSelectedUri = createTemplate.uri;
      mSelectedHotspot = createTemplate.hotspot;
    } if (savedInstanceState != null) {
      // Get the saved state is being recreated.
      unpackBundle(savedInstanceState);
      mEffectChooser.setSelectedEffect(mSelectedEffectName); // TODO(clocksmith): do this somewhere else?
    }

    initReceivers();
    initServices();
  }

  @Override
  protected void onNewIntent(Intent intent) {
    SzLog.f(TAG, "onNewIntent");
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    SzLog.f(TAG, "onActivityResult(): requestCode: " + requestCode + " resultCode: " + resultCode);

    if (requestCode == RequestCodes.REQUEST_PICK_IMAGE) {
      if (resultCode == RESULT_OK) {
        handleImageSelectedFromChooser(data);
      } else {
        handleImageChooserCancelled();
      }
    } else if (requestCode == RequestCodes.REQUEST_CROP_IMAGE) {
      if (resultCode == RESULT_OK) {
        handleHotspotSelectedFromChooser(data);
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

  @Override protected void onResume() {
    super.onResume();

    if (mSelectedUri == null) {
      launchImageChooser();
    } else if (mSelectedHotspot == null) {
      launchHotspotChooser();
    } else if (mSelectedBitmapSet == null)  {
      mSelectedBitmapSet = new BitmapSet(this, mSelectedUri, MediaConstants.THUMBNAIL_SIZE_PX);
      clearAndUpdateAllGifs();
    }
    // If we still have the bitmap, then the activity was not destroyed, and nothing needs to be done in onResume.
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
  public boolean onPrepareOptionsMenu(Menu menu) {
    menu.findItem(R.id.action_add_text).setVisible(BuildFlags.ENABLED_ADD_TEXT && !mIsAddTextViewShowing);
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

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    super.onSaveInstanceState(savedInstanceState);
    packBundle(savedInstanceState);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

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
  public void on(final BuyToUnlockDialogView.OnCancelClickedEvent event) {
    mUnlockPackDialogFragment.dismiss();
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

//    mLogoView = new ImageView(this);
//    mLogoView.setPadding(0, 0, 0, 0);
//    mLogoView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.sz_logo));
//    mLogoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//    mAddTextView = new AddTextView(this);
//    getSupportActionBar().setDisplayShowCustomEnabled(false);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    mToolbarTitle.setTypeface(FontProvider.getInstance().getTitleFont());
    showAddTextView(false);

//    SpannableString ss = new SpannableString("abc");
//    Drawable d = ContextCompat.getDrawable(this, R.drawable.sz_logo);
//    d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
//    ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
//    ss.setSpan(span, 0, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);


//    getSupportActionBar().setCustomView(mAddTextView,
//        new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

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

  private void launchImageChooser() {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("image/*");
    startActivityForResult(Intent.createChooser(intent, "Select Image"), RequestCodes.REQUEST_PICK_IMAGE);
  }

  private void launchHotspotChooser() {
    Intent intent = new Intent(CreateActivity.this, HotspotChooserActivity.class);
    intent.putExtra(Intent.EXTRA_STREAM, mSelectedUri);
    startActivityForResult(intent, RequestCodes.REQUEST_CROP_IMAGE);
  }

  private void handleImageSelectedFromChooser(Intent intent) {
    mSelectedUri = intent.getData();

    mSelectedEndText = null;

    // The next step after choosing an image is always to start the hotspot chooser, but onResume will handle that,
    // as long as we invalidate any existing hotspots.
    mSelectedHotspot = null;
  }

  private void handleImageChooserCancelled() {
    // If the image chooser was cancelled and there is no current uri, hotspot, or bitmap (i.e. the image chooser
    // was not started by the image chooser button), assume the user does not want to resume this activity.
    if (mSelectedUri == null || mSelectedBitmapSet == null || mSelectedBitmapSet == null) {
      finish();
    }
  }

  private void handleHotspotSelectedFromChooser(Intent intent) {
    mSelectedHotspot = intent.getParcelableExtra(Params.HOTSPOT);

    // If the image came from an external activity, we need to capture its uri at this point.
    Uri newUri = intent.getParcelableExtra(Params.IMAGE_URI);
    if (newUri != null) {
      mSelectedUri = newUri;
    }

    // This will force onResume to init the gif creation process.
    mSelectedBitmapSet = null;
  }

  private void handleHotspotChooserCancelled() {
    // If the hotspot chooser was cancelled, invalidate the uri and assume the user wants a different image.
    if (mSelectedBitmapSet == null) {
      mSelectedUri = null;
    }
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
      SnackbarPresenter.showErrorMessage(mGifViewCoordinatorLayout, "Choose an effect first!");
      // TODO(clocksmith): tell the user they must have an effect, or just disable share button.
    } else if (mGeneratingGif) {
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
    IabHelper.getPurchasedPackNames(mBillingService, new GetPurchasedPacksCallback() {
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
    TypedValue tv = new TypedValue();
    int actionBarHeight = 0;
    if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
      actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
    }

    getSupportActionBar().setCustomView(
        show ? mAddTextView : mLogoView,
        new Toolbar.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            show ? ViewGroup.LayoutParams.MATCH_PARENT : actionBarHeight));
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

  private void showUnlockPackDialog(String effectName, String packName) {
    mUnlockPackDialogFragment = UnlockPackDialogFragment.newInstance(effectName, packName);
    mUnlockPackDialogFragment.show(getSupportFragmentManager(), UnlockPackDialogFragment.class.getSimpleName());
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
    if (mShareProgressDialog != null) {
      mShareProgressDialog.dismiss();
    }
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
      showShareProgressDialog("Preparing video...");
      final StopwatchTracker tracker = new StopwatchTracker();
      mVideoCreator = new VideoCreator(this, getMainMediaConfig(), tracker);
      mVideoCreator.createAsync(new VideoCreatorCallback() {
        @Override
        public void onCreateVideo(File videoFile) {
          if (videoFile != null) {
            Log.wtf(TAG, tracker.getReport());

            SzAnalytics.newVideoSavedEvent()
                .withItemId(mSelectedEffectName)
                .withHotspotScale(mSelectedHotspot.width())
                .withEndTextLength(mSelectedEndText == null ? 0 : mSelectedEndText.length())
                .log(CreateActivity.this);

            if (mSelectedExportType == ExportType.SHARE) {
              Intent baseIntent = new Intent(Intent.ACTION_SEND);
              baseIntent.setType(FileType.VIDEO.mime);
              Uri uri = Uri.fromFile(videoFile);
              baseIntent.putExtra(Intent.EXTRA_STREAM, uri);

              if (Build.VERSION.SDK_INT >= 22) {
                Intent receiver = new Intent(CreateActivity.this, VideoSharedReceiver.class);
                receiver.putExtra(Params.SELECTED_EFFECT_NAME, mSelectedEffectName);
                receiver.putExtra(Params.HOTSPOT_SCALE, mSelectedHotspot.width());
                receiver.putExtra(Params.END_TEXT_LENGTH, mSelectedEndText == null ? 0 : mSelectedEndText.length());
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
            .withHotspotScale(mSelectedHotspot.width())
            .withEndTextLength(mSelectedEndText == null ? 0 : mSelectedEndText.length())
            .log(this);

        return gifFile;
      } catch (IOException e) {
        SzLog.e(TAG, "cannot save gif", e);
      }
    }

    return null;
  }

  private MediaConfig getMainMediaConfig() {
    return MediaConfig.newBuilder()
        .withHotspot(mSelectedHotspot)
        .withBitmapSet(mSelectedBitmapSet)
        .withEffectTemplate(Effects.getEffectTemplate(mSelectedEffectName))
        .withEndText(mSelectedEndText)
        .withSize(MediaConstants.MAIN_SIZE_PX)
        .withFps(MediaConstants.MAIN_FPS)
        .build();
  }

  private MediaConfig getThumbnailMediaConfig(EffectTemplate effectTemplate) {
    return MediaConfig.newBuilder()
        .withHotspot(mSelectedHotspot)
        .withBitmapSet(mSelectedBitmapSet)
        .withEffectTemplate(effectTemplate)
        .withSize(MediaConstants.THUMBNAIL_SIZE_PX)
        .withFps(MediaConstants.THUMBNAIL_FPS)
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

  private void unpackBundle(Bundle bundle) {
    mSelectedUri = bundle.getParcelable(Params.SELECTED_URI);
    mSelectedHotspot = bundle.getParcelable(Params.SELECTED_HOTSPOT);
    mSelectedEffectName = bundle.getString(Params.SELECTED_EFFECT_NAME);
    mSelectedEndText = bundle.getString(Params.SELECTED_END_TEXT);
    mPurchasedPackNames = bundle.getStringArrayList(Params.PURCHASED_PACK_NAMES);
    mNeedsUpdatePurchasePackNames = bundle.getBoolean(Params.NEEDS_UPDATE_PURCHASED_PACK_NAMES);
  }

  private void packBundle(Bundle bundle) {
    bundle.putParcelable(Params.SELECTED_URI, mSelectedUri);
    bundle.putParcelable(Params.SELECTED_HOTSPOT, mSelectedHotspot);
    bundle.putString(Params.SELECTED_EFFECT_NAME, mSelectedEffectName);
    bundle.putString(Params.SELECTED_END_TEXT, mSelectedEndText);
    bundle.putStringArrayList(Params.PURCHASED_PACK_NAMES, Lists.newArrayList(mPurchasedPackNames));
    bundle.putBoolean(Params.NEEDS_UPDATE_PURCHASED_PACK_NAMES, mNeedsUpdatePurchasePackNames);
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
            receiver.putExtra(Params.SELECTED_EFFECT_NAME, mSelectedEffectName);
            receiver.putExtra(Params.HOTSPOT_SCALE, mSelectedHotspot.width());
            receiver.putExtra(Params.END_TEXT_LENGTH, mSelectedEndText == null ? 0 : mSelectedEndText.length());
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
