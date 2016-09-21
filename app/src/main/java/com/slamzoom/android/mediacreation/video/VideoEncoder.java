package com.slamzoom.android.mediacreation.video;

import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.slamzoom.android.SzApp;
import com.slamzoom.android.common.Constants;
import com.slamzoom.android.common.files.FileType;
import com.slamzoom.android.common.logging.SzLog;
import com.slamzoom.android.common.files.FileUtils;
import com.slamzoom.android.mediacreation.MediaCreatorCallback;
import com.slamzoom.android.mediacreation.MediaEncoder;
import com.slamzoom.android.mediacreation.StopwatchTracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by clocksmith on 8/25/16.
 *
 * TODO(clocksmith) refactor GifEncoder and MediaEncoder.
 */
public class VideoEncoder extends MediaEncoder<VideoFrame> {
  private static final String TAG = VideoEncoder.class.getSimpleName();

  private static final String LOADING_FFMPEG = "loading ffmpeg";
  private static final String EXECUTING_FFMPEG = "executing ffmpeg";

  private FFmpeg mFFmpeg;
  private StopwatchTracker mTracker;

  public void setTracker(StopwatchTracker tracker) {
    mTracker = tracker;
  }

  @Override
  public void encodeAsync(MediaCreatorCallback callback) {
    super.encodeAsync(callback);

    mTracker.start(LOADING_FFMPEG);
    mFFmpeg = FFmpeg.getInstance(SzApp.CONTEXT);
    try {
      mFFmpeg.loadBinary(new FFmpegLoadBinaryResponseHandler() {
        @Override
        public void onFailure() {
          Log.wtf(TAG, "loadBinary onFailure()");
        }

        @Override
        public void onSuccess() {
          Log.wtf(TAG, "loadBinary onSuccess()");
          mTracker.stop(LOADING_FFMPEG);
          mTracker.start(EXECUTING_FFMPEG);
          execute();
        }

        @Override
        public void onStart() {
          Log.wtf(TAG, "loadBinary onStart()");
        }

        @Override
        public void onFinish() {
          Log.wtf(TAG, "loadBinary onFinish()");
        }
      });
    } catch (FFmpegNotSupportedException e) {
      SzLog.e(TAG, "Coud not load ffmpeg binary", e);
      mCallback.onCreateVideo(null);
    }
  }

  @Override
  public void cancel() {
    super.cancel();
    mFFmpeg.killRunningProcesses();
  }

  private void clean() {
    FileUtils.deletePrivateFileWithFilename("concat.txt");
    for (VideoFrame frame : mFrames) {
      File file = new File(frame.path.toString());
      file.delete();
    }
  }

  private void execute() {
    // TODO(clocksmith): clean up old files.

    final File videoOutFile = FileUtils.createTimestampedFileWithId(FileType.VIDEO, "video");
    final File concatFile = FileUtils.createPrivateFileWithFilename("concat.txt");

    try {
      PrintWriter pw = new PrintWriter(concatFile);
      for (VideoFrame frame : mFrames) {
        // TODO(clocksmith): used passed in FPS
        int frameDuration = (int) (1000.0 / Constants.MAIN_FPS);
        int numFrames = frame.delayMillis / frameDuration;
        for (int i = 0; i < numFrames; i++) {
          pw.println("file '" + frame.path.getAbsolutePath() + "'");
        }
      }
      pw.close();
    } catch (FileNotFoundException e) {
      SzLog.e(TAG, "Could not open print writer.", e);
      mCallback.onCreateVideo(null);
    }

    List<String> cmds = Lists.newArrayList();
    cmds.add("-f");
    cmds.add("concat");
    cmds.add("-safe");
    cmds.add("0");
    cmds.add("-y");
    cmds.add("-i");
    cmds.add(concatFile.getAbsolutePath());
    cmds.add("-an");
    cmds.add("-c:v");
    cmds.add("libx264");
    cmds.add("-b:v");
    cmds.add(Constants.VIDEO_KBPS + "k");
    cmds.add("-r");
    cmds.add(String.valueOf(Constants.MAIN_FPS));
    cmds.add(videoOutFile.getAbsolutePath());

    Log.wtf(TAG, Joiner.on(" ").join(cmds));

    try {
      mFFmpeg.execute(
          cmds.toArray(new String[cmds.size()]),
          new FFmpegExecuteResponseHandler() {
            @Override
            public void onSuccess(String message) {
              Log.wtf(TAG, "execute onSuccess()");
              clean();
              mTracker.stop(EXECUTING_FFMPEG);
              mCallback.onCreateVideo(videoOutFile);
            }

            @Override
            public void onProgress(String message) {
              Log.wtf(TAG, "execute onProgress() " + message);
            }

            @Override
            public void onFailure(String message) {
              Log.wtf(TAG, "execute onFailure()");
              // TODO(clocksmith): put error handlers in callback
              mCallback.onCreateVideo(null);
            }

            @Override
            public void onStart() {
              Log.wtf(TAG, "execute onStart()");
            }

            @Override
            public void onFinish() {
              Log.wtf(TAG, "execute onFinish()");
            }
          });
    } catch (FFmpegCommandAlreadyRunningException e) {
      SzLog.e(TAG, "Coud not execute ffmpeg cmd", e);
      mCallback.onCreateVideo(null);
    }
  }
}
