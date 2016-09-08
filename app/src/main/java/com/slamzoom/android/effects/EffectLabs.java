package com.slamzoom.android.effects;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.interpolation.filter.single.BulgeInAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.GaussianBlurFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.OverExposeFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.ShrinkInAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnderExposeFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.DesaturateFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlAtHotspotOnHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlTurntableAtHotspotOnHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.ZoomBlurAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.transform.TranslateInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashBounceBottomInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashMissInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashRumbleInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashTaranInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.BaseShakeInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.MegaShakeInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.ShakeInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.SuperShakeInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.LinearInterpolator;
import com.slamzoom.android.effects.interpolation.transform.scale.HalfInAndOutInterpolator;
import com.slamzoom.android.effects.interpolation.transform.scale.InAndOutInterpolator;
import com.slamzoom.android.effects.interpolation.transform.scale.OutAndInInterpolator;
import com.slamzoom.android.effects.interpolation.transform.scale.OvershootInterpolator;
import com.slamzoom.android.effects.interpolation.transform.scale.SlamHardInAndOutInterpolator;
import com.slamzoom.android.effects.interpolation.transform.scale.SlamHardInterpolator;
import com.slamzoom.android.effects.interpolation.transform.scale.SlamHardNoPauseInterpolator;
import com.slamzoom.android.effects.interpolation.transform.scale.SlamSoftOutInterpolator;
import com.slamzoom.android.effects.interpolation.transform.scale.SlamSoftOutNoPauseInterpolator;
import com.slamzoom.android.interpolators.spline.CubicSplineInterpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolators.spline.PointsBuilder;

import java.util.List;

/**
 * Created by clocksmith on 3/31/16.
 */
