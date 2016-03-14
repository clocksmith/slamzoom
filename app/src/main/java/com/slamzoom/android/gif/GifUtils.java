package com.slamzoom.android.gif;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.effect.EffectModel;
import com.slamzoom.android.effect.EffectStep;
import com.slamzoom.android.gif.encoder.GifEncoder;
import com.slamzoom.android.gif.encoder.GifEncoderOld;
import com.slamzoom.android.interpolate.base.SingleOutputInterpolator;
import com.slamzoom.android.interpolate.base.MultiOutputInterpolator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by clocksmith on 3/7/16.
 */
public class GifUtils {
  private static final String TAG = GifUtils.class.getSimpleName();

  public interface CreateGifCallback {
    void onCreateGif(byte[] gifBytes);
  }

  public static void createGif(
      Bitmap selectedBitmap,
      EffectModel effectModel,
      int gifSize,
      boolean important,
      CreateGifCallback callback) {
    Log.d(TAG, "createGifAsync()");
    CreateGifTask createGifTask = new CreateGifTask(selectedBitmap, effectModel, gifSize, important, callback);
    createGifTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
  }

  private static byte[] createGifSync(final Bitmap selectedBitmap, EffectModel effectModel, int gifSize) {
    List<List<Bitmap>> allFrames = getAllFrames(selectedBitmap, effectModel, gifSize);
    try {
//      byte[] gifOld = encodeGifOld(effectModel, allFrames);
      byte[] gif = encodeGif(effectModel, allFrames);

      return gif;
    } catch (IOException e) {
      Log.e(TAG, "Could not encode gif", e);
      return null;
    }
  }

  private static List<List<Bitmap>> getAllFrames(
      final Bitmap selectedBitmap,
      EffectModel effectModel,
      final int gifSize) {
    Log.d(TAG, "start getAllFrames()");
    long start = System.currentTimeMillis();

    List<FutureTask<List<Bitmap>>> getFramesFutureTasks = Lists.newArrayList();
    EffectStep previousStep = null;
    for (final EffectStep step : effectModel.getEffectSteps()) {
      final Rect startRect = previousStep == null ?
          new Rect(0, 0, selectedBitmap.getWidth(), selectedBitmap.getHeight()) :
          previousStep.getHotspot();
      getFramesFutureTasks.add(new FutureTask<>(new Callable<List<Bitmap>>() {
        @Override
        public List<Bitmap> call() throws Exception {
          return getFrames(selectedBitmap, startRect, step, gifSize);
        }
      }));
      previousStep = step;
    }

    ExecutorService executor = Executors.newFixedThreadPool(Constants.DEFAULT_THREAD_POOL_SIZE);
    for (FutureTask<List<Bitmap>> futureTask : getFramesFutureTasks) {
      executor.execute(futureTask);
    }

    List<List<Bitmap>> allFrames = FluentIterable.from(getFramesFutureTasks)
        .transform(new Function<FutureTask<List<Bitmap>>, List<Bitmap>>() {
          @Override
          public List<Bitmap> apply(FutureTask<List<Bitmap>> input) {
            try {
              return input.get();
            } catch (ExecutionException | InterruptedException e) {
              Log.e(TAG, "Could not get frames!", e);
              return null;
            }
          }
        }).toList();

    Log.d(TAG, "end getAllFrames(). duration: " + ((System.currentTimeMillis() - start)) + "ms");
    return allFrames;
  }

  private static byte[] encodeGif(EffectModel effectModel, List<List<Bitmap>> allFrames)
      throws IOException, InvalidObjectException {
    GifEncoder gifEncoder = new GifEncoder();

    int stepIndex = 0;
    for (final EffectStep step : effectModel.getEffectSteps()) {
      List<Bitmap> stepFrames = allFrames.get(stepIndex);

      Log.d(TAG, "step.getStartPauseSeconds(): " + step.getStartPauseSeconds());
      gifEncoder.addFrame(stepFrames.get(0), (int) step.getStartPauseSeconds() * 1000);

      if (stepFrames.size() > 2) {
        for (int frameIndex = 1; frameIndex < stepFrames.size() - 1; frameIndex++) {
          gifEncoder.addFrame(stepFrames.get(frameIndex));
        }
      }

      gifEncoder.addFrame(stepFrames.get(stepFrames.size() - 1), (int) step.getEndPauseSeconds() * 1000);
      Log.d(TAG, "step.getEndPauseSeconds(): " + step.getEndPauseSeconds());

      stepIndex++;
    }

    return gifEncoder.encode();
  }

