package com.slamzoom.android.effects.interpolation.transformfilter;

import com.slamzoom.android.common.utils.AnimationUtils;
import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;
import com.slamzoom.android.effects.interpolation.transform.TranslateInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;

import java.util.List;

/**
 * Created by clocksmith on 8/30/16.
 */
public class PopInAndWrapperInterpolatorProvider extends TransformAndFilterInterpolatorProvider {
  TransformAndFilterInterpolatorProvider mToWrap;
  private float mTransitionDurationFraction;

  public PopInAndWrapperInterpolatorProvider(TransformAndFilterInterpolatorProvider toWrap, float duration) {
    mToWrap = toWrap;
    mTransitionDurationFraction = AnimationUtils.DEFAULT_ANIMATION_DURATION_MS /
        (duration * 1000 + AnimationUtils.DEFAULT_ANIMATION_DURATION_MS);
  }

  @Override
  public TranslateInterpolatorProvider getTranslateInterpolationProvider() {
    return new TranslateInterpolatorProvider() {
      @Override
      public Interpolator getXInterpolator() {
        return new Interpolator() {
          @Override
          public float getValue(float t) {
            if (t < mTransitionDurationFraction || t > (1 - mTransitionDurationFraction)) {
              return 0;
            } else {
              return mToWrap.getXInterpolator().getValue(
                  (t - mTransitionDurationFraction / (1 - mTransitionDurationFraction)));
            }
          }
        };
      }

      @Override
      public Interpolator getYInterpolator() {
        return new Interpolator() {
          @Override
          public float getValue(float t) {
            if (t < mTransitionDurationFraction || t > (1 - mTransitionDurationFraction)) {
              return 0;
            } else {
              return mToWrap.getYInterpolator().getValue(
                  (t - mTransitionDurationFraction / (1 - mTransitionDurationFraction)));
            }
          }
        };
      }
    };
  }

  @Override
  public List<FilterInterpolator> getFilterInterpolators() {
//    return Lists.transform(mToWrap.getFilterInterpolators(), new Function<FilterInterpolator, FilterInterpolator>() {
//      @Override
//      public FilterInterpolator apply(FilterInterpolator input) {
//        return new FilterInterpolator() {
//          @Override
//          protected GPUImageFilter getFilter() {
//            return null;
//          }
//        }
//      }
//    })
    return null;
  }

  @Override
  public Interpolator getScaleInterpolator() {
    return null;
  }
}
