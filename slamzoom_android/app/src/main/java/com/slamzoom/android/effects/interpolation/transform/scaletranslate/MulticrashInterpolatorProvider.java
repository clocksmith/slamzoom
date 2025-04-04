package com.slamzoom.android.effects.interpolation.transform.scaletranslate;

import com.slamzoom.android.effects.interpolation.transform.TransformInterpolatorProvider;
import com.slamzoom.android.interpolators.ConstantInterpolator;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolators.spline.PointsBuilder;

/**
 * Created by clocksmith on 8/28/16.
 */
public class MulticrashInterpolatorProvider implements TransformInterpolatorProvider {
  private float firstBreak = 0.17f;
  private float secondBreak = 0.34f;
  private float thirdBreak = 0.51f;

  private TransformInterpolatorProvider firstCrash = new TransformInterpolatorProvider() {
    @Override
    public Interpolator getScaleInterpolator() {
      return new LinearSplineInterpolator(PointsBuilder.create()
          .withPoint(0, 0)
          .withPoint(0.4f, 1)
          .withPoint(1, 1)
          .build());
    }

    @Override
    public Interpolator getXInterpolator() {
      return new LinearSplineInterpolator(PointsBuilder.create()
          .withPoint(0, 0)
          .withPoint(0.37f, -2.35f)
          .withPoint(0.52f, -2.05f)
          .withPoint(1, -2)
          .build());
    }

    @Override
    public Interpolator getYInterpolator() {
      return new LinearSplineInterpolator(PointsBuilder.create()
          .withPoint(0, 0)
          .withPoint(0.4f, -2.05f)
          .withPoint(0.55f, -1.95f)
          .withPoint(1, -2)
          .build());
    }
  };

  private TransformInterpolatorProvider secondCrash = new TransformInterpolatorProvider() {
    @Override
    public Interpolator getScaleInterpolator() {
      return new LinearSplineInterpolator(PointsBuilder.create()
          .withPoint(0, 0)
          .withPoint(0.4f, 1)
          .withPoint(1, 1)
          .build());
    }

    @Override
    public Interpolator getXInterpolator() {
      return new LinearSplineInterpolator(PointsBuilder.create()
          .withPoint(0, 0)
          .withPoint(0.37f, 2.35f)
          .withPoint(0.52f, 2.05f)
          .withPoint(1, 2)
          .build());
    }

    @Override
    public Interpolator getYInterpolator() {
      return new LinearSplineInterpolator(PointsBuilder.create()
          .withPoint(0, 0)
          .withPoint(0.4f, 2.05f)
          .withPoint(0.55f, 1.95f)
          .withPoint(1, 2)
          .build());
    }
  };

  private TransformInterpolatorProvider thirdCrash = new TransformInterpolatorProvider() {
    @Override
    public Interpolator getScaleInterpolator() {
      return new LinearSplineInterpolator(PointsBuilder.create()
          .withPoint(0, 0)
          .withPoint(0.4f, 1)
          .withPoint(1, 1)
          .build());
    }

    @Override
    public Interpolator getXInterpolator() {
      return new LinearSplineInterpolator(PointsBuilder.create()
          .withPoint(0, 0)
          .withPoint(0.37f, -2.35f)
          .withPoint(0.52f, -2.05f)
          .withPoint(1, -2)
          .build());
    }

    @Override
    public Interpolator getYInterpolator() {
      return new LinearSplineInterpolator(PointsBuilder.create()
          .withPoint(0, 0)
          .withPoint(0.4f, 2.05f)
          .withPoint(0.55f, 1.95f)
          .withPoint(1, 2)
          .build());
    }
  };

  private TransformInterpolatorProvider fourthCrash = new TransformInterpolatorProvider() {
    @Override
    public Interpolator getScaleInterpolator() {
      return LinearSplineInterpolator.newBuilder()
          .withPoint(0, 0)
          .withPoint(0.2f, 1.3f)
          .withPoint(0.4f, 1)
          .withPoint(1, 1)
          .build();
    }

    @Override
    public Interpolator getXInterpolator() {
      return new ConstantInterpolator(0);
    }

    @Override
    public Interpolator getYInterpolator() {
      return new ConstantInterpolator(0);
    }
  };

  public Interpolator getScaleInterpolator() {
    return new Interpolator() {
      @Override
      public float getValue(float t) {
        return getSubInterpolator(t).getScaleInterpolator().getInterpolation(getAdjustedT(t));
      }
    };
  }

  @Override
  public Interpolator getXInterpolator() {
    return new Interpolator() {
      @Override
      public float getValue(float t) {
        return getSubInterpolator(t).getXInterpolator().getInterpolation(getAdjustedT(t));
      }
    };
  }

  @Override
  public Interpolator getYInterpolator() {
    return new Interpolator() {
      @Override
      public float getValue(float t) {
        return getSubInterpolator(t).getYInterpolator().getInterpolation(getAdjustedT(t));
      }
    };
  }

  private TransformInterpolatorProvider getSubInterpolator(float t) {
    if (t < firstBreak) {
      return firstCrash;
    } else if (t < secondBreak) {
      return secondCrash;
    } else if (t < thirdBreak) {
      return thirdCrash;
    } else {
      return fourthCrash;
    }
  }

  private float getAdjustedT(float t) {
    if (t < firstBreak) {
      return t / firstBreak;
    } else if (t < secondBreak) {
      return (t - firstBreak) / (secondBreak - firstBreak);
    } else if (t < thirdBreak) {
      return (t - secondBreak) / (thirdBreak - secondBreak);
    } else {
      return (t - thirdBreak) / (1 - secondBreak);
    }
  }
}