public class EffectLabs {
  public static List<EffectTemplate> getPack() {
    List<EffectTemplate> packModels = Lists.newArrayList();

//    ImmutableList.<EffectTemplate>builder()
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("blacktease")
//            .withStartDurationEndSeconds(1, 4, 2)
//            .withScaleInterpolator(new TeaseInterpolator())
//            .withFilterInterpolator(new UnderExposeFilterInterpolator(new ThreeEaseInHardOutInterpolator()))
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("blurcrash")
//            .withStartDurationEndSeconds(0.5f, 0.8f, 1)
//            .withScaleInterpolator(new SlamHardInterpolator())
//            .withFilterInterpolator(new GaussianUnblurFilterInterpolator())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("blurmagic")
//            .withStartDurationEndSeconds(0, 2, 1)
//            .withScaleInterpolator(new NoneToAllAtHalfInterpolator())
//            .withFilterInterpolator(new GuassianSuperBlurFilterInterpolator(new InAndOutInterpolator()))
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("blurslam")
//            .withStartDurationEndSeconds(0, 2, 1)
//            .withScaleInterpolator(new SlamHardInterpolator())
//            .withFilterInterpolator(new GaussianUnblurFilterInterpolator())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("blurshake")
//            .withStartDurationEndSeconds(0, 2, 1)
//            .withScaleInterpolator(new LinearInterpolator())
//            .withTranslateInterpolator(new ShakeInterpolatorProvider())
//            .withFilterInterpolator(new GaussianUnblurFilterInterpolator())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("blursmith")
//            .withStartDurationEndSeconds(0, 2, 1)
//            .withScaleInterpolator(LinearSplineInterpolator.newBuilder()
//                .withPoint(0, 0)
//                .withPoint(0.4f, 0)
//                .withPoint(0.6f, 1)
//                .withPoint(1, 1)
//                .build())
//            .withFilterInterpolator(new GaussianBlurFilterInterpolator(new InAndOutInterpolator()))
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("blurtease")
//            .withStartDurationEndSeconds(1, 4, 2)
//            .withScaleInterpolator(new TeaseInterpolator())
//            .withFilterInterpolator(new GaussianBlurFilterInterpolator(new ThreeEaseInHardOutInterpolator()))
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("crashdiag")
//            .withStartDurationEndSeconds(1, 1, 2)
//            .withTransformInterpolatorProvider(new CrashBounceDiagonalInterpolatorProvider())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("crashin")
//            .withStartDurationEndSeconds(1, 1, 2)
//            .withTransformInterpolatorProvider(new CrashBounceBottomInterpolatorProvider())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("crashmiss")
//            .withStartDurationEndSeconds(1, 0.75f, 2)
//            .withTransformInterpolatorProvider(new CrashMissInterpolatorProvider())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("crashrumble")
//            .withStartDurationEndSeconds(1, 1.5f, 0)
//            .withTransformInterpolatorProvider(new CrashRumbleInterpolatorProvider())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("earthquake")
//            .withStartDurationEndSeconds(0, 2, 0)
//            .withScaleInterpolator(new HalfInAndOutInterpolator())
//            .withTranslateInterpolator(new SuperShakeInterpolatorProvider())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("flush")
//            .withStartDurationEndSeconds(0, 3, 1)
//            .withEffectConfig(new FlushslamConfig())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("grayrumble")
//            .withStartDurationEndSeconds(1, 2, 2)
//            .withScaleInterpolator(new LinearInterpolator())
//            .withTranslateInterpolator(new ShakeInterpolatorProvider())
//            .withFilterInterpolator(new DesaturateFilterInterpolator())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("grayslam")
//            .withStartDurationEndSeconds(1, 2, 2)
//            .withScaleInterpolator(new SlamHardInterpolator())
//            .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(LinearSplineInterpolator.newBuilder()
//                .withPoint(0, 0)
//                .withPoint(0.7f, 0)
//                .withPoint(0.9f, 1)
//                .withPoint(0.9999f, 1)
//                .withPoint(1, 0)
//                .build()))
//            .withFilterInterpolator(new DesaturateFilterInterpolator())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("graytake")
//            .withStartDurationEndSeconds(0, 2, 2)
//            .withScaleInterpolator(new OutAndInInterpolator())
//            .withFilterInterpolator(new DesaturateFilterInterpolator(new LinearInterpolator()))
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("magoo")
//            .withStartDurationEndSeconds(1, 2, 2)
//            .withScaleInterpolator(new LinearInterpolator())
//            .withFilterInterpolators(new BulgeEyesFilterInterpolatorsProvider())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("djswirls")
//            .withStartDurationEndSeconds(1, 2, 2)
//            .withScaleInterpolator(new LinearInterpolator())
//            .withFilterInterpolator(new UnswirlTurntableAtHotspotOnHotspotFilterInterpolator())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("mctwisty")
//            .withStartDurationEndSeconds(1, 2, 2)
//            .withScaleInterpolator(new LinearInterpolator())
//            .withFilterInterpolators(new SwirlEyesTwistyMouthFilterInterpolator())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("multicrash")
//            .withStartDurationEndSeconds(1, 3, 1)
//            .withTransformInterpolatorProvider(new MulticrashInterpolatorProvider())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("overcrash")
//            .withStartDurationEndSeconds(1, 1, 2)
//            .withScaleInterpolator(new OvershootInterpolator())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("rumble")
//            .withStartDurationEndSeconds(0, 2, 0)
//            .withScaleInterpolator(new HalfInAndOutInterpolator())
//            .withTranslateInterpolator(new ShakeInterpolatorProvider())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("rumbleslam")
//            .withStartDurationEndSeconds(1, 2, 1)
//            .withScaleInterpolator(new SlamHardInterpolator())
//            .withTranslateInterpolator(new ShakeInterpolatorProvider())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("rumbletease")
//            .withStartDurationEndSeconds(0, 6, 0)
//            .withScaleInterpolator(new TeaseInterpolator())
//            .withTranslateInterpolator(new RumbleTeaseInterpolatorProvider())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("shrinkydink")
//            .withStartDurationEndSeconds(1, 2, 2)
//            .withScaleInterpolator(new LinearInterpolator())
//            .withFilterInterpolator(new ShrinkInAtHotspotFilterInterpolator())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("slamio")
//            .withStartDurationEndSeconds(0, 2, 0)
//            .withScaleInterpolator(new SlamHardInAndOutInterpolator())
//            .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(LinearSplineInterpolator.newBuilder()
//                .withPoint(0, 0)
//                .withPoint(0.8f, 1)
//                .withPoint(1, 0)
//                .build()))
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("spiral")
//            .withStartDurationEndSeconds(1, 3, 1)
//            .withTransformInterpolatorProvider(new CircleCenterInterpolatorProvider())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("swirleyes")
//            .withStartDurationEndSeconds(1, 2, 2)
//            .withScaleInterpolator(new ReverseLinearInterpolator())
//            .withFilterInterpolators(new UnswirlEyesFilterInterpolatorsProvider())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("swirlio")
//            .withStartDurationEndSeconds(0, 2, 0)
//            .withScaleInterpolator(new InAndOutInterpolator())
//            .withFilterInterpolator(new UnswirlFilterInterpolator())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("swirlout")
//            .withStartDurationEndSeconds(1, 2, 2)
//            .withScaleInterpolator(new ReverseLinearInterpolator())
//            .withFilterInterpolator(new UnswirlFilterInterpolator())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("swirlslam")
//            .withStartDurationEndSeconds(1, 2, 2)
//            .withScaleInterpolator(new SlamSoftInterpolator())
//            .withTranslateInterpolator(new ShakeInterpolatorProvider())
//            .withFilterInterpolator(new UnswirlAtHotspotFilterInterpolator())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("swirlin")
//            .withStartDurationEndSeconds(1, 2, 2)
//            .withScaleInterpolator(new LinearInterpolator())
//            .withFilterInterpolator(new UnswirlFilterInterpolator())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("swirlyeyes")
//            .withStartDurationEndSeconds(0, 2, 0)
//            .withScaleInterpolator(new InAndOutInterpolator())
//            .withFilterInterpolators(new UnswirlEyesFilterInterpolatorsProvider())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("swirlyspot")
//            .withStartDurationEndSeconds(0, 2, 0)
//            .withScaleInterpolator(new InAndOutInterpolator())
//            .withFilterInterpolator(new UnswirlAtHotspotOnHotspotFilterInterpolator())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("twistyfroggy")
//            .withStartDurationEndSeconds(0, 2, 0)
//            .withScaleInterpolator(new InAndOutInterpolator())
//            .withFilterInterpolators(new BulgeEyesSwirlMouthFilterInterpoaltor())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("shakezilla")
//            .withStartDurationEndSeconds(0, 2, 0)
//            .withScaleInterpolator(new HalfInAndOutInterpolator())
//            .withTranslateInterpolator(new MegaShakeInterpolatorProvider())
//            .build())
//        .add(EffectTemplate.newSingleStepBuilder()
//            .withName("weirdo")
//            .withStartDurationEndSeconds(1, 2, 2)
//            .withScaleInterpolator(new LinearInterpolator())
//            .withFilterInterpolators(new SumoBulge2FilterInterpolator())
//            .build());
//
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("crashdiag")
//        .addEffectStep(EffectStep.newBuilder()
//            .withStartPauseSeconds(0.5f)
//            .withDurationSeconds(1f)
//            .withTransformInterpolatorProvider(new CrashBounceDiagonalInterpolatorProvider())
//            .build())
//        .build());
//
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("flush")
//        .addEffectStep(EffectStep.newBuilder()
//            .withEffectConfig(new FlushslamConfig())
//            .withDurationSeconds(3f)
//            .withEndPauseSeconds(1f)
//            .build())
//        .build());
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("spiral")
//        .addEffectStep(EffectStep.newBuilder()
//            .withTransformInterpolatorProvider(new CircleCenterInterpolatorProvider())
//            .withDurationSeconds(3f)
//            .withEndPauseSeconds(1f)
//            .build())
//        .build());
//
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("swirlout")
//        .addEffectStep(EffectStep.newBuilder()
//            .withScaleInterpolator(new ReverseLinearInterpolator())
//            .withFilterInterpolator(new UnswirlFilterInterpolator())
//            .withEndPauseSeconds(0.5f)
//            .build())
//        .build());
//
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("twistyfrog")
//        .addEffectStep(EffectStep.newBuilder()
//            .withDurationSeconds(2)
//            .withScaleInterpolator(new LinearInterpolator())
//            .withFilterInterpolators(new BulgeEyesSwirlMouthFilterInterpoaltor())
//            .withEndPauseSeconds(1)
//            .build())
//        .build());
//
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("twistyfroggy")
//        .addEffectStep(EffectStep.newBuilder()
//            .withScaleInterpolator(new InAndOutInterpolator())
//            .withFilterInterpolators(new BulgeEyesSwirlMouthFilterInterpoaltor())
//            .build())
//        .build());
//
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("swirlslam")
//        .addEffectStep(EffectStep.newBuilder()
//            .withScaleInterpolator(new SlamSoftInterpolator())
//            .withTranslateInterpolator(new ShakeInterpolatorProvider())
//            .withFilterInterpolator(new UnswirlAtHotspotFilterInterpolator())
//            .withEndPauseSeconds(0.5f)
//            .build())
//        .build());
//
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("swirlyhead")
//        .addEffectStep(EffectStep.newBuilder()
//            .withScaleInterpolator(new InAndOutInterpolator())
//            .withFilterInterpolator(new UnswirlAtHotspotOnHotspotFilterInterpolator())
//            .build())
//        .build());
//
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("swirleyes")
//        .addEffectStep(EffectStep.newBuilder()
//            .withDurationSeconds(2)
//            .withScaleInterpolator(new LinearInterpolator())
//            .withFilterInterpolators(new UnswirlEyesFilterInterpolatorsProvider())
//            .withEndPauseSeconds(1)
//            .build())
//        .build());
//
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("swirlyeyes")
//        .addEffectStep(EffectStep.newBuilder()
//            .withScaleInterpolator(new InAndOutInterpolator())
//            .withFilterInterpolators(new UnswirlEyesFilterInterpolatorsProvider())
//            .build())
//        .build());
//
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("swirlaway")
//        .addEffectStep(EffectStep.newBuilder()
//            .withScaleInterpolator(new LinearInterpolator())
//            .withFilterInterpolator(new UnswirlAtHotspotFilterInterpolator())
//            .withDurationSeconds(3f)
//            .withEndPauseSeconds(0.5f)
//            .build())
//        .build());
//
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("blurslam")
//        .addEffectStep(EffectStep.newBuilder()
//            .withScaleInterpolator(new SlamHardInterpolator())
//            .withFilterInterpolator(new GaussianUnblurFilterInterpolator())
//            .withEndPauseSeconds(1f)
//            .build())
//        .build());
//
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("blurcrash")
//        .addEffectStep(EffectStep.newBuilder()
//            .withStartPauseSeconds(0.5f)
//            .withDurationSeconds(0.8f)
//            .withScaleInterpolator(new CubicSplineInterpolator(PointsBuilder.create()
//                .withPoint(0, 0)
//                .withPoint(0.3f, 1f)
//                .withPoint(1f, 1f)
//                .build()))
//            .withFilterInterpolator(new GaussianUnblurFilterInterpolator(
//                new CubicSplineInterpolator(PointsBuilder.create()
//                    .withPoint(0, 0f)
//                    .withPoint(0.8f, 0)
//                    .withPoint(1, 1)
//                    .build())))
//            .withEndPauseSeconds(0.5f)
//            .build())
//        .build());
//
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("blurshake")
//        .addEffectStep(EffectStep.newBuilder()
//            .withScaleInterpolator(new LinearInterpolator())
//            .withTranslateInterpolator(new ShakeInterpolatorProvider())
//            .withFilterInterpolator(new GaussianUnblurFilterInterpolator())
//            .withEndPauseSeconds(1f)
//            .build())
//        .build());
//
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("blursmith")
//        .addEffectStep(EffectStep.newBuilder()
//            .withScaleInterpolator(new LinearSplineInterpolator(PointsBuilder.create()
//                .withPoint(0, 0)
//                .withPoint(0.4f, 0)
//                .withPoint(0.6f, 1)
//                .withPoint(1, 1)
//                .build()))
//            .withFilterInterpolator(new GaussianBlurFilterInterpolator(new LinearSplineInterpolator(
//                PointsBuilder.create()
//                    .withPoint(0, 0)
//                    .withPoint(0.4f, 1)
//                    .withPoint(0.6f, 1)
//                    .withPoint(1, 0)
//                    .build())))
//            .withEndPauseSeconds(1f)
//            .build())
//        .build());
//
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("blurmagic")
//        .addEffectStep(EffectStep.newBuilder()
//            .withScaleInterpolator(new LinearSplineInterpolator(PointsBuilder.create()
//                .withPoint(0, 0)
//                .withPoint(0.4999f, 0)
//                .withPoint(0.5f, 1)
//                .withPoint(1, 1)
//                .build()))
//            .withFilterInterpolator(new GuassianBlurFilterInterpolator(new LinearSplineInterpolator(
//                PointListBuilder.create()
//                    .withPoint(0, 0)
//                    .withPoint(0.5f, 1)
//                    .withPoint(1, 0)
//                    .build())) {
//              @Override
//              public float getBlurSize() {
//                mBlurCalculator.setBaseValue(10);
//                return mBlurCalculator.getValueFromInterpolation();
//              }
//            })
//            .withEndPauseSeconds(1f)
//            .build())
//        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("crashmiss")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.75f)
            .withDurationSeconds(0.75f)
            .withEndPauseSeconds(2f)
            .withTransformInterpolatorProvider(new CrashMissInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("crashin")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(1f)
            .withEndPauseSeconds(2f)
            .withTransformInterpolatorProvider(new CrashBounceBottomInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("overcrash")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(1f)
            .withEndPauseSeconds(2f)
            .withScaleInterpolator(new OvershootInterpolator())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("crashblur")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.75f)
            .withDurationSeconds(0.75f)
            .withEndPauseSeconds(2f)
            .withTransformInterpolatorProvider(new CrashTaranInterpolatorProvider())
            .withFilterInterpolator(new GaussianBlurFilterInterpolator(
                new LinearSplineInterpolator(PointsBuilder.create()
                    .withPoint(0, 0)
                    .withPoint(0.3f, 1)
                    .withPoint(0.6f, 1)
                    .withPoint(0.9f, 0)
                    .withPoint(1, 0)
                    .build())) {
              @Override
              public float getBlurSize() {
                return super.getBlurSize() * 2;
              }
            })
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("crashrumble")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(1.5f)
            .withTransformInterpolatorProvider(new CrashRumbleInterpolatorProvider())
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("tile")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new LinearInterpolator())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("tile4")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new LinearInterpolator())
            .build())
        .withNumTilesInRow(2)
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("tile9")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new LinearInterpolator())
            .build())
        .withNumTilesInRow(3)
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("tile16")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new LinearInterpolator())
            .build())
        .withNumTilesInRow(4)
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("tile25")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new LinearInterpolator())
            .build())
        .withNumTilesInRow(5)
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("bulge")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withEndPauseSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolator(new BulgeInAtHotspotFilterInterpolator())
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("shrinkydink")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolator(new ShrinkInAtHotspotFilterInterpolator())
            .withEndPauseSeconds(2)
            .build())
        .build());

