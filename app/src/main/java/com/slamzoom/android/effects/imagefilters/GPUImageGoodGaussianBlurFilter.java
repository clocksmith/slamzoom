package com.slamzoom.android.effects.imagefilters;

import android.opengl.GLES20;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;

/**
 * Created by clocksmith on 9/6/16.
 *
 * Derived from https://github.com/BradLarson/GPUImage/blob/master/framework/Source/GPUImageGaussianBlurFilter.m
 */
public class GPUImageGoodGaussianBlurFilter extends GPUImageFilterGroup {

  private float mBlurSize;

  public GPUImageGoodGaussianBlurFilter(float blurSize) {
    super(null);
    mBlurSize = blurSize;
    int blurRadius = Math.round(blurSize);
    int calculatedSampleRadius = 0;
    if (blurRadius >= 1) {
      float minimumWeightToFindEdgeOfSamplingArea = 1f / 256f;
      calculatedSampleRadius =
          (int) Math.floor(Math.sqrt(-2.0 * Math.pow(blurRadius, 2.0)
              * Math.log(minimumWeightToFindEdgeOfSamplingArea
              * Math.sqrt(2.0 * Math.PI * Math.pow(blurRadius, 2.0)))));
      calculatedSampleRadius += calculatedSampleRadius % 2;
    }

    String vertexShader = getVertexShader(calculatedSampleRadius, blurRadius);
    String fragmentShader = getFragmentShader(calculatedSampleRadius, blurRadius);

    addFilter(new GPUImageFilter(vertexShader, fragmentShader));
    addFilter(new GPUImageFilter(vertexShader, fragmentShader));
  }

  //region From GPUImageTwoPassTextureSamplingFilter

  @Override
  public void onInit() {
    super.onInit();
    initTexelOffsets();
  }

  @Override
  public void onOutputSizeChanged(int width, int height) {
    super.onOutputSizeChanged(width, height);
    initTexelOffsets();
  }

  protected float getVerticalTexelOffsetRatio() {
    return 1f;
  }

  protected float getHorizontalTexelOffsetRatio() {
    return 1f;
  }

  protected void initTexelOffsets() {
    float ratio = getHorizontalTexelOffsetRatio();
    GPUImageFilter filter = mFilters.get(0);
    int texelWidthOffsetLocation = GLES20.glGetUniformLocation(filter.getProgram(), "texelWidthOffset");
    int texelHeightOffsetLocation = GLES20.glGetUniformLocation(filter.getProgram(), "texelHeightOffset");
    setFloatHack(filter, texelWidthOffsetLocation, ratio / mOutputWidth);
    setFloatHack(filter, texelHeightOffsetLocation, 0);

    ratio = getVerticalTexelOffsetRatio();
    filter = mFilters.get(1);
    texelWidthOffsetLocation = GLES20.glGetUniformLocation(filter.getProgram(), "texelWidthOffset");
    texelHeightOffsetLocation = GLES20.glGetUniformLocation(filter.getProgram(), "texelHeightOffset");
    setFloatHack(filter, texelWidthOffsetLocation, 0);
    setFloatHack(filter, texelHeightOffsetLocation, ratio / mOutputHeight);
  }

  private void setFloatHack(GPUImageFilter filter, final int location, final float floatValue) {
    try {
      Method setFloatMethod = GPUImageFilter.class.getDeclaredMethod("setFloat", Integer.TYPE, Float.TYPE);
      setFloatMethod.setAccessible(true);
      setFloatMethod.invoke(filter, location, floatValue);
    } catch (Exception e) {
      Log.wtf("!!!!!", "Reflection Fail!", e);
    }
  }

  //endregion

