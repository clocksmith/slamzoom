package com.slamzoom.android.gif;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.slamzoom.android.common.providers.BusProvider;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.providers.ExecutorProvider;
import com.slamzoom.android.common.utils.PostProcessorUtils;
import com.slamzoom.android.interpolate.filter.FilterInterpolator;
import com.slamzoom.android.interpolate.filter.GPUImageZoomBlurFilter;
import com.slamzoom.android.interpolate.filter.ZoomBlurFilterInterpolator;
import com.slamzoom.android.ui.effect.EffectModel;
import com.slamzoom.android.ui.effect.EffectStep;
import com.slamzoom.android.interpolate.base.Interpolator;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSwirlFilter;

/**
 * Created by clocksmith on 3/18/16.
 */
public class GifCreator implements GifEncoder.ProgressUpdateListener {
  public static final String TAG = GifCreator.class.getSimpleName();

  @Override
  public void onProgressUpdate(double amountToUpdate) {
    BusProvider.getInstance().post(new ProgressUpdateEvent(mEffectModel, amountToUpdate));
  }

  public class ProgressUpdateEvent {
    public final EffectModel effectModel;
    public final double amountToUpdate;
    public ProgressUpdateEvent(EffectModel effectModel, double amountToUpdate) {
      this.effectModel = effectModel;
      this.amountToUpdate = amountToUpdate;
    }
  }

  protected long mStart;
  protected List<List<Frame>> mAllFrames;
  protected AtomicInteger mTotalNumFramesToAdd;
  protected int mGifWidth;
  protected int mGifHeight;
  protected int mTotalNumFrames;

  protected Bitmap mSelectedBitmap;
  protected EffectModel mEffectModel;
  protected boolean mIsFinalGif;
  protected CreateGifCallback mCallback;
  protected int mNumTilesInRow;

  public interface CreateGifCallback {
    void onCreateGif(byte[] gifBytes);
  }

  public static GifCreator newInstance(Bitmap selectedBitmap,
      EffectModel effectModel,
      int gifSize,
      boolean isFinalGif,
      CreateGifCallback callback) {
    return new GifCreator(selectedBitmap, effectModel, gifSize, isFinalGif, callback);
  }

  public GifCreator(
      Bitmap selectedBitmap,
      EffectModel effectModel,
      int gifSize,
      boolean isFinalGif,
      CreateGifCallback callback) {
    mSelectedBitmap = selectedBitmap;
    mEffectModel = effectModel;
    mIsFinalGif = isFinalGif;
    mCallback = callback;
    mNumTilesInRow = effectModel.getNumTilesInRow();

    float aspectRatio = (float) mSelectedBitmap.getWidth() / mSelectedBitmap.getHeight();
    mGifWidth = aspectRatio > 1 ? gifSize : (int) (gifSize * aspectRatio);
    mGifHeight = aspectRatio > 1 ? (int) (gifSize / aspectRatio) : gifSize;
  }

  public void createAsync() {
    Log.d(TAG, "createAsync()");
    mStart = System.currentTimeMillis();
    List<EffectStep> steps = mEffectModel.getEffectSteps();
    mTotalNumFramesToAdd = new AtomicInteger(0);
    mAllFrames = Lists.newArrayListWithCapacity(steps.size());
    for (final EffectStep step : mEffectModel.getEffectSteps()) {
      final int numFramesForChunk = (int) (Constants.DEFAULT_FPS * step.getDurationSeconds());
      List<Frame> frames = Lists.newArrayListWithCapacity(numFramesForChunk);
      for (int i = 0; i < numFramesForChunk; i++) {
        mTotalNumFramesToAdd.incrementAndGet();
        frames.add(null);
      }
      mAllFrames.add(frames);
    }
    mTotalNumFrames = mTotalNumFramesToAdd.get();

    collectFrames();
  }

