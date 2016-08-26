package com.slamzoom.android.mediacreation.video;

import android.os.AsyncTask;

import com.google.common.collect.Lists;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.Files;
import com.slamzoom.android.common.utils.SzLog;
import com.slamzoom.android.mediacreation.MediaCreatorCallback;
import com.slamzoom.android.mediacreation.MediaEncoder;

import org.jcodec.api.SequenceEncoder;
import org.jcodec.api.SequenceEncoder8Bit;
import org.jcodec.api.android.AndroidSequenceEncoder8Bit;
import org.jcodec.containers.mxf.model.Sequence;

import java.io.File;
import java.io.IOException;

/**
 * Created by clocksmith on 8/25/16.
 *
 * TODO(clocksmith) refactor GifEncoder and MediaEncoder.
 */
public class VideoEncoder extends MediaEncoder<VideoFrame> {
  private static final String TAG = VideoEncoder.class.getSimpleName();

  @Override
  public void encodeAsync(MediaCreatorCallback callback) {
    super.encodeAsync(callback);
    EncodeVideoTask encodeVideoTask = new EncodeVideoTask();
    mTasks.add(encodeVideoTask);
    encodeVideoTask.execute();
  }

  private class EncodeVideoTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... params) {
      File videoFile = Files.makeFile(Files.FileType.VIDEO, "video");

      try {
        AndroidSequenceEncoder8Bit encoder = AndroidSequenceEncoder8Bit.create24Fps(videoFile);
        for (VideoFrame frame : mFrames) {
          int baseDelayMillis = (int) (1000.0 / Constants.MAIN_FPS);
          int numFrames = frame.delayMillis / baseDelayMillis;
          for (int i = 0; i < numFrames; i++) {
            encoder.encodeImage(frame.bitmap);
          }
        }
        encoder.finish();
      } catch (IOException e) {
        SzLog.e(TAG,  "Cannot encode video.", e);
      }

      mCallback.onCreateVideo(videoFile);
      return null;
    }
  }
}
