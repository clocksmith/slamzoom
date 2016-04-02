package com.slamzoom.android.mediacreation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.slamzoom.android.global.Constants;
import com.slamzoom.android.global.singletons.ExecutorProvider;
import com.slamzoom.android.global.utils.PostProcessorUtils;
import com.slamzoom.android.interpolaters.filter.FilterInterpolator;
import com.slamzoom.android.interpolaters.filter.GPUImageZoomBlurFilter;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.interpolaters.base.Interpolator;
import com.slamzoom.android.ui.main.effectchooser.EffectModel;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSwirlFilter;

/**
 * Created by clocksmith on 3/18/16.
 */
public abstract class MediaCreator {
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
  protected boolean mIsFinalGif;
  protected CreateMediaCallback mCallback;
  protected int mNumTilesInRow;

  public MediaCreator(
      Context context,
      Bitmap selectedBitmap,
      EffectModel effectModel,
      int gifSize,
      boolean isFinalGif,
      CreateMediaCallback callback) {
    mContext = context;
    mSelectedBitmap = selectedBitmap;
    mEffectModel = effectModel;
    mIsFinalGif = isFinalGif;
    mCallback = callback;
    mNumTilesInRow = mEffectModel.getEffectTemplate().getNumTilesInRow();

    float aspectRatio = (float) mSelectedBitmap.getWidth() / mSelectedBitmap.getHeight();
    mGifWidth = aspectRatio > 1 ? gifSize : Math.round(gifSize * aspectRatio);
    mGifHeight = aspectRatio > 1 ? Math.round(gifSize / aspectRatio) : gifSize;
  }

  public abstract MediaFrame createFrame(Bitmap bitmap, int delayMillis);

