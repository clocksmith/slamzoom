package com.slamzoom.android.mediacreation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.util.Log;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.slamzoom.android.effects.interpolation.filter.base.FilterInterpolator;
import com.slamzoom.android.global.Constants;
import com.slamzoom.android.global.singletons.ExecutorProvider;
import com.slamzoom.android.global.utils.DebugUtils;
import com.slamzoom.android.global.utils.PostProcessorUtils;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.interpolators.base.Interpolator;
import com.slamzoom.android.interpolators.effect.IdentityInterpolator;
import com.slamzoom.android.ui.create.effectchooser.EffectModel;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 3/18/16.
 */
public abstract class MediaCreator<E extends MediaEncoder> {
  public static final String TAG = MediaCreator.class.getSimpleName();

  protected long mStart;
  protected List<List<MediaFrame>> mAllFrames;
  protected AtomicInteger mTotalNumFramesToAdd;
  protected int mGifWidth;
  protected int mGifHeight;
  protected int mTotalNumFrames;

  protected Context mContext;
  protected Bitmap mSelectedBitmap;
  protected EffectModel mEffectModel;
  protected CreateMediaCallback mCallback;
  protected int mNumTilesInRow;

  public MediaCreator(
      Context context,
      Bitmap selectedBitmap,
      EffectModel effectModel,
      int gifSize,
      CreateMediaCallback callback) {
    mContext = context;
    mSelectedBitmap = selectedBitmap;
    mEffectModel = effectModel;
    mCallback = callback;
    mNumTilesInRow = mEffectModel.getEffectTemplate().getNumTilesInRow();

    float aspectRatio = (float) mSelectedBitmap.getWidth() / mSelectedBitmap.getHeight();
    mGifWidth = aspectRatio > 1 ? gifSize : Math.round(gifSize * aspectRatio);
    mGifHeight = aspectRatio > 1 ? Math.round(gifSize / aspectRatio) : gifSize;
  }

  public abstract MediaFrame createFrame(Bitmap bitmap, int delayMillis);

  public abstract E createEncoder();

