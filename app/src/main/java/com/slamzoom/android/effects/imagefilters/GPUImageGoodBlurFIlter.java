package com.slamzoom.android.effects.imagefilters;

import jp.co.cyberagent.android.gpuimage.GPUImageTwoPassTextureSamplingFilter;

/**
 * Created by clocksmith on 9/6/16.
 */
public class GPUImageGoodBlurFIlter extends GPUImageTwoPassTextureSamplingFilter {
  public static final String VERTEX_SHADER =
      "attribute vec4 position;\n" +
          "attribute vec4 inputTextureCoordinate;\n" +
          "\n" +
          "const int GAUSSIAN_SAMPLES = 25;\n" +
          "\n" +
          "uniform float texelWidthOffset;\n" +
          "uniform float texelHeightOffset;\n" +
          "\n" +
          "varying vec2 textureCoordinate;\n" +
          "varying vec2 blurCoordinates[GAUSSIAN_SAMPLES];\n" +
          "\n" +
          "void main()\n" +
          "{\n" +
          "	gl_Position = position;\n" +
          "	textureCoordinate = inputTextureCoordinate.xy;\n" +
          "	\n" +
          "	// Calculate the positions for the blur\n" +
          "	int multiplier = 0;\n" +
          "	vec2 blurStep;\n" +
          "   vec2 singleStepOffset = vec2(texelHeightOffset, texelWidthOffset);\n" +
          "    \n" +
          "	for (int i = 0; i < GAUSSIAN_SAMPLES; i++)\n" +
          "   {\n" +
          "		multiplier = (i - ((GAUSSIAN_SAMPLES - 1) / 2));\n" +
          "       // Blur in x (horizontal)\n" +
          "       blurStep = float(multiplier) * singleStepOffset;\n" +
          "		blurCoordinates[i] = inputTextureCoordinate.xy + blurStep;\n" +
          "	}\n" +
          "}\n";

  public static final String FRAGMENT_SHADER =
      "uniform sampler2D inputImageTexture;\n" +
          "\n" +
          "const lowp int GAUSSIAN_SAMPLES = 9;\n" +
          "\n" +
          "varying highp vec2 textureCoordinate;\n" +
          "varying highp vec2 blurCoordinates[GAUSSIAN_SAMPLES];\n" +
          "\n" +
          "void main()\n" +
          "{\n" +
          "	lowp vec3 sum = vec3(0.0);\n" +
          "   lowp vec4 fragColor=texture2D(inputImageTexture,textureCoordinate);\n" +
          "	\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[0]).rgb * 0.003765;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[1]).rgb * 0.015019;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[2]).rgb * 0.023792;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[3]).rgb * 0.015019;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[4]).rgb * 0.003765;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[5]).rgb * 0.015019;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[6]).rgb * 0.059912;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[7]).rgb * 0.094907;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[8]).rgb * 0.059912;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[9]).rgb * 0.015019;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[10]).rgb * 0.023792;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[11]).rgb * 0.094907;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[12]).rgb * 0.150342;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[13]).rgb * 0.094907;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[14]).rgb * 0.023792;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[15]).rgb * 0.015019;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[16]).rgb * 0.059912;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[17]).rgb * 0.094907;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[18]).rgb * 0.059912;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[19]).rgb * 0.015019;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[20]).rgb * 0.003765;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[21]).rgb * 0.015019;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[22]).rgb * 0.023792;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[23]).rgb * 0.015019;\n" +
          "    sum += texture2D(inputImageTexture, blurCoordinates[24]).rgb * 0.003765;\n" +
          "\n" +
          "	gl_FragColor = vec4(sum,fragColor.a);\n" +
          "}";

  protected float mBlurSize = 1f;

  public GPUImageGoodBlurFIlter() {
    this(1f);
  }

  public GPUImageGoodBlurFIlter(float blurSize) {
    super(VERTEX_SHADER, FRAGMENT_SHADER, VERTEX_SHADER, FRAGMENT_SHADER);
    mBlurSize = blurSize;
  }

  @Override
  public float getVerticalTexelOffsetRatio() {
    return mBlurSize;
  }

  @Override
  public float getHorizontalTexelOffsetRatio() {
    return mBlurSize;
  }

  /**
   * A multiplier for the blur size, ranging from 0.0 on up, with a default of 1.0
   *
   * @param blurSize from 0.0 on up, default 1.0
   */
  public void setBlurSize(float blurSize) {
    mBlurSize = blurSize;
    runOnDraw(new Runnable() {
      @Override
      public void run() {
        initTexelOffsets();
      }
    });
  }
}
