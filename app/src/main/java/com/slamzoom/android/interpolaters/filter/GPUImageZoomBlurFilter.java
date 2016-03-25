package com.slamzoom.android.interpolaters.filter;

import android.graphics.PointF;
import android.opengl.GLES20;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by clocksmith on 3/21/16.
 */
public class GPUImageZoomBlurFilter extends GPUImageFilter {
  public static final String ZOOM_BLUR_FRAGMENT_SHADER = "" +
      "varying highp vec2 textureCoordinate;\n" +
      "\n" +
      "uniform sampler2D inputImageTexture;\n" +
      "\n" +
      "uniform highp vec2 blurCenter;\n" +
      "uniform highp float blurSize;" +
      "\n" +
      "void main()\n" +
      "{\n" +
      "highp vec2 samplingOffset = 1.0/100.0 * (blurCenter - textureCoordinate) * blurSize;\n" +
      "lowp vec4 fragmentColor = texture2D(inputImageTexture, textureCoordinate) * 0.18;\n" +
      "fragmentColor += texture2D(inputImageTexture, textureCoordinate + samplingOffset) * 0.15;\n" +
      "fragmentColor += texture2D(inputImageTexture, textureCoordinate + (2.0 * samplingOffset)) *  0.12;\n" +
      "fragmentColor += texture2D(inputImageTexture, textureCoordinate + (3.0 * samplingOffset)) * 0.09;\n" +
      "fragmentColor += texture2D(inputImageTexture, textureCoordinate + (4.0 * samplingOffset)) * 0.05;\n" +
      "fragmentColor += texture2D(inputImageTexture, textureCoordinate - samplingOffset) * 0.15;\n" +
      "fragmentColor += texture2D(inputImageTexture, textureCoordinate - (2.0 * samplingOffset)) *  0.12;\n" +
      "fragmentColor += texture2D(inputImageTexture, textureCoordinate - (3.0 * samplingOffset)) * 0.09;\n" +
      "fragmentColor += texture2D(inputImageTexture, textureCoordinate - (4.0 * samplingOffset)) * 0.05;\n" +
      "\n" +
      "gl_FragColor = fragmentColor;\n" +
      "\n" +
      "}\n";

  protected float mBlurSize;
  protected PointF mBlurCenter;
  protected int mBlurSizeLocation;
  protected int mBlurCenterLocation;

  public GPUImageZoomBlurFilter() {
    this(1.0f);
  }

  public GPUImageZoomBlurFilter(float blurSize) {
    this(blurSize, new PointF(0.5f, 0.5f));
  }

  public GPUImageZoomBlurFilter(float blurSize, PointF blurCenter) {
    super(NO_FILTER_VERTEX_SHADER, ZOOM_BLUR_FRAGMENT_SHADER);
    mBlurSize = blurSize;
    mBlurCenter = blurCenter;
  }

  @Override
  public void onInit() {
    super.onInit();
    mBlurSizeLocation = GLES20.glGetUniformLocation(getProgram(), "blurSize");
    mBlurCenterLocation = GLES20.glGetUniformLocation(getProgram(), "blurCenter");
  }

  @Override
  public void onInitialized() {
    super.onInitialized();
    setBlurSize(mBlurSize);
    setBlurCenter(mBlurCenter);
  }

  public void setBlurSize(final float blurSize) {
    mBlurSize = blurSize;
    setFloat(mBlurSizeLocation, mBlurSize);
  }

  public void setBlurCenter(final PointF blurCenter) {
    mBlurCenter = blurCenter;
    setPoint(mBlurCenterLocation, mBlurCenter);
  }
}