  public abstract MediaEncoder createEncoder();

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
      Matrix transformationMatrix, List<GPUImageFilter> filters, int delayMillis, String textToRender) {
    // Transform the selected bitmap
    Bitmap targetBitmap = Bitmap.createBitmap(
        mSelectedBitmap.getWidth(), mSelectedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(targetBitmap);
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    canvas.drawBitmap(mSelectedBitmap, transformationMatrix, paint);

    if (mNumTilesInRow > 1) {
      // Write subframes into new bitmap
      int tileWidth = targetBitmap.getWidth() / mNumTilesInRow;
      int tileHeight = targetBitmap.getHeight() / mNumTilesInRow;
      Bitmap bitmapTile = Bitmap.createScaledBitmap(targetBitmap, tileWidth, tileHeight, true);
      Bitmap tiledTargetBitmap = Bitmap.createBitmap(
          targetBitmap.getWidth(), targetBitmap.getHeight(), Bitmap.Config.ARGB_8888);
      Canvas tiledCanvas = new Canvas(tiledTargetBitmap);
      for (int i = 0; i < mNumTilesInRow; i++) {
        for (int j = 0; j < mNumTilesInRow; j++) {
          tiledCanvas.drawBitmap(bitmapTile, i * tileWidth, j * tileHeight, paint);
        }
      }
      targetBitmap = tiledTargetBitmap;
    }

    Bitmap scaledBitmap = Bitmap.createScaledBitmap(targetBitmap, mGifWidth, mGifHeight, true);

    Bitmap finalBitmap = PostProcessorUtils.process(mContext, scaledBitmap, filters);

    if (!Strings.isNullOrEmpty(textToRender)) {
      delayMillis += 1000;
      Canvas textCanvas = new Canvas(finalBitmap);
      Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
      textPaint.setColor(Color.WHITE); // Text Color
      textPaint.setTextSize(getCorrectedWidth(textToRender, finalBitmap.getWidth(), 3 * finalBitmap.getWidth() / 4));
      textPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
      textPaint.setTextAlign(Paint.Align.CENTER);
      textCanvas.drawText(textToRender, finalBitmap.getWidth() / 2, finalBitmap.getHeight() / 2, textPaint);
    }

    return createFrame(finalBitmap, delayMillis);
  }

  private int getCorrectedWidth(String text, int textSize, int desiredWidth) {
    Paint paint = new Paint();
    Rect bounds = new Rect();

    paint.setTextSize(textSize);
    paint.getTextBounds(text, 0, text.length(), bounds);

    while (bounds.width() > desiredWidth) {
      textSize--;
      paint.setTextSize(textSize);
      paint.getTextBounds(text, 0, text.length(), bounds);
    }

    return textSize;
  }

  protected void collectFrames() {
    List<EffectStep> steps = mEffectModel.getEffectTemplate().getEffectSteps();
    EffectStep previousStep = null;

    for (int stepIndex = 0; stepIndex < steps.size(); stepIndex++) {
      final EffectStep step = steps.get(stepIndex);
      String textToRender = null;

      final Rect startRect = previousStep == null ?
          new Rect(0, 0, mSelectedBitmap.getWidth(), mSelectedBitmap.getHeight()) :
          previousStep.getHotspot();
      previousStep = step;
      final Rect endRect = step.getHotspot();

      Interpolator scaleInterpolator = step.getScaleInterpolator();
      Interpolator xInterpolator = step.getXInterpolator();
      Interpolator yInterpolator = step.getYInterpolator();

      float pivotX = endRect.left + endRect.left * endRect.width() / (startRect.width() - endRect.width());
      float pivotY = endRect.top + endRect.top * endRect.height() / (startRect.height() - endRect.height());

      float startScale = 1;
      final float endScale = (float) startRect.height() / endRect.height();
      scaleInterpolator.setDomain(startScale, endScale);

      int numFramesForChunk = mAllFrames.get(stepIndex).size();
      for (int frameIndex = 0; frameIndex < numFramesForChunk; frameIndex++) {
        final float percent = ((float) frameIndex / (numFramesForChunk - 1));
        final float scale = scaleInterpolator.getInterpolation(percent);
        final float dx = pivotX + xInterpolator.getInterpolation(percent) * startRect.width() / scale;
        final float dy = pivotY + yInterpolator.getInterpolation(percent) * startRect.width() / scale;

        List<GPUImageFilter> filters = Lists.transform(step.getFilterInterpolators(),
            new Function<FilterInterpolator, GPUImageFilter>() {
              @Override
              public GPUImageFilter apply(FilterInterpolator filterInterpolator) {
                GPUImageFilter filter = filterInterpolator.getInterpolationFilter(percent);
                float c1 = (scale - 1) / endScale;
                float c2 = 1 - c1;
                PointF effectCenter = new PointF(
                    c1 * 0.5f + c2 * endRect.centerX() / mSelectedBitmap.getWidth(),
                    c1 * 0.5f + c2 * endRect.centerY() / mSelectedBitmap.getHeight());
                if (filter instanceof GPUImageZoomBlurFilter) {
                  ((GPUImageZoomBlurFilter) filter).setBlurCenter(effectCenter);
                }  else if (filter instanceof GPUImageSwirlFilter) {
                  ((GPUImageSwirlFilter) filter).setCenter(effectCenter);
                } else if (filter instanceof GPUImageBulgeDistortionFilter) {
                  ((GPUImageBulgeDistortionFilter) filter).setCenter(effectCenter);
                }
                return filter;
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
      return getFrame(transformationMatrix, mFilters, mDelayMillis, mTextToRender);

    }

    @Override
    protected void onPostExecute(MediaFrame frame) {
      mAllFrames.get(mStepIndex).set(mFrameIndex, frame);
      if (mTotalNumFramesToAdd.decrementAndGet() == 0) {
        Log.wtf(TAG, "Finished collecting frames " + (System.currentTimeMillis() - mStart) + "ms");
        MediaEncoder mediaEncoder = createEncoder();
        mediaEncoder.addFrames(Iterables.concat(mAllFrames));
        mediaEncoder.encodeAsync(mCallback);
      }
    }
  }
}