  protected Frame getFrame(Matrix transformationMatrix, List<GPUImageFilter> filters, int delayMillis) {
    // Transform the selected bitmap
    Bitmap targetBitmap = Bitmap.createBitmap(
        mSelectedBitmap.getWidth(), mSelectedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(targetBitmap);
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setDither(true);
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

    // Do post processing
//    for (GPUImageFilter filter : filters) {
//      float[] values = new float[9];
//      transformationMatrix.getValues(values);
//      float dx = values[2];
//      float dy = values[5];
//      if (filter instanceof GPUImageZoomBlurFilter) {
//        ((GPUImageZoomBlurFilter) filter).setBlurCenter(
//            new PointF(dx / mSelectedBitmap.getWidth(), dy / mSelectedBitmap.getHeight()));
//      }
//    }
    Bitmap finalBitmap = PostProcessorUtils.process(scaledBitmap, filters);

    Frame frame = new Frame(finalBitmap, delayMillis);
    return frame;
  }

  protected void collectFrames() {
    List<EffectStep> steps = mEffectModel.getEffectSteps();
    EffectStep previousStep = null;

    for (int stepIndex = 0; stepIndex < steps.size(); stepIndex++) {
      final EffectStep step = steps.get(stepIndex);

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
                }
                return filter;
              }
            });

        int extraDelayMillis = 0;
        if (frameIndex == 0) {
          extraDelayMillis = (int) (1000f * step.getStartPauseSeconds());
        } else if (frameIndex == numFramesForChunk - 1) {
          extraDelayMillis = (int) (1000f * step.getEndPauseSeconds());
        }
        int delayMillis = (int) (1000f / Constants.DEFAULT_FPS) + extraDelayMillis;

        CreateFrameTask createFrameTask = new CreateFrameTask(
            stepIndex,
            frameIndex,
            delayMillis,
            scale,
            dx,
            dy,
            filters);
        createFrameTask.executeOnExecutor(ExecutorProvider.getInstance());
      }
    }
  }

  protected class CreateFrameTask extends AsyncTask<Void, Void, Frame> {
    private int mStepIndex;
    private int mFrameIndex;
    private int mDelayMillis;
    private float mScale;
    private float mDx;
    private float mDy;
    private List<GPUImageFilter> mFilters;

    CreateFrameTask(
        int stepIndex,
        int frameIndex,
        int delayMillis,
        float scale,
        float dx,
        float dy,
        List<GPUImageFilter> filters) {
      super();
      mStepIndex = stepIndex;
      mFrameIndex = frameIndex;
      mDelayMillis = delayMillis;
      mScale = scale;
      mDx = dx;
      mDy = dy;
      mFilters = filters;
    }

    @Override
    protected Frame doInBackground(Void... params) {
      Matrix transformationMatrix = new Matrix();
      transformationMatrix.postScale(mScale, mScale, mDx, mDy);

      Frame frame = getFrame(transformationMatrix, mFilters, mDelayMillis);

      if (mIsFinalGif) {
        BusProvider.getInstance().post(new ProgressUpdateEvent(mEffectModel, 1.0 / 3 / mTotalNumFrames));
      }
//      Log.d(TAG, "Finished scaling matrix and creating frame in " + (System.currentTimeMillis() - start) + "ms");
      return frame;
    }

    @Override
    protected void onPostExecute(Frame frame) {
      mAllFrames.get(mStepIndex).set(mFrameIndex, frame);
      if (mTotalNumFramesToAdd.decrementAndGet() == 0) {
        Log.wtf(TAG, "Finished collecting frames " + (System.currentTimeMillis() - mStart) + "ms");
        GifEncoder gifEncoder = new GifEncoder();

        if (mIsFinalGif) {
          gifEncoder.setProgressUpdateListener(GifCreator.this);
        }

        try {
          Iterable<Frame> flattenedFrames = Iterables.concat(mAllFrames);
          gifEncoder.addFrames(flattenedFrames);
        } catch (InvalidObjectException e) {
          Log.e(TAG, "Could not add frames", e);
        }

        try {
          gifEncoder.encodeAsync(mCallback);
        } catch (IOException e) {
          Log.e(TAG, "Could not encode frames", e);
        }
      }
    }
  }
}