  private static byte[] encodeGifOld(EffectModel effectModel, List<List<Bitmap>> allFrames)
      throws IOException, InvalidObjectException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    GifEncoderOld gifEncoderOld = new GifEncoderOld();
    gifEncoderOld.start(out);
    Bitmap firstFrame = allFrames.get(0).get(0);
    gifEncoderOld.setSize(firstFrame.getWidth(), firstFrame.getHeight());
    gifEncoderOld.setQuality(20);
    gifEncoderOld.setRepeat(0);

    int fps = 15;
    int delay = Math.round(1000f / fps);
    int stepIndex = 0;
    for (final EffectStep step : effectModel.getEffectSteps()) {
      List<Bitmap> stepFrames = allFrames.get(stepIndex);

      gifEncoderOld.setDelay(delay + (int) step.getStartPauseSeconds() * 1000);
      gifEncoderOld.addFrame(stepFrames.get(0));

      if (stepFrames.size() > 2) {
        for (int frameIndex = 1; frameIndex < stepFrames.size() - 1; frameIndex++) {
          gifEncoderOld.setDelay(delay);
          gifEncoderOld.addFrame(stepFrames.get(frameIndex));
        }
      }

      gifEncoderOld.setDelay(delay + (int) step.getEndPauseSeconds() * 1000);
      gifEncoderOld.addFrame(stepFrames.get(stepFrames.size() - 1));

      stepIndex++;
    }

    return out.toByteArray();
  }


  private static List<Bitmap> getFrames(Bitmap selectedBitmap, Rect startRect, EffectStep step, int gifSize) {
    Log.d(TAG, "start getFrames()");
    long start = System.currentTimeMillis();

    Rect endRect = step.getHotspot();
    SingleOutputInterpolator scaleInterpolator = step.getScaleInterpolator();
    MultiOutputInterpolator translateInterpolator = step.getTranslateInterpolator();

    float aspectRatio = (float) selectedBitmap.getWidth() / selectedBitmap.getHeight();
    final int gifWidth = aspectRatio > 1 ? gifSize : (int) (gifSize * aspectRatio);
    final int gifHeight = aspectRatio > 1 ? (int) (gifSize / aspectRatio) : gifSize;

    float dx = endRect.left + endRect.left * endRect.width() / (startRect.width() - endRect.width());
    float dy = endRect.top + endRect.top * endRect.height() / (startRect.height() - endRect.height());

    float startScale = 1;
    float endScale = (float) startRect.height() / endRect.height();
    scaleInterpolator.setDomain(startScale, endScale);

    List<Bitmap> frames = Lists.newArrayList();
    int numFrames = (int) (GifEncoder.DEFAULT_FPS * step.getDurationSeconds());
    for (int i = 0; i < numFrames; i++) {
      float percent = ((float) i / (numFrames - 1));
      float scale = (float) scaleInterpolator.getInterpolation(percent);
      float x = 0;
      float y = 0;
      if (translateInterpolator != null) {
        double[] translate = translateInterpolator.getInterpolation(percent);
        x = (float) translate[0] * startRect.width() / scale / 16;
        y = (float) translate[1] * startRect.height() / scale / 16;
      }

      Matrix matrix = new Matrix();
      matrix.postScale(scale, scale, dx + x , dy + y);
//      matrix.postTranslate(50 * x1, 50 * x2);

      Bitmap targetBitmap = Bitmap.createBitmap(
          selectedBitmap.getWidth(), selectedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
      Canvas canvas = new Canvas(targetBitmap);
      Paint paint = new Paint();
      paint.setAntiAlias(true);
      paint.setDither(true);
      canvas.drawBitmap(selectedBitmap, matrix, paint);
      Bitmap scaledBitmap = Bitmap.createScaledBitmap(targetBitmap, gifWidth, gifHeight, true);
      frames.add(scaledBitmap);
    }

    Log.d(TAG, "end getFrames(). duration: " + ((System.currentTimeMillis() - start)) + "ms");
    return frames;
  }

  public static class CreateGifTask extends AsyncTask<Void, Void, byte[]> {
    private Bitmap mSelectedBitmap;
    private EffectModel mEffectModel;
    private int mGifSize;
    private boolean mImportant;
    private CreateGifCallback mCallback;

    CreateGifTask(Bitmap selectedBitmap, EffectModel effectModel, int gifSize, boolean important, CreateGifCallback callback) {
      super();
      mSelectedBitmap = selectedBitmap;
      mEffectModel = effectModel;
      mGifSize = gifSize;
      mImportant = important;
      mCallback = callback;
    }

    @Override
    protected byte[] doInBackground(Void... params) {
      if (mImportant) {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
      }
      return createGifSync(mSelectedBitmap, mEffectModel, mGifSize);
    }

    @Override
    protected void onPostExecute(byte[] gifBytes) {
      mCallback.onCreateGif(gifBytes);
    }
  }
}
