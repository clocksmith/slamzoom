package com.slamzoom.android.ui.start;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.Button;

import com.google.common.collect.ImmutableList;

/**
 * Created by clocksmith on 6/14/16.
 */
public class CreateButton extends Button {
  private static int STROKE_WIDTH = 8;

  private static ImmutableList<Integer> COLORS = ImmutableList.of(
      Color.parseColor("#388E3C"),
      Color.parseColor("#43A047"),
      Color.parseColor("#4CAF50"),
      Color.parseColor("#66BB6A"),
      Color.parseColor("#81C784"),
      //blue
      Color.parseColor("#1976D2"),
      Color.parseColor("#1E88E5"),
      Color.parseColor("#2196F3"),
      Color.parseColor("#42A5F5"),
      Color.parseColor("#64B5F6"),
      //purple
      Color.parseColor("#7B1FA2"),
      Color.parseColor("#8E24AA"),
      Color.parseColor("#9C27B0"),
      Color.parseColor("#AB47BC"),
      Color.parseColor("#BA68C8"),
      //red
      Color.parseColor("#D32F2F"),
      Color.parseColor("#E53935"),
      Color.parseColor("#F44336"),
      Color.parseColor("#EF5350"),
      Color.parseColor("#E57373"),
      //orange
      Color.parseColor("#F57C00"),
      Color.parseColor("#FB8C00"),
      Color.parseColor("#FF9800"),
      Color.parseColor("#FFA726"),
      Color.parseColor("#FFB74D"),
      //yellow
      Color.parseColor("#FBC02D"),
      Color.parseColor("#FDD835"),
      Color.parseColor("#FFEB3B"),
      Color.parseColor("#FFEE58"),
      Color.parseColor("#FFF176"));

  private Rect mRect;
  private Paint mBgPaint;
  private Paint mBorderPaint;
  private int mColorIndex = 0;
  private int mX = 0;
  private int mY = 0;
  private Rect mBlock;

  private Handler mHandler;
  private Runnable mTick;

  public CreateButton(Context context) {
    this(context, null);
  }

  public CreateButton(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CreateButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    mRect = new Rect();
    mBlock = new Rect(0, 0, STROKE_WIDTH, STROKE_WIDTH);

    mBgPaint = new Paint();
    mBgPaint.setColor(Color.parseColor("#202020"));

    mBorderPaint = new Paint();
    mBorderPaint.setStyle(Paint.Style.STROKE);
    mBorderPaint.setStrokeWidth(STROKE_WIDTH);

    mHandler = new Handler();
    mTick = new Runnable() {
      public void run() {
        invalidate();
      }
    };
  }

  @Override
  public void onDraw(Canvas canvas) {
    mBlock = new Rect(mX, mY, mX + STROKE_WIDTH, mY + STROKE_WIDTH);

    canvas.getClipBounds(mRect);
    canvas.drawRect(mRect, mBgPaint);

    while (mBlock.left < mRect.right) {
      mBorderPaint.setColor(COLORS.get(mColorIndex));
      mColorIndex = (mColorIndex + 1) % 30;
      canvas.drawRect(mBlock, mBorderPaint);
      mBlock.offset(STROKE_WIDTH, 0);
    }

    mColorIndex = (mColorIndex + 1) % 30;
    mBlock.offset(-STROKE_WIDTH + (mRect.right - mBlock.left), 0);

    while (mBlock.top < mRect.bottom) {
      mBorderPaint.setColor(COLORS.get(mColorIndex));
      mColorIndex = (mColorIndex + 1) % 30;
      canvas.drawRect(mBlock, mBorderPaint);
      mBlock.offset(0, STROKE_WIDTH);
    }

    mBlock.offset(0, -STROKE_WIDTH + (mRect.bottom - mBlock.top));

    mColorIndex = (mColorIndex + 1) % 30;

    while (mBlock.left > 0) {
      mBorderPaint.setColor(COLORS.get(mColorIndex));
      mColorIndex = (mColorIndex + 1) % 30;
      canvas.drawRect(mBlock, mBorderPaint);
      mBlock.offset(-STROKE_WIDTH, 0);
    }

    mColorIndex = (mColorIndex + 1) % 30;

    while (mBlock.top > 0) {
      mBorderPaint.setColor(COLORS.get(mColorIndex));
      mColorIndex = (mColorIndex + 1) % 30;
      canvas.drawRect(mBlock, mBorderPaint);
      mBlock.offset(0, -STROKE_WIDTH);
    }

    super.onDraw(canvas);
//    mX = (mX + 3) % mRect.width();

    //Call the next frame.
    mHandler.postDelayed(mTick, 68);
  }
}