  private String getVertexShader(int radius, float sigma) {
    float[] standardGaussianWeights = new float[radius + 1];
    float sumOfWeights = 0;

    for (int i = 0; i < standardGaussianWeights.length; i++) {
      standardGaussianWeights[i] = (float) ((1.0 / Math.sqrt(2.0 * Math.PI * Math.pow(sigma, 2.0)))
          * Math.exp(-Math.pow(i, 2.0) / (2.0 * Math.pow(sigma, 2.0))));

      if (i == 0) {
        sumOfWeights += standardGaussianWeights[i];
      }
      else {
        sumOfWeights += 2.0 * standardGaussianWeights[i];
      }
    }

    for (int i = 0; i < standardGaussianWeights.length; i++) {
      standardGaussianWeights[i] = standardGaussianWeights[i] / sumOfWeights;
    }

    int numberOfOptimizedOffsets = Math.min(radius / 2 + (radius % 2), 7);
    float[] optimizedGaussianOffsets = new float[numberOfOptimizedOffsets];

    for (int i = 0; i < optimizedGaussianOffsets.length; i++) {
      float firstWeight = standardGaussianWeights[i * 2 + 1];
      float secondWeight = standardGaussianWeights[i * 2 + 2];
      float optimizedWeight = firstWeight + secondWeight;
      optimizedGaussianOffsets[i] = (firstWeight * (i * 2 + 1) + secondWeight * (i * 2 + 2)) / optimizedWeight;
    }

    int numGaussianSamples = 1 + (numberOfOptimizedOffsets * 2);
    StringBuilder sb = new StringBuilder();
    sb.append("attribute vec4 position;\n");
    sb.append("attribute vec4 inputTextureCoordinate;\n");
    sb.append("\n");
    sb.append("uniform float texelWidthOffset;\n");
    sb.append("uniform float texelHeightOffset;\n");
    sb.append("\n");
    sb.append("varying vec2 blurCoordinates[" + numGaussianSamples   + "];\n");
    sb.append("\n");
    sb.append("void main()\n");
    sb.append("{\n");
    sb.append("  gl_Position = position;\n");
    sb.append("\n");
    sb.append("  vec2 singleStepOffset = vec2(texelWidthOffset, texelHeightOffset);\n");
    sb.append("  blurCoordinates[0] = inputTextureCoordinate.xy;\n");

    for (int i = 0; i < numberOfOptimizedOffsets; i++) {
      int index1 = (i * 2) + 1;
      int index2 = index1 + 1;
      float val = optimizedGaussianOffsets[i];
      sb.append("blurCoordinates[" + index1 + "] = inputTextureCoordinate.xy + singleStepOffset * " + val + ";\n");
      sb.append("blurCoordinates[" + index2 + "] = inputTextureCoordinate.xy - singleStepOffset * " + val + ";\n");
    }

    sb.append("}\n");

    return sb.toString();
  }

  private String getFragmentShader(int radius, float sigma) {
    float[] standardGaussianWeights = new float[radius + 1];
    float sumOfWeights = 0;

    for (int i = 0; i < standardGaussianWeights.length; i++) {
      standardGaussianWeights[i] = (float) ((1.0 / Math.sqrt(2.0 * Math.PI * Math.pow(sigma, 2.0)))
          * Math.exp(-Math.pow(i, 2.0) / (2.0 * Math.pow(sigma, 2.0))));

      if (i == 0) {
        sumOfWeights += standardGaussianWeights[i];
      }
      else {
        sumOfWeights += 2.0 * standardGaussianWeights[i];
      }
    }

    for (int i = 0; i < standardGaussianWeights.length; i++) {
      standardGaussianWeights[i] = standardGaussianWeights[i] / sumOfWeights;
    }

    int numberOfOptimizedOffsets = Math.min(radius / 2 + (radius % 2), 7);
    int trueNumberOfOptimizedOffsets = radius / 2 + (radius % 2);

    int numGaussianSamples = 1 + (numberOfOptimizedOffsets * 2);

    StringBuilder sb = new StringBuilder();
    sb.append("uniform sampler2D inputImageTexture;\n");
    sb.append("uniform float texelWidthOffset;\n");
    sb.append("uniform float texelHeightOffset;\n");
    sb.append("\n");
    sb.append("varying highp vec2 blurCoordinates[" + numGaussianSamples + "];\n");
    sb.append("\n");
    sb.append("void main()\n");
    sb.append("\n");
    sb.append("  vec4 sum = vec4(0.0);\\n");
    sb.append("sum += texture2D(inputImageTexture, blurCoordinates[0]) * " + standardGaussianWeights[0] + ";\n");

    for (int i = 0; i < numberOfOptimizedOffsets; i++) {
      int index1 = (i * 2) + 1;
      int index2 = index1 + 1;
      float firstWeight = standardGaussianWeights[i * 2 + 1];
      float secondWeight = standardGaussianWeights[i * 2 + 2];
      float optimizedWeight = firstWeight + secondWeight;
      sb.append("sum += texture2D(inputImageTexture, blurCoordinates[" + index1 + "]) * " + optimizedWeight + ";\n");
      sb.append("sum += texture2D(inputImageTexture, blurCoordinates[" + index2 + "]) * " + optimizedWeight + ";\n");
    }

    if (trueNumberOfOptimizedOffsets > numberOfOptimizedOffsets) {
      sb.append("highp vec2 singleStepOffset = vec2(texelWidthOffset, texelHeightOffset);\n");
      for (int i = numberOfOptimizedOffsets; i < trueNumberOfOptimizedOffsets; i++) {
        float firstWeight = standardGaussianWeights[i * 2 + 1];
        float secondWeight = standardGaussianWeights[i * 2 + 2];

        float optimizedWeight = firstWeight + secondWeight;
        float optimizedOffset = (firstWeight * (i * 2 + 1) + secondWeight * (i * 2 + 2)) / optimizedWeight;

        sb.append("sum += texture2D(inputImageTexture, blurCoordinates[0] + singleStepOffset * " + optimizedOffset +") * " + optimizedWeight + ";\n");
        sb.append("sum += texture2D(inputImageTexture, blurCoordinates[0] - singleStepOffset * " + optimizedOffset +") * " + optimizedWeight + ";\n");
      }
    }

    sb.append("gl_FragColor = sum;\n");
    sb.append("}\n");

    return sb.toString();
  }
}
