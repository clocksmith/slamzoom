package com.slamzoom.android.gif;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.slamzoom.android.common.BusProvider;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.ExecutorProvider;
import com.slamzoom.android.effect.EffectModel;
import com.slamzoom.android.effect.EffectStep;
import com.slamzoom.android.interpolate.base.Interpolator;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

  public interface CreateGifCallback {
    void onCreateGif(byte[] gifBytes);
  }

  public static GifCreator newInstance(Bitmap selectedBitmap,
      EffectModel effectModel,
      int gifSize,
      boolean isFinalGif,
      CreateGifCallback callback) {
    if (effectModel.getNumTilesInRow() > 1) {
      return new StaticTiledGifCreator(selectedBitmap, effectModel, gifSize, isFinalGif, callback);
    } else {
      return new GifCreator(selectedBitmap, effectModel, gifSize, isFinalGif, callback);
    }
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

  protected Frame getFrame(Matrix matrix, int delayMillis) {
    Bitmap targetBitmap = Bitmap.createBitmap(
        mSelectedBitmap.getWidth(), mSelectedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(targetBitmap);
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setDither(true);
    canvas.drawBitmap(mSelectedBitmap, matrix, paint);
    Bitmap scaledBitmap = Bitmap.createScaledBitmap(targetBitmap, mGifWidth, mGifHeight, true);
    Frame frame = new Frame(scaledBitmap, delayMillis);
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
      Rect endRect = step.getHotspot();

      Interpolator scaleInterpolator = step.getScaleInterpolatorProvider().getScaleInterpolator();
      Interpolator xInterpolator = step.getTranslateInterpolatorProvider().getXInterpolator();
      Interpolator yInterpolator = step.getTranslateInterpolatorProvider().getYInterpolator();

      float pivotX = endRect.left + endRect.left * endRect.width() / (startRect.width() - endRect.width());
      float pivotY = endRect.top + endRect.top * endRect.height() / (startRect.height() - endRect.height());

      float startScale = 1;
      float endScale = (float) startRect.height() / endRect.height();
      scaleInterpolator.setDomain(startScale, endScale);

      int numFramesForChunk = mAllFrames.get(stepIndex).size();
      for (int frameIndex = 0; frameIndex < numFramesForChunk; frameIndex++) {
        float percent = ((float) frameIndex / (numFramesForChunk - 1));
        float scale = scaleInterpolator.getInterpolation(percent);
        float dx = pivotX;
        float dy = pivotY;
        if (xInterpolator != null && yInterpolator != null) {
          dx += xInterpolator.getInterpolation(percent) * startRect.width() / scale;
          dy += yInterpolator.getInterpolation(percent) * startRect.width() / scale;
        }

        int extraDelayMillis = 0;
        if (frameIndex == 0) {
          extraDelayMillis = (int) (1000f * step.getStartPauseSeconds());
        } else if (frameIndex == numFramesForChunk - 1) {
          extraDelayMillis = (int) (1000f * step.getEndPauseSeconds());
        }
        int delayMillis = (int) (1000f / Constants.DEFAULT_FPS) + extraDelayMillis;

        CreateFrameTask createFrameTask = new CreateFrameTask(stepIndex, frameIndex, delayMillis, scale, dx, dy);
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

    CreateFrameTask(int stepIndex, int frameIndex, int delayMillis, float scale, float dx, float dy) {
      super();
      mStepIndex = stepIndex;
      mFrameIndex = frameIndex;
      mDelayMillis = delayMillis;
      mScale = scale;
      mDx = dx;
      mDy = dy;
    }

    @Override
    protected Frame doInBackground(Void... params) {
      final Matrix matrix = new Matrix();
      matrix.postScale(mScale, mScale, mDx, mDy);

      Frame frame = getFrame(matrix, mDelayMillis);

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
