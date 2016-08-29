package com.slamzoom.android.effects;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.slamzoom.android.common.SzLog;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeDoubleLeftRightFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeEyesFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeEyesSwirlMouthFilterInterpoaltor;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeFaceFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeLeftRightSwapFilterInterpolationGroup;
import com.slamzoom.android.effects.interpolation.filter.group.DeflateFaceFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.InflateFaceFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.SumoBulge2FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.group.SumoBulgeFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.group.SwirlEyesTwistyMouthFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.group.UnswirlEyesFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.single.BulgeInAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.GaussianBlurFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.GaussianUnblurFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.GuassianSuperBlurFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.OverExposeFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.ShrinkInAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnderExposeFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnsaturateFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlAtHotspotOnHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlTurntableAtHotspotOnHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.ZoomBlurAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CircleCenterInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashBounceBottomInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashBounceDiagonalInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashMissInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashRumbleInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashTaranInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.FlushInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.MulticrashInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.RumbleTeaseInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.ShakeSwitchInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.MegaShakeInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.ShakeInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.SuperShakeInterpolatorProvider;
import com.slamzoom.android.interpolators.LinearInterpolator;
import com.slamzoom.android.interpolators.ReverseLinearInterpolator;
import com.slamzoom.android.interpolators.custom.HalfInAndOutInterpolator;
import com.slamzoom.android.interpolators.custom.InAndOutInterpolator;
import com.slamzoom.android.interpolators.custom.NoneToAllAtHalfInterpolator;
import com.slamzoom.android.interpolators.custom.OutAndInInterpolator;
import com.slamzoom.android.interpolators.custom.OvershootInterpolator;
import com.slamzoom.android.interpolators.custom.SlamHardInAndOutInterpolator;
import com.slamzoom.android.interpolators.custom.SlamHardInterpolator;
import com.slamzoom.android.interpolators.custom.SlamHardNoPauseInterpolator;
import com.slamzoom.android.interpolators.custom.SlamSoftInterpolator;
import com.slamzoom.android.interpolators.custom.SlamSoftOutInterpolator;
import com.slamzoom.android.interpolators.custom.TeaseInterpolator;
import com.slamzoom.android.interpolators.custom.ThreeEaseInHardOutInterpolator;
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
          .withName("blacktease")
          .withStartDurationEndSeconds(1, 4, 2)
          .withScaleInterpolator(new TeaseInterpolator())
          .withFilterInterpolator(new UnderExposeFilterInterpolator(new ThreeEaseInHardOutInterpolator()))
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("blurcrash")
          .withStartDurationEndSeconds(0.5f, 0.8f, 1)
          .withScaleInterpolator(new SlamHardInterpolator())
          .withFilterInterpolator(new GaussianUnblurFilterInterpolator())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("blurmagic")
          .withStartDurationEndSeconds(0, 2, 1)
          .withScaleInterpolator(new NoneToAllAtHalfInterpolator())
          .withFilterInterpolator(new GuassianSuperBlurFilterInterpolator(new InAndOutInterpolator()))
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("blurslam")
          .withStartDurationEndSeconds(0, 2, 1)
          .withScaleInterpolator(new SlamHardInterpolator())
          .withFilterInterpolator(new GaussianUnblurFilterInterpolator())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("blurshake")
          .withStartDurationEndSeconds(0, 2, 1)
          .withScaleInterpolator(new LinearInterpolator())
          .withTranslateInterpolator(new ShakeInterpolatorProvider())
          .withFilterInterpolator(new GaussianUnblurFilterInterpolator())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("blursmith")
          .withStartDurationEndSeconds(0, 2, 1)
          .withScaleInterpolator(LinearSplineInterpolator.newBuilder()
              .withPoint(0, 0)
              .withPoint(0.4f, 0)
              .withPoint(0.6f, 1)
              .withPoint(1, 1)
              .build())
          .withFilterInterpolator(new GaussianBlurFilterInterpolator(new InAndOutInterpolator()))
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("blurtease")
          .withStartDurationEndSeconds(1, 4, 2)
          .withScaleInterpolator(new TeaseInterpolator())
          .withFilterInterpolator(new GaussianBlurFilterInterpolator(new ThreeEaseInHardOutInterpolator()))
          .build())
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
          .withScaleAndTranslateInterpolatorProvider(new CrashTaranInterpolatorProvider())
          .withFilterInterpolator(new GaussianBlurFilterInterpolator(LinearSplineInterpolator.newBuilder()
              .withPoint(0, 0)
              .withPoint(0.3f, 1)
              .withPoint(0.6f, 1)
              .withPoint(0.9f, 0)
              .withPoint(1, 0)
              .build()))
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("crashdiag")
          .withStartDurationEndSeconds(1, 1, 2)
          .withScaleAndTranslateInterpolatorProvider(new CrashBounceDiagonalInterpolatorProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("crashin")
          .withStartDurationEndSeconds(1, 1, 2)
          .withScaleAndTranslateInterpolatorProvider(new CrashBounceBottomInterpolatorProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("crashmiss")
          .withStartDurationEndSeconds(1, 0.75f, 2)
          .withScaleAndTranslateInterpolatorProvider(new CrashMissInterpolatorProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("crashrumble")
          .withStartDurationEndSeconds(1, 1.5f, 0)
          .withScaleAndTranslateInterpolatorProvider(new CrashRumbleInterpolatorProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("deflate")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new LinearInterpolator())
          .withFilterInterpolatorGroup(new DeflateFaceFilterInterpolatorGroup())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("djswirls")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new LinearInterpolator())
          .withFilterInterpolator(new UnswirlTurntableAtHotspotOnHotspotFilterInterpolator())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("doublebulge")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new LinearInterpolator())
          .withFilterInterpolatorGroup(new BulgeDoubleLeftRightFilterInterpolatorGroup())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("earthquake")
          .withStartDurationEndSeconds(0, 2, 0)
          .withScaleInterpolator(new HalfInAndOutInterpolator())
          .withTranslateInterpolator(new SuperShakeInterpolatorProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("flashreveal")
          .withStartDurationEndSeconds(1, 4, 2)
          .withScaleInterpolator(new TeaseInterpolator())
          .withFilterInterpolator(new OverExposeFilterInterpolator(new ThreeEaseInHardOutInterpolator()))
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("flush")
          .withStartDurationEndSeconds(0, 3, 1)
          .withScaleAndTranslateInterpolatorProvider(new FlushInterpolatorProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("flushslam")
          .withStartDurationEndSeconds(1, 1, 1)
          .withScaleAndTranslateInterpolatorProvider(new FlushInterpolatorProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("grayrumble")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new LinearInterpolator())
          .withTranslateInterpolator(new ShakeInterpolatorProvider())
          .withFilterInterpolator(new UnsaturateFilterInterpolator())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("grayslam")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new SlamHardInterpolator())
          .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(LinearSplineInterpolator.newBuilder()
              .withPoint(0, 0)
              .withPoint(0.7f, 0)
              .withPoint(0.9f, 1)
              .withPoint(0.9999f, 1)
              .withPoint(1, 0)
              .build()))
          .withFilterInterpolator(new UnsaturateFilterInterpolator())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("graytake")
          .withStartDurationEndSeconds(0, 2, 2)
          .withScaleInterpolator(new OutAndInInterpolator())
          .withFilterInterpolator(new UnsaturateFilterInterpolator(new LinearInterpolator()))
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("inflate")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new LinearInterpolator())
          .withFilterInterpolatorGroup(new InflateFaceFilterInterpolatorGroup())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("magoo")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new LinearInterpolator())
          .withFilterInterpolatorGroup(new BulgeEyesFilterInterpolatorGroup())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("mctwisty")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new LinearInterpolator())
          .withFilterInterpolatorGroup(new SwirlEyesTwistyMouthFilterInterpolator())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("multicrash")
          .withStartDurationEndSeconds(1, 3, 1)
          .withScaleAndTranslateInterpolatorProvider(new MulticrashInterpolatorProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("overcrash")
          .withStartDurationEndSeconds(1, 1, 2)
          .withScaleInterpolator(new OvershootInterpolator())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("rumble")
          .withStartDurationEndSeconds(0, 2, 0)
          .withScaleInterpolator(new HalfInAndOutInterpolator())
          .withTranslateInterpolator(new ShakeInterpolatorProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("rumbleslam")
          .withStartDurationEndSeconds(1, 2, 1)
          .withScaleInterpolator(new SlamHardInterpolator())
          .withTranslateInterpolator(new ShakeInterpolatorProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("rumbletease")
          .withStartDurationEndSeconds(0, 6, 0)
          .withScaleInterpolator(new TeaseInterpolator())
          .withTranslateInterpolator(new RumbleTeaseInterpolatorProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("rumblestiltskin")
          .withStartDurationEndSeconds(0, 3, 0)
          .withScaleAndTranslateInterpolatorProvider(new ShakeSwitchInterpolatorProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("shakezilla")
          .withStartDurationEndSeconds(0, 2, 0)
          .withScaleInterpolator(new HalfInAndOutInterpolator())
          .withTranslateInterpolator(new MegaShakeInterpolatorProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("shrinkydink")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new LinearInterpolator())
          .withFilterInterpolator(new ShrinkInAtHotspotFilterInterpolator())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("slamin")
          .withStartDurationEndSeconds(1.4f, 0.6f, 2)
          .withScaleInterpolator(new SlamHardNoPauseInterpolator())
          .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(LinearSplineInterpolator.newBuilder()
              .withPoint(0, 0)
              .withPoint(0.9f, 1)
              .withPoint(0.9999f, 1)
              .withPoint(1, 0)
              .build()))
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("slamio")
          .withStartDurationEndSeconds(0, 2, 0)
          .withScaleInterpolator(new SlamHardInAndOutInterpolator())
          .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(LinearSplineInterpolator.newBuilder()
              .withPoint(0, 0)
              .withPoint(0.8f, 1)
              .withPoint(1, 0)
              .build()))
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("slamout")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new SlamSoftOutInterpolator())
          .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(LinearSplineInterpolator.newBuilder()
              .withPoint(0, 0)
              .withPoint(0.8f, 1)
              .withPoint(1, 0)
              .build()))
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("smush")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new LinearInterpolator())
          .withFilterInterpolatorGroup(new BulgeFaceFilterInterpolatorGroup())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("spiral")
          .withStartDurationEndSeconds(1, 3, 1)
          .withScaleAndTranslateInterpolatorProvider(new CircleCenterInterpolatorProvider())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("blockhead")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new LinearInterpolator())
          .withFilterInterpolatorGroup(new SumoBulgeFilterInterpolator())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("swirleyes")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new ReverseLinearInterpolator())
          .withFilterInterpolatorGroup(new UnswirlEyesFilterInterpolatorGroup())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("swirlin")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new LinearInterpolator())
          .withFilterInterpolator(new UnswirlFilterInterpolator())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("swirlio")
          .withStartDurationEndSeconds(0, 2, 0)
          .withScaleInterpolator(new InAndOutInterpolator())
          .withFilterInterpolator(new UnswirlFilterInterpolator())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("swirlout")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new ReverseLinearInterpolator())
          .withFilterInterpolator(new UnswirlFilterInterpolator())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("swirlslam")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new SlamSoftInterpolator())
          .withTranslateInterpolator(new ShakeInterpolatorProvider())
          .withFilterInterpolator(new UnswirlAtHotspotFilterInterpolator())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("swirlspot")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new LinearInterpolator())
          .withFilterInterpolator(new UnswirlAtHotspotOnHotspotFilterInterpolator())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("swirlyeyes")
          .withStartDurationEndSeconds(0, 2, 0)
          .withScaleInterpolator(new InAndOutInterpolator())
          .withFilterInterpolatorGroup(new UnswirlEyesFilterInterpolatorGroup())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("swirlyspot")
          .withStartDurationEndSeconds(0, 2, 0)
          .withScaleInterpolator(new InAndOutInterpolator())
          .withFilterInterpolator(new UnswirlAtHotspotOnHotspotFilterInterpolator())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("throwback")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(LinearSplineInterpolator.newBuilder()
              .withPoint(0, 0)
              .withPoint(0.2f, 1.3f)
              .withPoint(0.4f, 1)
              .withPoint(1, 1)
              .build())
          .withFilterInterpolator(new UnsaturateFilterInterpolator(LinearSplineInterpolator.newBuilder()
              .withPoint(0, 0)
              .withPoint(0.5f, 0)
              .withPoint(1, 1)
              .build()))
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("twistyfroggy")
          .withStartDurationEndSeconds(0, 2, 0)
          .withScaleInterpolator(new InAndOutInterpolator())
          .withFilterInterpolatorGroup(new BulgeEyesSwirlMouthFilterInterpoaltor())
          .build())
      .add(EffectTemplate.newSingleStepBuilder()
          .withName("weirdo")
          .withStartDurationEndSeconds(1, 2, 2)
          .withScaleInterpolator(new LinearInterpolator())
          .withFilterInterpolatorGroup(new SumoBulge2FilterInterpolator())
          .build())

      .add(EffectTemplate.newSingleStepBuilder()
          .withName("debug1")
          .withStartDurationEndSeconds(2, 0.2f, 2)
          .withScaleInterpolator(new LinearInterpolator())
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