  public void createAsync() {
    Log.d(TAG, "createAsync()");
    mStart = System.currentTimeMillis();
    List<EffectStep> steps = mEffectModel.getEffectTemplate().getEffectSteps();
    mTotalNumFramesToAdd = new AtomicInteger(0);
    mAllFrames = Lists.newArrayListWithCapacity(steps.size());
    for (final EffectStep step : steps) {
      final int numFramesForChunk = Math.round(Constants.DEFAULT_FPS * step.getDurationSeconds());
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

  protected MediaFrame getFrame(
      Matrix transformationMatrix,
      List<GPUImageFilter> filters,
      int delayMillis,
      String textToRender,
      int frameIndex) {
    Bitmap finalBitmap = getTransformedAndScaledBitmap(transformationMatrix);

    if (mNumTilesInRow > 1) {
      finalBitmap = PostProcessorUtils.applyTiling(mNumTilesInRow, finalBitmap);
    }

    if (!filters.isEmpty()) {
      finalBitmap = PostProcessorUtils.applyFilters(mContext, finalBitmap, filters);
    }

    if (!Strings.isNullOrEmpty(textToRender)) {
      delayMillis += 1000;
      PostProcessorUtils.renderText(finalBitmap, textToRender);
    }

    if (Constants.USE_WATERMARK) {
      PostProcessorUtils.renderwWatermark(mContext, finalBitmap);
    }

    if (Constants.SAVE_INDIVIDUAL_FRAMES_AS_BITMAPS) {
      DebugUtils.saveFrameAsBitmap(finalBitmap, frameIndex);
    }

    return createFrame(finalBitmap, delayMillis);
  }

  protected void collectFrames() {
    List<EffectStep> steps = mEffectModel.getEffectTemplate().getEffectSteps();
    EffectStep previousStep = null;

    for (int stepIndex = 0; stepIndex < steps.size(); stepIndex++) {
      final EffectStep step = steps.get(stepIndex);
      String textToRender = null;

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
      float pivotX = endRect.left + endRect.left * endRect.width() / (startRect.width() - endRect.width() + 1);
      float pivotY = endRect.top + endRect.top * endRect.height() / (startRect.height() - endRect.height() + 1);

      final float startScale = 1;
      final float endScale = (float) startRect.height() / endRect.height();
      scaleInterpolator.setDomain(startScale, endScale);

      final IdentityInterpolator leftInterpolator = new IdentityInterpolator(endRect.left, startRect.left);
      final IdentityInterpolator topInterpolator = new IdentityInterpolator(endRect.top, startRect.top);
      final IdentityInterpolator rightInterpolator = new IdentityInterpolator(endRect.right, startRect.right);
      final IdentityInterpolator bottomInterpolator = new IdentityInterpolator(endRect.bottom, startRect.bottom);

      int numFramesForChunk = mAllFrames.get(stepIndex).size();
      for (int frameIndex = 0; frameIndex < numFramesForChunk; frameIndex++) {
        final float percent = ((float) frameIndex / (numFramesForChunk - 1));
        final float scale = scaleInterpolator.getInterpolation(percent);
        final float intermediateWidth = startRect.width() / scale;
        final float intermediateHeight = startRect.height() / scale;
        final float dx = pivotX + xInterpolator.getInterpolation(percent) * intermediateWidth;
        final float dy = pivotY + yInterpolator.getInterpolation(percent) * intermediateHeight;

        // TODO(clocksmith): extract this.
        List<GPUImageFilter> filters = Lists.transform(step.getFilterInterpolators(),
            new Function<FilterInterpolator, GPUImageFilter>() {
              @Override
              public GPUImageFilter apply(FilterInterpolator filterInterpolator) {
                // 0 is original/start, 1 is
                float interpolationValue = filterInterpolator.getInterpolator().getInterpolation(percent);
//                float normalizedScale = scale == endScale ? startScale : startScale * (scale - 1) / (endScale - 1);

                float endLeftFromIntermediateLeft = leftInterpolator.getInterpolation(interpolationValue);
                float endTopFromIntermediateTop = topInterpolator.getInterpolation(interpolationValue);
                float endRightFromIntermediateRight = rightInterpolator.getInterpolation(interpolationValue);
                float endBottomFromIntermediateBottom = bottomInterpolator.getInterpolation(interpolationValue);


                RectF normalizedHotspot = new RectF(
                    endLeftFromIntermediateLeft / startRect.width(),
                    endTopFromIntermediateTop / startRect.height(),
                    endRightFromIntermediateRight / startRect.width(),
                    endBottomFromIntermediateBottom / startRect.height());

                return filterInterpolator.getInterpolationFilter(percent, normalizedHotspot);
              }
            });

        int extraDelayMillis = 0;
        if (frameIndex == 0) {
          extraDelayMillis = Math.round(1000f * step.getStartPauseSeconds());
        } else if (frameIndex == numFramesForChunk - 1) {
          extraDelayMillis = Math.round(1000f * step.getEndPauseSeconds());
          textToRender = step.getEndText();
        }
        int delayMillis = Math.round(1000f / Constants.DEFAULT_FPS) + extraDelayMillis;

        CreateFrameTask createFrameTask = new CreateFrameTask(
            stepIndex,
            frameIndex,
            delayMillis,
            scale,
            dx,
            dy,
            filters,
            textToRender);
        createFrameTask.executeOnExecutor(ExecutorProvider.getInstance());
      }
    }
  }

  private Bitmap getTransformedAndScaledBitmap(Matrix transformationMatrix) {
    return Bitmap.createScaledBitmap(
        transformSelectedBitmap(transformationMatrix), mGifWidth, mGifHeight, true);
  }

  private Bitmap transformSelectedBitmap(Matrix transformationMatrix) {
    Bitmap targetBitmap = Bitmap.createBitmap(
        mSelectedBitmap.getWidth(), mSelectedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(targetBitmap);
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    canvas.drawBitmap(mSelectedBitmap, transformationMatrix, paint);
    return targetBitmap;
  }

  protected class CreateFrameTask extends AsyncTask<Void, Void, MediaFrame> {
    protected int mStepIndex;
    protected int mFrameIndex;
    protected int mDelayMillis;
    protected float mScale;
    protected float mDx;
    protected float mDy;
    protected List<GPUImageFilter> mFilters;
    protected String mTextToRender;

    CreateFrameTask(
        int stepIndex,
        int frameIndex,
        int delayMillis,
        float scale,
        float dx,
        float dy,
        List<GPUImageFilter> filters,
        String textToRender) {
      super();
      mStepIndex = stepIndex;
      mFrameIndex = frameIndex;
      mDelayMillis = delayMillis;
      mScale = scale;
      mDx = dx;
      mDy = dy;
      mFilters = filters;
      mTextToRender = textToRender;
    }

    @Override
    protected MediaFrame doInBackground(Void... params) {
      Matrix transformationMatrix = new Matrix();
      transformationMatrix.postScale(mScale, mScale, mDx, mDy);
      return getFrame(transformationMatrix, mFilters, mDelayMillis, mTextToRender, mFrameIndex);
    }

    @Override
    protected void onPostExecute(MediaFrame frame) {
      mAllFrames.get(mStepIndex).set(mFrameIndex, frame);
      if (mTotalNumFramesToAdd.decrementAndGet() == 0) {
        Log.wtf(TAG, "Finished collecting frames " + (System.currentTimeMillis() - mStart) + "ms");
        E mediaEncoder = createEncoder();
        mediaEncoder.addFrames(Iterables.concat(mAllFrames));
        mediaEncoder.encodeAsync(mCallback);
      }
    }
  }
}
