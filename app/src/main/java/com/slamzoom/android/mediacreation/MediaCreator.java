package com.slamzoom.android.mediacreation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.executor.ExecutorProvider;
import com.slamzoom.android.common.preferences.CreatorPreferences;
import com.slamzoom.android.common.utils.BitmapUtils;
import com.slamzoom.android.common.utils.DebugUtils;
import com.slamzoom.android.common.utils.PostProcessorUtils;
import com.slamzoom.android.common.SzLog;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.LinearInterpolator;
import com.slamzoom.android.mediacreation.video.VideoCreator;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 3/18/16.
 */
public abstract class MediaCreator<E extends MediaEncoder> {
  public static final String TAG = MediaCreator.class.getSimpleName();

  public static final String STOPWATCH_TRANSFORMING = "transforming";
  public static final String STOPWATCH_FILTERING = "filtering";

  protected long mStart;
  protected List<List<MediaFrame>> mAllFrames;
  protected AtomicInteger mTotalNumFramesToAdd;
  protected int mWidth;
  protected int mHeight;
  protected int mTotalNumFrames;
  protected Set<CreateFrameTask> mCreateFrameTasks = Sets.newConcurrentHashSet();
  protected E mMediaEncoder;
  protected boolean mIsCancelled;
  protected boolean mIsPreview;
  protected int mSize;
  protected int mFps;

  protected Context mContext;
  protected Bitmap mSelectedBitmap;
  protected EffectTemplate mEffectTemplate;
  protected MediaCreatorCallback mCallback;
  protected int mNumTilesInRow;
  protected MultiPhaseStopwatch mTracker;

  protected boolean mIsVideo;

  public MediaCreator(Context context, MediaConfig config, MultiPhaseStopwatch tracker) {
    mIsVideo = this instanceof VideoCreator;

    mContext = context;
    mSelectedBitmap = config.bitmap;
    mEffectTemplate = getAdjustedEffectTemplate(config);

    // TODO(clocksmith) can delete this.
    mNumTilesInRow = mEffectTemplate.getNumTilesInRow();

    int size = config.size;
    mIsPreview = size == Constants.THUMBNAIL_SIZE_PX;
    float aspectRatio = (float) mSelectedBitmap.getWidth() / mSelectedBitmap.getHeight();
    if (aspectRatio > 1) {
      mWidth = size;
      mHeight = Math.round(size / aspectRatio) / 2 * 2;
    } else {
      mWidth = Math.round(size * aspectRatio) / 2 * 2;
      mHeight = size;
    }

    mSize = size;
    mFps = config.fps;
    mTracker = tracker;
  }

  public abstract MediaFrame createFrame(Bitmap bitmap, int delayMillis, int frameIndex);

  public abstract E createEncoder();

  public void createAsync(MediaCreatorCallback callback) {
    mCallback = callback;
    mStart = System.currentTimeMillis();
    List<EffectStep> steps = mEffectTemplate.getEffectSteps();
    mTotalNumFramesToAdd = new AtomicInteger(0);
    mAllFrames = Lists.newArrayListWithCapacity(steps.size());
    for (final EffectStep step : steps) {
      final int numFramesForChunk = Math.round(mFps * step.getDurationSeconds());
      List<MediaFrame> frames = Lists.newArrayListWithCapacity(numFramesForChunk);
      for (int i = 0; i < numFramesForChunk; i++) {
        mTotalNumFramesToAdd.incrementAndGet();
        frames.add(null);
      }
      mAllFrames.add(frames);
    }
    mTotalNumFrames = mTotalNumFramesToAdd.get();

    collectFrames();
  }

  public void cancel() {
    mIsCancelled = true;
    for (CreateFrameTask task : mCreateFrameTasks) {
      task.cancel(true);
    }
    if (mMediaEncoder != null) {
      mMediaEncoder.cancel();
    }
  }

  protected MediaFrame getFrame(
      Matrix transformationMatrix,
      ImmutableList<GPUImageFilter> filters,
      int delayMillis,
      String textToRender,
      int frameIndex) {
    mTracker.start(STOPWATCH_TRANSFORMING);
    Bitmap scaledFrameBitmap = transformAndScaleSelectedBitmap(transformationMatrix);
    mTracker.stop(STOPWATCH_TRANSFORMING);

    if (DebugUtils.SAVE_SCALED_FRAMES_AS_PNGS && !mIsPreview) {
      BitmapUtils.saveBitmapToDiskAsPng(scaledFrameBitmap, "scaled_" + frameIndex);
    }

    if (mNumTilesInRow > 1) {
      scaledFrameBitmap = PostProcessorUtils.applyTiling(mNumTilesInRow, scaledFrameBitmap);
    }

    mTracker.start(STOPWATCH_FILTERING);
    Bitmap filteredFrameBitmap;
    if (filters != null && !filters.isEmpty()) {
      filteredFrameBitmap = PostProcessorUtils.applyFilters(scaledFrameBitmap, filters);
      BitmapUtils.recycleIfSupposedTo(scaledFrameBitmap);
    } else {
      filteredFrameBitmap = scaledFrameBitmap;
    }
    mTracker.stop(STOPWATCH_FILTERING);

    if (!Strings.isNullOrEmpty(textToRender)) {
      delayMillis += 1000;
      PostProcessorUtils.renderText(filteredFrameBitmap, textToRender);
    }

    if (!DebugUtils.SKIP_WATERMARK && !mIsPreview) {
      PostProcessorUtils.renderWatermark(mContext, filteredFrameBitmap);
    }

    if (DebugUtils.SAVE_FILTERED_FRAMES_AS_PNGS && !mIsPreview) {
      BitmapUtils.saveBitmapToDiskAsPng(filteredFrameBitmap, "filtered_" + frameIndex);
    }

    return createFrame(filteredFrameBitmap, delayMillis, frameIndex);
  }

