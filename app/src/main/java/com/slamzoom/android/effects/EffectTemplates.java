package com.slamzoom.android.effects;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.slamzoom.android.common.SzLog;
import com.slamzoom.android.effects.interpolation.CrashblurInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.RumbleslamInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.ThrowbackInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.PopInInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.PopOutInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.SlaminNoPauseInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeDoubleLeftRightFilterInterpolatorsProvider;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeFaceFilterInterpolatorsProvider;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeLeftRightSwapFilterInterpolationGroup;
import com.slamzoom.android.effects.interpolation.filter.group.DeflateFaceFilterInterpolatorsProvider;
import com.slamzoom.android.effects.interpolation.filter.group.InflateFaceFilterInterpolatorsProvider;
import com.slamzoom.android.effects.interpolation.filter.group.SumoBulgeFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.BulgeInAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.GaussianBlurFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.OverExposeFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlAtHotspotOnHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashTaranInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.FlushslamInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.RumblestiltskinInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.SlamoutNoPauseInterpolatorProvider;
import com.slamzoom.android.interpolators.LinearInterpolator;
import com.slamzoom.android.effects.interpolation.transform.scale.TeaseInterpolator;
import com.slamzoom.android.effects.interpolation.transform.scale.ThreeEaseInHardOutInterpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;

import java.util.Map;

/**
 * Created by clocksmith on 6/16/16.
 *
 * All effects. Keep alphabetized.
 */
public class EffectTemplates {
  private static final String TAG = EffectTemplates.class.getSimpleName();

  private static ImmutableList<EffectTemplate> EFFECT_TEMPLATES_LIST = ImmutableList.<EffectTemplate>builder()
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("bulge")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new LinearInterpolator())
          .withFilterInterpolator(new BulgeInAtHotspotFilterInterpolator())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("bulgeswap")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new LinearInterpolator())
          .withFilterInterpolatorGroup(new BulgeLeftRightSwapFilterInterpolationGroup())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("crashblur")
          .withStartDurationEndSeconds(1, 0.75f, 2)
          .withEffectInterpolatorProvider(new CrashblurInterpolatorProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("deflate")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new LinearInterpolator())
          .withFilterInterpolatorGroup(new DeflateFaceFilterInterpolatorsProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("doublebulge")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new LinearInterpolator())
          .withFilterInterpolatorGroup(new BulgeDoubleLeftRightFilterInterpolatorsProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("flushslam")
          .withStartDurationEndSeconds(1, 2, 1)
          .withEffectInterpolatorProvider(new FlushslamInterpolatorProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("inflate")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new LinearInterpolator())
          .withFilterInterpolatorGroup(new InflateFaceFilterInterpolatorsProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("peekaboo")
          .withStartDurationEndSeconds(1, 4, 2)
          .withScaleInterpolator(new TeaseInterpolator())
          .withFilterInterpolator(new OverExposeFilterInterpolator(new ThreeEaseInHardOutInterpolator()))
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("rumbleslam")
          .withStartDurationEndSeconds(1, 2, 1)
          .withEffectInterpolatorProvider(new RumbleslamInterpolatorProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("rumblestiltskin")
          .withStartDurationEndSeconds(1, 3, 0)
          .withEffectInterpolatorProvider(new RumblestiltskinInterpolatorProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("slamin")
          .withStartDurationEndSeconds(1.4f, 0.6f, 2)
          .withEffectInterpolatorProvider(new SlaminNoPauseInterpolatorProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("slamout")
          .withStartDurationEndSeconds(1.4f, 0.6f, 2)
          .withEffectInterpolatorProvider(new SlamoutNoPauseInterpolatorProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("smush")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new LinearInterpolator())
          .withFilterInterpolatorGroup(new BulgeFaceFilterInterpolatorsProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("blockhead")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new LinearInterpolator())
          .withFilterInterpolatorGroup(new SumoBulgeFilterInterpolator())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("swirlspot")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new LinearInterpolator())
          .withFilterInterpolator(new UnswirlAtHotspotOnHotspotFilterInterpolator())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("throwback")
          .withStartDurationEndSeconds(1, 2, 2)
          .withEffectInterpolatorProvider(new ThrowbackInterpolatorProvider())
          .build())

      // DEBUG

      .add(EffectTemplate.newSingleStepBuilder()
          .withName("debug1")
          .withStartDurationEndSeconds(2, 0.2f, 2)
          .withScaleInterpolator(new LinearInterpolator())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("popin")
          .withStartDurationEndSeconds(0, 0.175f, 0)
          .withTransformInterpolatorProvider(new PopInInterpolatorProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("popout")
          .withStartDurationEndSeconds(0, 0.175f, 0)
          .withTransformInterpolatorProvider(new PopOutInterpolatorProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("crashblur-show")
          .withStartDurationEndSeconds(0.25f, 0.75f, 1)
          .withTransformInterpolatorProvider(new CrashTaranInterpolatorProvider())
          .withFilterInterpolator(new GaussianBlurFilterInterpolator(LinearSplineInterpolator.newBuilder()
              .withPoint(0, 0)
              .withPoint(0.3f, 1)
              .withPoint(0.6f, 1)
              .withPoint(0.9f, 0)
              .withPoint(1, 0)
              .build()))
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("deflate-show")
          .withStartDurationEndSeconds(0, 2, 0)
          .withScaleInterpolator(new LinearInterpolator())
          .withFilterInterpolatorGroup(new DeflateFaceFilterInterpolatorsProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("inflate-show")
          .withStartDurationEndSeconds(0, 2, 0)
          .withScaleInterpolator(new LinearInterpolator())
          .withFilterInterpolatorGroup(new InflateFaceFilterInterpolatorsProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("rumblestiltskin-show")
          .withStartDurationEndSeconds(0, 3, 0)
          .withEffectInterpolatorProvider(new RumblestiltskinInterpolatorProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("slamin-show")
          .withStartDurationEndSeconds(0.4f, 0.6f, 1)
          .withEffectInterpolatorProvider(new SlaminNoPauseInterpolatorProvider())
          .build())
      .build();

  private static Map<String, EffectTemplate> EFFECT_TEMPLATES_MAP_TO_USE =
      Maps.newHashMap(Maps.uniqueIndex(EFFECT_TEMPLATES_LIST,
          new Function<EffectTemplate, String>() {
            @Override
            public String apply(EffectTemplate input) {
              return input.getName();
            }
          }));

  // TODO(clocksmith): sloppy to reuse the code for this.
  private static Map<String, EffectTemplate> USED_EFFECT_TEMPLATES_MAP = Maps.newHashMap();

  public static EffectTemplate consume(String name) {
    if (EFFECT_TEMPLATES_MAP_TO_USE.containsKey(name)) {
      USED_EFFECT_TEMPLATES_MAP.put(name, EFFECT_TEMPLATES_MAP_TO_USE.remove(name));
      return USED_EFFECT_TEMPLATES_MAP.get(name);
    } else {
      SzLog.e(TAG, "Effect: \"" + name + "\" does not exist or has already been used.");
      return null;
    }
  }

  public static EffectTemplate get(String name) {
//    if (USED_EFFECT_TEMPLATES_MAP.containsKey(name)) {
//      return USED_EFFECT_TEMPLATES_MAP.get(name);
//    } else {
//      SzLog.e(TAG, "Effect: \"" + name + "\" was never used.");
//      return null;
//    }

    if (!EFFECT_TEMPLATES_MAP_TO_USE.containsKey(name)) {
      SzLog.e(TAG, "No effect with name: " + name);
    }
    return EFFECT_TEMPLATES_MAP_TO_USE.get(name);
  }
}