//    packModels.add(EffectTemplate.newBuilder()
//        .withName("smush")
//        .addEffectStep(EffectStep.newBuilder()
//            .withDurationSeconds(2)
//            .withEndPauseSeconds(2)
//            .withScaleInterpolator(new LinearInterpolator())
//            .withFilterInterpolators(new BulgeFaceFilterInterpolatorsProvider())
//            .build())
//        .build());
//
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("inflate")
//        .addEffectStep(EffectStep.newBuilder()
//            .withDurationSeconds(2)
//            .withScaleInterpolator(new LinearInterpolator())
//            .withFilterInterpolators(new InflateFaceFilterInterpolatorsProvider())
//            .withEndPauseSeconds(2)
//            .build())
//        .build());
//
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("sumo")
//        .addEffectStep(EffectStep.newBuilder()
//            .withDurationSeconds(2)
//            .withScaleInterpolator(new LinearInterpolator())
//            .withFilterInterpolators(new SumoBulgeFilterInterpolator())
//            .withEndPauseSeconds(2)
//            .build())
//        .build());
//
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("doublebulge")
//        .addEffectStep(EffectStep.newBuilder()
//            .withDurationSeconds(2)
//            .withEndPauseSeconds(2)
//            .withScaleInterpolator(new LinearInterpolator())
//            .withFilterInterpolators(new BulgeDoubleLeftRightFilterInterpolatorsProvider())
//            .build())
//        .build());
//
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("magoo")
//        .addEffectStep(EffectStep.newBuilder()
//            .withDurationSeconds(2)
//            .withEndPauseSeconds(2)
//            .withScaleInterpolator(new LinearInterpolator())
//            .withFilterInterpolators(new BulgeEyesFilterInterpolatorsProvider())
//            .build())
//        .build());
//
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("deflate")
//        .addEffectStep(EffectStep.newBuilder()
//            .withDurationSeconds(2)
//            .withScaleInterpolator(new LinearInterpolator())
//            .withFilterInterpolators(new DeflateFaceFilterInterpolatorsProvider())
//            .withEndPauseSeconds(2)
//            .build())
//        .build());
//
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("bulgeswap")
//        .addEffectStep(EffectStep.newBuilder()
//            .withDurationSeconds(2)
//            .withEndPauseSeconds(2)
//            .withScaleInterpolator(new LinearInterpolator())
//            .withFilterInterpolators(new BulgeLeftRightSwapFilterInterpolationGroup())
//            .build())
//        .build());
//
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("weirdo")
//        .addEffectStep(EffectStep.newBuilder()
//            .withDurationSeconds(2)
//            .withScaleInterpolator(new LinearInterpolator())
//            .withFilterInterpolators(new SumoBulge2FilterInterpolator())
//            .withEndPauseSeconds(2)
//            .build())
//        .build());
//
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("grayson")
//        .addEffectStep(EffectStep.newBuilder()
//            .withScaleInterpolator(new LinearInterpolator())
//            .withFilterInterpolator(new DesaturateFilterInterpolator())
//            .withEndPauseSeconds(2f)
//            .build())
//        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("grayslam")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new SlamHardInterpolator())
            .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(new LinearSplineInterpolator(
                PointsBuilder.create()
                    .withPoint(0, 0)
                    .withPoint(0.7f, 0)
                    .withPoint(0.9f, 1f)
                    .withPoint(0.9999f, 1f)
                    .withPoint(1f, 0)
                    .build())))
            .withFilterInterpolator(new DesaturateFilterInterpolator())
            .withEndPauseSeconds(2f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("grayfreeze")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(2f)
            .withScaleInterpolator(new CubicSplineInterpolator(PointsBuilder.create()
                .withPoint(0, 0)
                .withPoint(0.15f, 1f)
                .withPoint(1f, 1f)
                .build()))
            .withFilterInterpolator(new DesaturateFilterInterpolator(
                new CubicSplineInterpolator(PointsBuilder.create()
                    .withPoint(0, 0)
                    .withPoint(0.5f, 0)
                    .withPoint(1f, 1f)
                    .build())))
            .withEndPauseSeconds(2f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("grayrumble")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new LinearInterpolator())
            .withTranslateInterpolator(new ShakeInterpolatorProvider())
            .withFilterInterpolator(new DesaturateFilterInterpolator())
            .withEndPauseSeconds(2f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("graytake")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new OutAndInInterpolator())
            .withFilterInterpolator(new DesaturateFilterInterpolator(new LinearInterpolator()))
            .withEndPauseSeconds(2f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("rumble")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new HalfInAndOutInterpolator())
            .withTranslateInterpolator(new ShakeInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("earthquake")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new HalfInAndOutInterpolator())
            .withTranslateInterpolator(new SuperShakeInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("shakezilla")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new HalfInAndOutInterpolator())
            .withTranslateInterpolator(new MegaShakeInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("rumbleslam")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new SlamHardInterpolator())
            .withTranslateInterpolator(new ShakeInterpolatorProvider())
            .withEndPauseSeconds(1)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withPackName("slam pack")
        .withName("slamin")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(1.4f)
            .withDurationSeconds(0.6f)
            .withEndPauseSeconds(2)
            .withScaleInterpolator(new SlamHardNoPauseInterpolator())
            .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(new LinearSplineInterpolator(
                PointsBuilder.create()
                    .withPoint(0, 0)
                    .withPoint(0.9f, 1)
                    .withPoint(0.9999f, 1)
                    .withPoint(1, 0)
                    .build())))
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withPackName("slam pack")
        .withName("slamout")
        .addEffectStep(EffectStep.newBuilder()
//            .withStartPauseSeconds(1.2f)
//            .withDurationSeconds(0.8f)
            .withEndPauseSeconds(2)
//            .withScaleInterpolator(new SlamSoftOutNoPauseInterpolator())
            .withScaleInterpolator(new SlamSoftOutInterpolator())
            .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(new LinearSplineInterpolator(
                PointsBuilder.create()
                    .withPoint(0, 0)
                    .withPoint(0.8f, 1)
                    .withPoint(1, 0)
                    .build())))
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withPackName("slam pack")
        .withName("slamio")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(1f)
            .withScaleInterpolator(new SlamHardInAndOutInterpolator())
            .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(new LinearSplineInterpolator(
                PointsBuilder.create()
                    .withPoint(0, 0)
                    .withPoint(0.35f, 0)
                    .withPoint(0.45f, 1f)
                    .withPoint(0.4999f, 1f)
                    .withPoint(0.5f, 0)
                    .withPoint(0.85f, 0)
                    .withPoint(0.95f, 1f)
                    .withPoint(0.9999f, 1f)
                    .withPoint(1f, 0)
                    .build())))
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("slamfinity")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0)
            .withDurationSeconds(0.5f)
            .withScaleInterpolator(new SlamHardNoPauseInterpolator())
            .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(new LinearSplineInterpolator(
                PointsBuilder.create()
                    .withPoint(0, 0)
                    .withPoint(0.9f, 1f)
                    .withPoint(0.9999f, 1f)
                    .withPoint(1f, 0)
                    .build())))
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("slamfunity")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0)
            .withDurationSeconds(0.5f)
            .withScaleInterpolator(new SlamSoftOutNoPauseInterpolator())
            .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(new LinearSplineInterpolator(
                PointsBuilder.create()
                    .withPoint(0, 0)
                    .withPoint(0.9f, 1f)
                    .withPoint(0.9999f, 1f)
                    .withPoint(1f, 0)
                    .build())))
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlin")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolator(new UnswirlFilterInterpolator())
            .withEndPauseSeconds(2)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlinout")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new InAndOutInterpolator())
            .withFilterInterpolator(new UnswirlFilterInterpolator())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlspot")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolator(new UnswirlAtHotspotOnHotspotFilterInterpolator())
            .withDurationSeconds(3f)
            .withEndPauseSeconds(2)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("djswirls")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolator(new UnswirlTurntableAtHotspotOnHotspotFilterInterpolator())
            .withEndPauseSeconds(2)
            .build())
        .build());