  protected void collectFrames() {
    List<EffectStep> steps = mEffectTemplate.getEffectSteps();
    EffectStep previousStep = null;

    for (int stepIndex = 0; stepIndex < steps.size(); stepIndex++) {
      final EffectStep step = steps.get(stepIndex);

      // Even though in the 1 step case, the startRect top left is 0, 0 and width and height is full selected bitmap,
      // we want to make this future proof for multiple steps.
      final Rect startRect = previousStep == null ?
          new Rect(0, 0, mSelectedBitmap.getWidth(), mSelectedBitmap.getHeight()) :
          previousStep.getHotspot();
      previousStep = step;
      final Rect endRect = step.getHotspot();

      Interpolator scaleInterpolator = step.getScaleInterpolator();
      Interpolator xInterpolator = step.getXInterpolator();
      Interpolator yInterpolator = step.getYInterpolator();

      // TODO(clocksmith): This works but we need to double check for edge cases and off by 1s.
      final float px = endRect.left + endRect.left * endRect.width() / (startRect.width() - endRect.width() + 1);
      final float py = endRect.top + endRect.top * endRect.height() / (startRect.height() - endRect.height() + 1);

      final float startScale = 1;
      final float endScale = (float) startRect.height() / endRect.height();
      scaleInterpolator.setRange(startScale, endScale);

      final LinearInterpolator leftInterpolator = new LinearInterpolator(endRect.left, startRect.left);
      final LinearInterpolator topInterpolator = new LinearInterpolator(endRect.top, startRect.top);
      final LinearInterpolator rightInterpolator = new LinearInterpolator(endRect.right, startRect.right);
      final LinearInterpolator bottomInterpolator = new LinearInterpolator(endRect.bottom, startRect.bottom);

      int numFramesForChunk = mAllFrames.get(stepIndex).size();
      String textToRender = null;
      for (int frameIndex = 0; frameIndex < numFramesForChunk; frameIndex++) {
        final float t = ((float) frameIndex / (numFramesForChunk - 1));
        final float scale = scaleInterpolator.getInterpolation(t);
        SzLog.f(TAG, "scale: " + scale);
        final float intermediateWidth = startRect.width() / scale;
        final float intermediateHeight = startRect.height() / scale;
        final float dx = xInterpolator.getInterpolation(t) * intermediateWidth;
        final float dy = yInterpolator.getInterpolation(t) * intermediateHeight;

        final RectF normalizedHotspot = new RectF(0, 0, 1, 1);
        try {
          // Simple way to consume normalized scale. We could hash out a formula, but this is easier to understand.
          Interpolator normalizedScaleInterpolator = scaleInterpolator.clone();
          normalizedScaleInterpolator.setRange(0, 1);
          float normalizedScale = normalizedScaleInterpolator.getInterpolation(t);

          float endLeftFromIntermediateLeft = leftInterpolator.getInterpolation(normalizedScale);
          float endTopFromIntermediateTop = topInterpolator.getInterpolation(normalizedScale);
          float endRightFromIntermediateRight = rightInterpolator.getInterpolation(normalizedScale);
          float endBottomFromIntermediateBottom = bottomInterpolator.getInterpolation(normalizedScale);

          normalizedHotspot.set(
              endLeftFromIntermediateLeft / startRect.width(),
              endTopFromIntermediateTop / startRect.height(),
              endRightFromIntermediateRight / startRect.width(),
              endBottomFromIntermediateBottom / startRect.height());

//          Log.wtf(TAG, "frame: " + frameIndex + " normRect: " + normalizedHotspot.toShortString() +
//          " normW: " + normalizedHotspot.width() + " normH: " + normalizedHotspot.height());
        } catch (CloneNotSupportedException e) {
          SzLog.e(TAG, "unable to clone", e);
        }

        // TODO(clocksmith): extract this.
        ImmutableList<GPUImageFilter> filters = ImmutableList.copyOf(Lists.transform(step.getFilterInterpolators(),
            new Function<FilterInterpolator, GPUImageFilter>() {
              @Override
              public GPUImageFilter apply(FilterInterpolator filterInterpolator) {
                return filterInterpolator.getInterpolationFilter(t, normalizedHotspot);
              }
            }));

        int extraDelayMillis = 0;
        if (frameIndex == 0) {
          extraDelayMillis = Math.round(1000f * step.getStartPauseSeconds());
        } else if (frameIndex == numFramesForChunk - 1) {
          extraDelayMillis = Math.round(1000f * step.getEndPauseSeconds());
          textToRender = step.getEndText();
        }
        int delayMillis = Math.round(1000f / mFps) + extraDelayMillis;

        CreateFrameTask createFrameTask = new CreateFrameTask(
            stepIndex,
            frameIndex,
            delayMillis,
            scale,
            px,
            py,
            dx,
            dy,
            filters,
            textToRender);
        mCreateFrameTasks.add(createFrameTask);
        createFrameTask.executeOnExecutor(ExecutorProvider.getCollectFramesExecutor());
      }
    }
  }

