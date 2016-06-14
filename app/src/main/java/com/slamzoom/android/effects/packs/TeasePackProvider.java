package com.slamzoom.android.effects.packs;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.EffectStep;
import com.slamzoom.android.effects.EffectTemplate;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeEyesSwirlMouthFilterInterpoaltor;
import com.slamzoom.android.effects.interpolation.filter.group.UnswirlEyesFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.single.GaussianBlurFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.OverExposeFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnderExposeFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnsaturateFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlAtHotspotOnHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.transform.TranslateInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CircleCenterInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashBounceDiagonalInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.FlushInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.BaseShakeInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.ShakeInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.LinearInterpolator;
import com.slamzoom.android.interpolators.custom.InAndOutInterpolator;
import com.slamzoom.android.interpolators.custom.SlamSoftInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 6/14/16.
 */
public class TeasePackProvider {
  public static List<EffectTemplate> getPack() {
    List<EffectTemplate> packModels = Lists.newArrayList();

    packModels.add(EffectTemplate.newBuilder()
        .withName("blacktease")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(1)
            .withDurationSeconds(4f)
            .withScaleInterpolator(new Interpolator() {
              @Override
              protected float getRangePercent(float t) {
                if (t < 0.1667) {
                  return 0;
                } else if (t < 0.5) {
                  return 0.2f;
                } else if (t < 0.8333) {
                  return 0.45f;
                } else {
                  return 1;
                }
              }
            })
            .withFilterInterpolator(new UnderExposeFilterInterpolator(
                new Interpolator() {
                  @Override
                  protected float getRangePercent(float t) {
                    return (float) Math.pow(Math.sin(3 * Math.PI * t), 10);
                  }
                }))
            .withEndPauseSeconds(1)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("whitetease")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(1)
            .withDurationSeconds(4)
            .withScaleInterpolator(new Interpolator() {
              @Override
              protected float getRangePercent(float t) {
                if (t < 0.1667) {
                  return 0;
                } else if (t < 0.5) {
                  return 0.2f;
                } else if (t < 0.8333) {
                  return 0.45f;
                } else {
                  return 1;
                }
              }
            })
            .withFilterInterpolator(new OverExposeFilterInterpolator(
                new Interpolator() {
                  @Override
                  protected float getRangePercent(float t) {
                    return (float) Math.pow(Math.sin(3 * Math.PI * t), 10);
                  }
                }))
            .withEndPauseSeconds(1)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("blurtease")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(1)
            .withDurationSeconds(4)
            .withScaleInterpolator(new Interpolator() {
              @Override
              protected float getRangePercent(float t) {
                if (t < 0.1667) {
                  return 0;
                } else if (t < 0.5) {
                  return 0.2f;
                } else if (t < 0.8333) {
                  return 0.45f;
                } else {
                  return 1;
                }
              }
            })
            .withFilterInterpolator(new GaussianBlurFilterInterpolator(
                new Interpolator() {
                  @Override
                  protected float getRangePercent(float t) {
                    return (float) Math.pow(Math.sin(3 * Math.PI * t), 10);
                  }
                }))
            .withEndPauseSeconds(1)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("rumbletease")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(6)
            .withScaleInterpolator(new Interpolator() {
              @Override
              protected float getRangePercent(float t) {
                if (t < 0.25) {
                  return 0;
                } else if (t < 0.5) {
                  return 0.2f;
                } else if (t < 0.75) {
                  return 0.45f;
                } else {
                  return 1;
                }
              }
            })
            .withTranslateInterpolator(new TranslateInterpolatorProvider() {
              private BaseShakeInterpolatorProvider first = new BaseShakeInterpolatorProvider(8, 8);
              private BaseShakeInterpolatorProvider second = new BaseShakeInterpolatorProvider(6, 12);
              private BaseShakeInterpolatorProvider third = new BaseShakeInterpolatorProvider(4, 20);
              private BaseShakeInterpolatorProvider fourth = new BaseShakeInterpolatorProvider(2, 32);
              @Override
              public Interpolator getXInterpolator() {
                return new Interpolator() {
                  @Override
                  protected float getRangePercent(float t) {
                    if (t < 0.25) {
                      return first.getXInterpolator().getInterpolation(t);
                    } else if (t < 0.5) {
                      return second.getXInterpolator().getInterpolation(t);
                    } else if (t < 0.75) {
                      return third.getXInterpolator().getInterpolation(t);
                    } else {
                      return fourth.getXInterpolator().getInterpolation(t);
                    }
                  }
                };
              }

              @Override
              public Interpolator getYInterpolator() {
                return new Interpolator() {
                  @Override
                  protected float getRangePercent(float t) {
                    if (t < 0.25) {
                      return first.getYInterpolator().getInterpolation(t);
                    } else if (t < 0.5) {
                      return second.getYInterpolator().getInterpolation(t);
                    } else if (t < 0.75) {
                      return third.getYInterpolator().getInterpolation(t);
                    } else {
                      return fourth.getYInterpolator().getInterpolation(t);
                    }
                  }
                };
              }
            })
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("graytease")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(1)
            .withDurationSeconds(4)
            .withScaleInterpolator(new Interpolator() {
              @Override
              protected float getRangePercent(float t) {
                if (t < 0.1667) {
                  return 0;
                } else if (t < 0.5) {
                  return 0.2f;
                } else if (t < 0.8333) {
                  return 0.45f;
                } else {
                  return 1;
                }
              }
            })
            .withFilterInterpolator(new UnsaturateFilterInterpolator(
                new Interpolator() {
                  @Override
                  protected float getRangePercent(float t) {
                    return (float) Math.pow(Math.sin(3.5 * Math.PI * (t + 2f / 7)), 2);
                  }
                }))
            .withEndPauseSeconds(1)
            .build())
        .build());

    return packModels;
  }
}