//    packModels.add(EffectTemplate.newBuilder()
//        .withName("mctwisty")
//        .addEffectStep(EffectStep.newBuilder()
//            .withDurationSeconds(2)
//            .withScaleInterpolator(new LinearInterpolator())
//            .withFilterInterpolators(new SwirlEyesTwistyMouthFilterInterpolator())
//            .withEndPauseSeconds(2)
//            .build())
//        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("blacktease")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(1)
            .withDurationSeconds(4f)
            .withScaleInterpolator(new Interpolator() {
              @Override
              public float getValue(float t) {
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
                  public float getValue(float t) {
                    return (float) Math.pow(Math.sin(3 * Math.PI * t), 10);
                  }
                }))
            .withEndPauseSeconds(2)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("whitetease")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(1)
            .withDurationSeconds(4)
            .withScaleInterpolator(new Interpolator() {
              @Override
              public float getValue(float t) {
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
                  public float getValue(float t) {
                    return (float) Math.pow(Math.sin(3 * Math.PI * t), 10);
                  }
                }))
            .withEndPauseSeconds(2)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("blurtease")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(1)
            .withDurationSeconds(4)
            .withScaleInterpolator(new Interpolator() {
              @Override
              public float getValue(float t) {
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
                  public float getValue(float t) {
                    return (float) Math.pow(Math.sin(3 * Math.PI * t), 10);
                  }
                }))
            .withEndPauseSeconds(2)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("rumbletease")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(6)
            .withScaleInterpolator(new Interpolator() {
              @Override
              public float getValue(float t) {
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
              private BaseShakeInterpolatorProvider first = new BaseShakeInterpolatorProvider(6, 12);
              private BaseShakeInterpolatorProvider second = new BaseShakeInterpolatorProvider(5, 15);
              private BaseShakeInterpolatorProvider third = new BaseShakeInterpolatorProvider(4, 20);
              private BaseShakeInterpolatorProvider fourth = new BaseShakeInterpolatorProvider(2, 30);
              @Override
              public Interpolator getXInterpolator() {
                return new Interpolator() {
                  @Override
                  public float getValue(float t) {
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
                  public float getValue(float t) {
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
              public float getValue(float t) {
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
            .withFilterInterpolator(new DesaturateFilterInterpolator(
                new Interpolator() {
                  @Override
                  public float getValue(float t) {
                    return (float) Math.pow(Math.sin(3.5 * Math.PI * (t + 2f / 7)), 2);
                  }
                }))
            .withEndPauseSeconds(2)
            .build())
        .build());


    return packModels;
  }
}