  @Deprecated
  private Bitmap transformSelectedBitmap(Matrix transformationMatrix) {
    Bitmap targetBitmap = Bitmap.createBitmap(
        mSelectedBitmap.getWidth(), mSelectedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(targetBitmap);
    Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
    paint.setFilterBitmap(true);
    canvas.drawBitmap(mSelectedBitmap, transformationMatrix, paint);
    return targetBitmap;
  }

  private Bitmap transformAndScaleSelectedBitmap(Matrix transformationMatrix) {
    boolean forceSquareOutput = mIsVideo && !DebugUtils.ALLOW_NON_SQUARE_OUTPUT_VIDEO;

    Bitmap targetBitmap;
    if (forceSquareOutput) {
      targetBitmap = Bitmap.createBitmap(mSize, mSize, Bitmap.Config.ARGB_8888);
    } else {
      targetBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
    }

    Canvas canvas = new Canvas(targetBitmap);
    Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    transformationMatrix.postScale(
        (float) mWidth / mSelectedBitmap.getWidth(),
        (float) mHeight / mSelectedBitmap.getHeight(),
        0,
        0);

    if (forceSquareOutput) {
      transformationMatrix.postTranslate(
          mWidth < mSize ? (mSize - mWidth) / 2 : 0,
          mHeight < mSize ? (mSize - mHeight) / 2 : 0
      );
    }
    canvas.drawBitmap(mSelectedBitmap, transformationMatrix, paint);

    return targetBitmap;
  }

  protected class CreateFrameTask extends AsyncTask<Void, Void, MediaFrame> {
    protected int mStepIndex;
    protected int mFrameIndex;
    protected int mDelayMillis;
    protected float mScale;
    protected float mPx;
    protected float mPy;
    protected float mDx;
    protected float mDy;
    protected ImmutableList<GPUImageFilter> mFilters;
    protected String mTextToRender;

    CreateFrameTask(
        int stepIndex,
        int frameIndex,
        int delayMillis,
        float scale,
        float px,
        float py,
        float dx,
        float dy,
        ImmutableList<GPUImageFilter> filters,
        String textToRender) {
      super();
      mStepIndex = stepIndex;
      mFrameIndex = frameIndex;
      mDelayMillis = delayMillis;
      mScale = scale;
      mPx = px;
      mPy = py;
      mDx = dx;
      mDy = dy;
      mFilters = filters;
      mTextToRender = textToRender;
    }

    @Override
    protected MediaFrame doInBackground(Void... params) {
      Matrix transformationMatrix = new Matrix();
      transformationMatrix.postScale(mScale, mScale, mPx, mPy);
      transformationMatrix.postTranslate(mDx, mDy);
      return getFrame(transformationMatrix, mFilters, mDelayMillis, mTextToRender, mFrameIndex);
    }

    @Override
    protected void onPostExecute(MediaFrame frame) {
      if (!mIsCancelled) {
        mAllFrames.get(mStepIndex).set(mFrameIndex, frame);
        if (mTotalNumFramesToAdd.decrementAndGet() == 0) {
//          Log.wtf(TAG, "Finished collecting frames " + (System.currentTimeMillis() - mStart) + "ms");
          mMediaEncoder = createEncoder();

          List<MediaFrame> concatedFrames = Lists.newArrayList(Iterables.concat(mAllFrames));

          if (concatedFrames.size() > 1 &&
              (DebugUtils.REVERSE_LOOP_EFFECTS || CreatorPreferences.isCycle(mContext))) {
            concatedFrames.get(0).delayMillis = concatedFrames.get(1).delayMillis;
            concatedFrames.get(concatedFrames.size() - 1).delayMillis =
                concatedFrames.get(concatedFrames.size() - 2).delayMillis;
            concatedFrames.addAll(Lists.reverse(concatedFrames.subList(1, concatedFrames.size() - 1)));
          }

          mMediaEncoder.addFrames(concatedFrames);
          mMediaEncoder.encodeAsync(mCallback);
        }
      }
    }
  }

  private static EffectTemplate getAdjustedEffectTemplate(MediaConfig mediaConfig) {
    // TODO(clocksmith): This is weird. Not yet sure how else to do this.
    EffectTemplate effectTemplate = mediaConfig.effectTemplate;
    for (EffectStep step : effectTemplate.getEffectSteps()) {
      step.setHotspot(mediaConfig.hotspot);
      if (mediaConfig.endText != null) {
        step.setEndText(mediaConfig.endText);
      }
    }
    return effectTemplate;
  }
}
